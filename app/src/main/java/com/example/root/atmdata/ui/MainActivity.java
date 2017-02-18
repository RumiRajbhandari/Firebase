package com.example.root.atmdata.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.root.atmdata.AtmDetails;
import com.example.root.atmdata.util.BankListener;
import com.example.root.atmdata.util.MyConstants;
import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class
MainActivity extends BaseActivity implements ValueEventListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mBankConnection;
    private FirebaseDatabase mFirebaseInstance;



    BankListener bankListener;
    String bankString;

    List<Bank> bankList=new ArrayList<>();


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;

    private static final String TAG = "MainActivity";

    @Override
    public int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mBankConnection = mFirebaseInstance.getReference("bank");
        mBankConnection.addValueEventListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));



    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

         bankList = new ArrayList<>();


        // initial looping for 0, 1, 2 ...

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//            Log.e(TAG, "snapshot before"+snapshot );
            Bank bank = new Bank();
            bank.setName((String) snapshot.child(MyConstants.KEY_NAME).getValue());
            bank.setAddress((String) snapshot.child(MyConstants.KEY_ADDRESS).getValue());
            bank.setPhone((String) snapshot.child(MyConstants.KEY_PHONE).getValue());
            bank.setEmail((String) snapshot.child(MyConstants.KEY_EMAIL).getValue());
            bank.setHeadOffice((String) snapshot.child(MyConstants.KEY_HEAD_OFFICE)
                    .getValue());
            bank.setOpeningHours((String) snapshot.child(MyConstants.KEY_OPENING_HOURS)
                    .getValue());

            List<Atm> atmList = new ArrayList<>();
            // looping for atm 0, 1, 2 ...
            for (DataSnapshot atmsnapshot : snapshot.child(MyConstants.KEY_ATM_LIST).getChildren()) {
                Atm atm = new Atm();
                String location = atmsnapshot.child(MyConstants.KEY_LOCATION)
                        .getValue().toString();
                atm.setLat(Double.parseDouble(location.split(",")[0]));
                atm.setLon(Double.parseDouble(location.split(",")[1]));
                atm.setReference(atmsnapshot.getRef().toString());
                atmList.add(atm);
            }
            bank.setAtmlist(atmList);

            bankList.add(bank);
        }
        Log.e(TAG, "After fetching data" );
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
         bankString=gson.toJson(bankList);



        int position = 1;
        Bank ban = bankList.get(position);
        List<Atm> atm = ban.getAtmlist();
        Atm atm2 = atm.get(position);
        Intent i = new Intent(this, AtmDetails.class);

//        String b = atm2.getReference();
//        i.putExtra("bank", b);
//        startActivity(i);

            bankListener.onBankListUpdate(bankList);


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // space for rent
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Log.e(TAG, "onNavigationItemSelected: " );

        switch (item.getItemId()) {
            case R.id.navigation_bank:
                bankListener=BankListFragment.newInstance(bankList);

                break;
            case R.id.navigation_map:
                bankListener=MapFragment.newInstance(bankList);

                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, bankListener.getFragment()).commit();
        drawerLayout.closeDrawer(Gravity.START);
        item.setChecked(true);
        return false;
    }


}
