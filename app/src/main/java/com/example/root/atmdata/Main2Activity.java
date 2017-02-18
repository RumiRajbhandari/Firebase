package com.example.root.atmdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class Main2Activity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        sharedpreferences = getSharedPreferences("AtmDataPreference", Context.MODE_PRIVATE);
        sharedpreferences.getString("username",null);

    }
}
