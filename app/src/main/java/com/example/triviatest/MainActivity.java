package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;
import com.example.triviatest.model.Score;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";
    private static final String TRIVIA_PREFS = "travia";
    private static final String SCORE = "score" ;

    private int scoreCounter =0;
    private Score score;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;

    CardView cardView;
    ImageButton nextButton , backButton;
    Button btnTrue , btnFalse;
    TextView txtCounter , txtQuestion ,txtScore;
    private int currentQuestionIndex = 0;
    private List<Question> questionsList;
    public static final String QUESTION_BANK ="question_bank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        score = new Score();
        Gson gson = new Gson();
        sharedPreferences = getSharedPreferences(TRIVIA_PREFS ,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Type questionType = new TypeToken<List<Question>>(){}.getType();

        ArrayList<Question> questionsBank = gson.fromJson(sharedPreferences.getString(QUESTION_BANK , null) , questionType);

        String lastScore = sharedPreferences.getString(SCORE , "0");
        if( null != questionsBank && !lastScore.equals("")){
            Log.d(TAG, "onCreate: inside if statement");
            questionsList =  questionsBank;
             showinital(questionsBank , lastScore);

        }else{
            Log.d(TAG, "onCreate: inside else statement");
           questionsList = new QuestionBank().getQuestions(questionBanks -> {

                editor.putString(QUESTION_BANK  , gson.toJson(questionBanks));
                editor.commit();

                showinital(questionBanks , String.valueOf(scoreCounter));

            });
        }


    
       

        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btnFalse.setOnClickListener(this);
        btnTrue.setOnClickListener(this);

    }

    private void showinital(ArrayList<Question> questionsBank, String lastScore) {


        String question = (currentQuestionIndex+1) + ": "+questionsBank.get(currentQuestionIndex).getQuestion()+"?";
        String count =currentQuestionIndex+ " / " + questionsList.size();
        txtCounter.setText(count);
        txtQuestion.setText(question);
        txtScore.setText(String.format("Score: %s", lastScore));


    }

    private void initViews() {
        txtCounter = findViewById(R.id.txtCounter);
        nextButton = findViewById(R.id.imgNext);
        backButton = findViewById(R.id.imgBack);
        btnFalse = findViewById(R.id.btnFalse);
        btnTrue = findViewById(R.id.btnTrue);
        txtQuestion = findViewById(R.id.txtQuestion);
        cardView = findViewById(R.id.cardView);
        txtScore = findViewById(R.id.txtScore);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgNext:

                currentQuestionIndex = (currentQuestionIndex+1)%questionsList.size();
                updateQuestion();

                break;
            case R.id.imgBack:
                if( currentQuestionIndex > 0){

                    currentQuestionIndex = (currentQuestionIndex-1)%questionsList.size();
                    updateQuestion();
                }

                break;
            case R.id.btnFalse:
                checkAnswer(false);
                updateScore();
                updateQuestion();

                break;
            case R.id.btnTrue:
                checkAnswer(true);
                updateScore();
                updateQuestion();
                break;
            default:
                break;

        }
    }

    private void updateScore() {
        String currentScore = "Score: " + score.getScore();
        txtScore.setText(currentScore);

    }

    private void checkAnswer(boolean b) {
        boolean answerIsTrue  = questionsList.get(currentQuestionIndex).isAnswer();
        Log.d(TAG, "checkAnswer: " + answerIsTrue + ":" +b + ":" + currentQuestionIndex);
        String toastMessageId  = "";
        if( b == answerIsTrue){
            toastMessageId = "Correct";
            addPoint();
            fadeView();
            currentQuestionIndex +=1;
        }else{
            shakeAnimation();
            minusPoint();

            toastMessageId = "Wrong";
        }

        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();

    }

    private void minusPoint() {
        scoreCounter -= 100;
        if( scoreCounter > 0){

            score.setScore(scoreCounter);
        }else{

            scoreCounter = 0;
            score.setScore(scoreCounter);
        }


    }

    private void addPoint() {

        scoreCounter += 100;
        score.setScore(scoreCounter);
    }

    private void updateQuestion() {

        String question = (currentQuestionIndex+1) + ": "+questionsList.get(currentQuestionIndex).getQuestion()+"?";
        String count =currentQuestionIndex+ " / " + questionsList.size();
        txtCounter.setText(count);
        txtQuestion.setText(question);
    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(this , R.anim.shake_animation);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void fadeView(){


        AlphaAnimation alphaAnimation  = new AlphaAnimation(1.0f , 0.0f);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);


        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                cardView.setCardBackgroundColor(Color.GREEN);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    protected void onPause() {

        super.onPause();
        sharedPreferences = getSharedPreferences(TRIVIA_PREFS ,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SCORE , String.valueOf(score.getScore()));
        editor.apply();


    }
}