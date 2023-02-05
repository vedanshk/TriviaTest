package com.example.triviatest.data;

import android.util.Log;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.triviatest.controller.AppController;
import com.example.triviatest.model.Question;


import org.json.JSONException;

import java.util.ArrayList;

public class QuestionBank {
        private static final String TAG = "QuestionBank";

        ArrayList<Question> questionsArrayList = new ArrayList<>();


        public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callback){



                String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements.json";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET, url, null, response -> {


                        for (int i = 0;  i<response.length();++i){
                                try {
                                      String question = response.getJSONArray(i).get(0).toString();
                                      boolean answer = Boolean.parseBoolean(response.getJSONArray(i).get(1).toString());
                                      questionsArrayList.add(new Question(question , answer));

                                } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                }

                        }
                        if( null != callback) callback.processFinished(questionsArrayList);

                        }, error -> {

                        }
                );

                AppController.getInstance().addToRequestQueue(jsonArrayRequest);


                return questionsArrayList;


        };





}
