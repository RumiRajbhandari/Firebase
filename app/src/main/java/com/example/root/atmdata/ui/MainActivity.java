package com.example.root.atmdata.ui;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ValueEventListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mBankConnection;
    private FirebaseDatabase mFirebaseInstance;

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_bank:
                fragment = new BankListFragment();
                break;
            case R.id.navigation_map:
                fragment = new MapFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
        drawerLayout.closeDrawer(Gravity.START);
        return false;
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

        List<Bank> bankList = new ArrayList<>();

        // initial looping for 0, 1, 2 ...
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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
                atmList.add(atm);
            }

            Log.e(TAG, bank.toString());

            bankList.add(bank);
        }

//        bankListener.onBankListUpdate(bankList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // space for rent
    }

}
