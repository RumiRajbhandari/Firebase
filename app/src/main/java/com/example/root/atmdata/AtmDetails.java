package com.example.root.atmdata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.util.MyConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class AtmDetails extends BaseActivity {

    Bank bank;
    TextView bankName, phone, email, openingHour, headOffice;
    ImageView image;

    @Override
    public int layout() {
        return R.layout.atm_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bankName = (TextView) findViewById(R.id.bank_name);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        openingHour = (TextView) findViewById(R.id.opening_hour);
        headOffice = (TextView) findViewById(R.id.head_office);
        image = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();
        bank = (Bank) i.getSerializableExtra("bank");

        bankName.setText(bank.getName());
        phone.setText(bank.getPhone());
        email.setText(bank.getEmail());
        headOffice.setText(bank.getHead_office());
        openingHour.setText(bank.getOpeing_hours());
        Picasso.with(this)
                .load(bank.getUrl())
                .into(image);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (Atm atm : bank.getAtmlist()) {
                    MarkerOptions options = new MarkerOptions();
                    options.title(bank.getName() + " atm");
                    options.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    options.position(new LatLng(atm.getLat(), atm.getLon()));
                    googleMap.addMarker(options);
                }
            }
        });
    }

    public void call(View view){

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9849829387"));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
    }

    public void edit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Please set the status")
                .setTitle("Status update");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

// 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
        dialog.show();
    }


}

