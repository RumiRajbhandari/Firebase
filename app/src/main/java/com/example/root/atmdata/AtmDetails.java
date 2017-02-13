package com.example.root.atmdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.util.MyConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by root on 2/13/17.
 */

public class AtmDetails extends BaseActivity implements ValueEventListener{
    private DatabaseReference mBankConnection;
    private FirebaseDatabase mFirebaseInstance;

    TextView bankName,phone,email,openingHour,headOffice;
    ImageView image;

    @Override
    public int layout() {
        return R.layout.atm_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bankName=(TextView)findViewById(R.id.bank_name);
        phone=(TextView)findViewById(R.id.phone);
        email=(TextView)findViewById(R.id.email);
        openingHour=(TextView)findViewById(R.id.opening_hour);
        headOffice=(TextView)findViewById(R.id.head_office);
        image=(ImageView)findViewById(R.id.image);

        Intent i=getIntent();
        String ban= (String) i.getSerializableExtra("bank");

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mBankConnection=mFirebaseInstance.getReferenceFromUrl(ban).getParent().getParent();
        Log.e("TAG", "Mbankconnection: "+mBankConnection );
        mBankConnection.addValueEventListener(this);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Bank bank = new Bank();
        HashMap<String,String> hm=new HashMap<>();
        for (DataSnapshot snapshot:dataSnapshot.getChildren())
        {

            hm.put(snapshot.getKey(),snapshot.getValue().toString());

        }
        bankName.setText(hm.get(MyConstants.KEY_NAME));
        phone.setText(hm.get(MyConstants.KEY_PHONE));
        email.setText(hm.get(MyConstants.KEY_EMAIL));
        headOffice.setText(hm.get(MyConstants.KEY_HEAD_OFFICE));
        openingHour.setText(hm.get(MyConstants.KEY_OPENING_HOURS));
        Picasso.with(this)
                .load(hm.get(MyConstants.KEY_URL))
                .into(image);


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
