package com.example.root.atmdata;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by root on 1/23/17.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
