package com.example.triviatest.controller;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();

    private static RequestQueue mRequestQueue;
    private static AppController mInstance;

    public static synchronized  AppController getInstance(){

//        if(mInstance == null){
//            mInstance = new AppController();
//        }
        return mInstance;

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: started");
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req , String tag){

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);

    }

    public <T> void addToRequestQueue(Request<T> req){

        req.setTag(TAG);
        getRequestQueue().add(req);

    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
