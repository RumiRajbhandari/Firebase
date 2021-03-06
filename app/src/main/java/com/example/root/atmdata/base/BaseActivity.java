package com.example.root.atmdata.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.root.atmdata.R;

/**
 * Created by root on 2/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int layout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
    }
}
