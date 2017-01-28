package com.example.root.atmdata;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.root.atmdata.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private String bankId;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Bank bank = new Bank();
    private ActivityMainBinding mainBinding;

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setBank(bank);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        //get reference to 'bank'node
        mFirebaseDatabase = mFirebaseInstance.getReference(Bank.FIREBASE_KEY);

        mainBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankId = mFirebaseDatabase.push().getKey();
                Log.d(TAG, "onClick: " + bank);
                mFirebaseDatabase.child(bankId).setValue(bank);
            }
        });

        mainBinding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank = new Bank();
            }
        });
    }
}
