package com.example.root.atmdata.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.util.BankListener;
import com.example.root.atmdata.util.MyConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ValueEventListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private List<Bank> bankList = new ArrayList<>();
    private BankListener bankListener;
    private FirebaseDatabase database;
    private DatabaseReference bankReference;

    /**
     * todo move google api client implementation here, delegate location updates to fragment
     */


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

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        // initialize database
        database = FirebaseDatabase.getInstance();
        bankReference = database.getReference(Bank.EXTRA_KEY);
        bankReference.addValueEventListener(this);

        // initialize navigation

        navigationView.setNavigationItemSelectedListener(this);
        // default selection for navigation
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // reset bank
        bankList = new ArrayList<>();
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
            bank.setUrl((String) snapshot.child(MyConstants.KEY_URL).getValue());
            Log.e(TAG, "onDataChange: "+bank.getName() );

            List<Atm> atmList = new ArrayList<>();
            // looping for atm 0, 1, 2 ...
            for (DataSnapshot atmSnapshot : snapshot.child(MyConstants.KEY_ATM_LIST).getChildren()) {
                Log.e(TAG, "onDataChange: "+atmSnapshot.toString() );
                Atm atm = new Atm();
                String location = atmSnapshot.child(MyConstants.KEY_LOCATION).getValue().toString();
                atm.setLatitude(Double.parseDouble(location.split(",")[0]));
                atm.setLongitude(Double.parseDouble(location.split(",")[1]));
               atm.setStatus((String)atmSnapshot.child(MyConstants.KEY_STATUS).getValue());
//                if (snapshot.child(MyConstants.KEY_STATUS) != null && snapshot.child(MyConstants.KEY_STATUS).getValue() != null)
//                    atm.setStatus((snapshot.child(MyConstants.KEY_STATUS).getValue().toString()));
                //Log.e(TAG, "onDataChange: "+snapshot.child(MyConstants.KEY_STATUS).getValue().toString());
                atm.setReference(atmSnapshot.getRef().toString());
                atmList.add(atm);
            }
            bank.setAtmList(atmList);
            bankList.add(bank);
        }

        bankListener.onBankListUpdate(bankList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // space for rent
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        // todo add check if fragment exists
        switch (item.getItemId()) {
            case R.id.navigation_bank:

                bankListener = BankListFragment.newInstance(bankList);
                break;
            case R.id.navigation_map:
                // todo send current user location
                bankListener = MapFragment.newInstance(bankList, null);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, bankListener.getFragment()).commit();
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }
}
