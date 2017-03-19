package com.example.root.atmdata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.ui.ScrollCompatibleMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by root on 2/13/17.
 */
public class AtmDetails extends BaseActivity {

    private ScrollView container;
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

        container = (ScrollView) findViewById(R.id.container);
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

        // todo add map clustering here as well
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (Atm atm : bank.getAtmList()) {
                    MarkerOptions options = new MarkerOptions();
                    options.title(bank.getName() + " atm");
                    // todo add marker color according to atm status
                    options.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    options.position(new LatLng(atm.getLatitude(), atm.getLongitude()));
                    googleMap.addMarker(options);
                    lat = 27.6884306;
                    lon = 85.3394647;
                    latLng = new LatLng(lat, lon);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }
            }
        });
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
}

