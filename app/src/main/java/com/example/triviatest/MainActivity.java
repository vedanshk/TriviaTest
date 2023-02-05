package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviatest.data.AnswerListAsyncResponse;
import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "MainActivity";

    CardView cardView;
    ImageButton nextButton , backButton;
    Button btnTrue , btnFalse;
    TextView txtCounter , txtQuestion;
    private int currentQuestionIndex = 0;
    private List<Question> questionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        questionsList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionBanks) {

                String question = (currentQuestionIndex+1) + ": "+questionBanks.get(currentQuestionIndex).getQuestion()+"?";
                String count =currentQuestionIndex+ " / " + questionsList.size();
                txtCounter.setText(count);
             txtQuestion.setText(question);

            }
        });

        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btnFalse.setOnClickListener(this);
        btnTrue.setOnClickListener(this);

    }

    private void initViews() {
        txtCounter = findViewById(R.id.txtCounter);
        nextButton = findViewById(R.id.imgNext);
        backButton = findViewById(R.id.imgBack);
        btnFalse = findViewById(R.id.btnFalse);
        btnTrue = findViewById(R.id.btnTrue);
        txtQuestion = findViewById(R.id.txtQuestion);
        cardView = findViewById(R.id.cardView);
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
                updateQuestion();
                break;
            case R.id.btnTrue:
                checkAnswer(true);
                updateQuestion();
                break;
            default:
                break;

        }
    }

    private void checkAnswer(boolean b) {
        boolean answerIsTrue  = questionsList.get(currentQuestionIndex).isAnswer();
        String toastMessageId  = "";
        if( b == answerIsTrue){
            toastMessageId = "Correct";
        }else{
            shakeAnimation();
            toastMessageId = "Wrong";
        }

        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();

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
}