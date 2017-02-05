package com.example.root.atmdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button add_btn,list_btn;
    private EditText lat;
    private EditText lon;
    private EditText status;
    private String bankId;
    private DatabaseReference mFirebaseDatabase,mFireBase2;
    private FirebaseDatabase mFirebaseInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        add_btn=(Button)findViewById(R.id.add);
        list_btn=(Button)findViewById(R.id.list);
        lat=(EditText)findViewById(R.id.lat);
        lon=(EditText)findViewById(R.id.lon);
        status=(EditText)findViewById(R.id.status);

        final ArrayList<Atm> atms= new ArrayList<Atm>();

        mFirebaseInstance=FirebaseDatabase.getInstance();
        //get refrence to 'bank'node
       mFirebaseDatabase=mFirebaseInstance.getReference("bank/atmlist");
        mFireBase2=mFirebaseInstance.getReference("bank");
        //store

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double lat2=Double.parseDouble(lat.getText().toString());
                Double lon2=Double.parseDouble(lon.getText().toString());
                Boolean status2=Boolean.parseBoolean(status.getText().toString());

                Atm atm2=new Atm(lat2,lon2,status2);
                Atm atm3=new Atm(2.0123,2.0142,Boolean.TRUE);

                atms.add(atm2);
                atms.add(atm3);
                mFirebaseDatabase.setValue(atms);

            }
        });
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mFireBase2.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //ArrayList<Bank> banklist=new ArrayList<Bank>();
                        Bank bank1;
                        bank1 = dataSnapshot.getValue(Bank.class);
                        Log.d("rumi","Name"+bank1.getName()+" , email "+bank1.getEmail()+" , add "+bank1.getAddress());
                        ArrayList<Atm> atm=new ArrayList<Atm>(bank1.getAtmlist());

                        for (Atm atm1:atm
                             ) {
                            Log.d("rumi","Lat "+atm1.getLat());
                            Log.d("rumi","Lon "+atm1.getLon());
                            Log.d("rumi","status "+atm1.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("rumi","error generated");
                    }
                });

            }
        });
    }
}
