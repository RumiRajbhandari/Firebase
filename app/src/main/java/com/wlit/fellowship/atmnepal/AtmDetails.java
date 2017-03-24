package com.wlit.fellowship.atmnepal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlit.fellowship.atmnepal.base.BaseActivity;
import com.wlit.fellowship.atmnepal.model.Atm;
import com.wlit.fellowship.atmnepal.model.Bank;
import com.wlit.fellowship.atmnepal.ui.ScrollCompatibleMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by root on 2/13/17.
 */
public class AtmDetails extends BaseActivity implements GoogleMap.OnInfoWindowClickListener {

    private NestedScrollView container;
    private TextView bankName, phone, email, openingHour, headOffice;
    private ImageView image;

    private double lat, lon;
    // using data binding here would be more fruitful
    private Atm atm;
    private Bank bank;
    private LatLng latLng;


    @Override
    public int layout() {
        return R.layout.atm_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark_normal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        container = (NestedScrollView) findViewById(R.id.container);
        bankName = (TextView) findViewById(R.id.bank_name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        openingHour = (TextView) findViewById(R.id.opening_hour);
        headOffice = (TextView) findViewById(R.id.head_office);
        image = (ImageView) findViewById(R.id.image);


        bank = (Bank) getIntent().getSerializableExtra(Bank.EXTRA_KEY);


        bankName.setText(bank.getName());
        phone.setText(bank.getPhone());
        email.setText(bank.getEmail());
        headOffice.setText(bank.getHeadOffice());
        openingHour.setText(bank.getOpeningHours());
        Picasso.with(this)
                .load(bank.getUrl())
                .into(image);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ScrollCompatibleMapFragment mapFragment = (ScrollCompatibleMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.setTouchListener(new ScrollCompatibleMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                container.requestDisallowInterceptTouchEvent(true);
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                lat = 27.6884306;
                lon = 85.3394647;
                latLng = new LatLng(lat, lon);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                setupClustering(bank.getAtmList(),googleMap);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void call(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        Log.e("TAG", "call: " + bank.getPhone());
        callIntent.setData(Uri.parse("tel:" + bank.getPhone()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public void email(View view) {
        String to = bank.getEmail();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void setupClustering(List<Atm> metadataList,GoogleMap googleMap) {

        // add preconditions
        if ((metadataList == null || metadataList.isEmpty()) && googleMap == null) return;

        // Initialize the manager with the context and the map.
        ClusterManager<Atm> clusterManager = new ClusterManager<>(this, googleMap);
        clusterManager.addItems(metadataList);
        clusterManager.getClusterMarkerCollection().setOnInfoWindowClickListener(this);
        clusterManager.setRenderer(new DefaultClusterRenderer<Atm>(this,
                googleMap, clusterManager) {

            @Override
            protected void onBeforeClusterItemRendered(Atm item, MarkerOptions markerOptions) {
                super.onBeforeClusterItemRendered(item, markerOptions);
                // add in code to change marker bitmap color, something like this
                try {
                    markerOptions.icon(item.getStatus().equalsIgnoreCase("true") ?
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) :
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onClusterItemRendered(Atm clusterItem, Marker marker) {
                super.onClusterItemRendered(clusterItem, marker);

            }
        });

        googleMap.setOnCameraIdleListener(clusterManager);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}

