package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.triviatest.controller.AppController;
import com.example.triviatest.data.QuestionBank;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QuestionBank().getQuestions();
    }
}