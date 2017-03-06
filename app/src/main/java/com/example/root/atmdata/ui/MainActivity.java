package com.example.root.atmdata.ui;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ValueEventListener,
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

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
     * as found in
     * https://developer.android.com/training/location/retrieve-current.html
     */
    GoogleApiClient apiClient;


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

        // init api client
        // but before client initialization request permission for location
        // check self permission
        // ContextCompat.checkSelfPermission()
        // if true proceed to init api client
        // if false ask for permission
        // ActivityCompat.requestPermissions();
        // read more -> https://developer.android.com/training/permissions/requesting.html
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
    public void onConnected(@Nullable Bundle bundle) {
        // called when google's api client is connected
        // you can either request last know location -> https://developer.android.com/training/location/retrieve-current.html#last-known
        // or register for location updates -> https://developer.android.com/training/location/receive-location-updates.html#updates
    }

    @Override
    public void onConnectionSuspended(int i) {
        // do nothing
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // do nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        // recieve location updates from here, if you've registered for location updates,
        // send update to respective fragment, we will only have to listen for updates in map
        // e.g.
        // bankListener.onLocationUpdate(new LatLng(location.getLatitude(), location.getLongitude()));
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
            Log.e(TAG, "onDataChange: " + bank.getName());

            List<Atm> atmList = new ArrayList<>();
            // looping for atm 0, 1, 2 ...
            for (DataSnapshot atmSnapshot : snapshot.child(MyConstants.KEY_ATM_LIST).getChildren()) {
                Log.e(TAG, "onDataChange: " + atmSnapshot.toString());
                Atm atm = new Atm();
                String location = atmSnapshot.child(MyConstants.KEY_LOCATION).getValue().toString();
                atm.setLatitude(Double.parseDouble(location.split(",")[0]));
                atm.setLongitude(Double.parseDouble(location.split(",")[1]));
                atm.setStatus((String) atmSnapshot.child(MyConstants.KEY_STATUS).getValue());
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
