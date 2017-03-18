package com.example.root.atmdata.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;

public class MainActivity extends BaseActivity implements ValueEventListener,
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private List<Bank> bankList = new ArrayList<>();
    private BankListener bankListener;

    GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    LocationRequest mLocationRequest;


    private static final String TAG = "MainActivity";

    @Override
    public int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        // initialize database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bankReference = database.getReference(Bank.EXTRA_KEY);
        bankReference.addValueEventListener(this);


        // initialize navigation

        navigationView.setNavigationItemSelectedListener(this);
        // default selection for navigation
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        if (isLocationPermissionGranted()) initApiClient();
        else requestForPermission();

        // init api client
        // but before client initialization request permission for location
        // check self permission
        // ContextCompat.checkSelfPermission()
        // if true proceed to init api client
        // if false ask for permission
        // ActivityCompat.requestPermissions();
        // read more -> https://developer.android.com/training/permissions/requesting.html
    }

    private boolean isLocationPermissionGranted() {
        Log.d(TAG, "isLocationPermissionGranted() called");
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }

    private void requestForPermission() {
        Log.d(TAG, "requestForPermission() called");
        // justify why we need location request, if necessary
        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION))) {
            new AlertDialog.Builder(this).setTitle("Request for permission")
                    .setMessage("The app needs location permission for map")
                    .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // redirect user to settings page
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return;
        }

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                100);
    }

    private void initApiClient() {
        Log.d(TAG, "initApiClient() called");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "onStart: not null");
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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


    // called when google's api client is connected
    // you can either request last know location -> https://developer.android.com/training/location/retrieve-current.html#last-known
    // or register for location updates -> https://developer.android.com/training/location/receive-location-updates.html#updates


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
        startLocationUpdates();
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                onLocationChanged(location);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(2 * 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: ");
        if (requestCode == 100) {
            for (int result : grantResults) {
                if (result == PERMISSION_DENIED) return;
            }
            initApiClient();
        }
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
        Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
        // receive location updates from here, if you've registered for location updates,
        // send update to respective fragment, we will only have to listen for updates in map
        // e.g.
        lastLocation = location;
        bankListener.onLocationUpdate(new LatLng(location.getLatitude(), location.getLongitude()));
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // reset bank
        List<Bank> newBankList = new ArrayList<>();
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

            List<Atm> atmList = new ArrayList<>();
            // looping for atm 0, 1, 2 ...
            for (DataSnapshot atmSnapshot : snapshot.child(MyConstants.KEY_ATM_LIST).getChildren()) {
                Atm atm = new Atm();
                String location = atmSnapshot.child(MyConstants.KEY_LOCATION).getValue().toString();
                atm.setLatitude(Double.parseDouble(location.split(",")[0]));
                atm.setLongitude(Double.parseDouble(location.split(",")[1]));
                atm.setStatus((String) atmSnapshot.child(MyConstants.KEY_STATUS).getValue());
                atm.setReference(atmSnapshot.getRef().toString());
                atmList.add(atm);
            }
            bank.setAtmList(atmList);
            newBankList.add(bank);
        }

        if (!newBankList.isEmpty()) {
            bankListener.onBankListUpdate(newBankList);
            bankList = newBankList;
        }
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
                    bankListener = MapFragment.newInstance(bankList, lastLocation == null ? null :
                            new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    break;


        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, bankListener.getFragment()).commit();
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }
}
