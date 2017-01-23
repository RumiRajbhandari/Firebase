package com.example.root.atmdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button add_btn;
    private EditText bankName;
    private EditText bankAddress;
    private EditText bankEmail;
    private String bankId;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        add_btn=(Button)findViewById(R.id.add);
        bankName=(EditText)findViewById(R.id.bank_name);
        bankAddress=(EditText)findViewById(R.id.bank_address);
        bankEmail=(EditText)findViewById(R.id.bank_email);

        mFirebaseInstance=FirebaseDatabase.getInstance();
        //get refrence to 'bank'node
       mFirebaseDatabase=mFirebaseInstance.getReference("bank");
        //store

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=bankName.getText().toString();
                String address=bankAddress.getText().toString();
                String email=bankEmail.getText().toString();


                bankId = mFirebaseDatabase.push().getKey();

                Bank bank = new Bank(name,address, email);

                mFirebaseDatabase.child(bankId).setValue(bank);


            }
        });
    }
}
