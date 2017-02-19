package com.example.root.atmdata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
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

    private Atm atm;
    private Bank bank;
    private TextView bankName, phone, email, openingHour, headOffice;
    private ImageView image;
    // only use one shared preference
    private SharedPreferences sharedPreferences;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (Atm atm : bank.getAtmList()) {
                    MarkerOptions options = new MarkerOptions();
                    options.title(bank.getName() + " atm");
                    options.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    options.position(new LatLng(atm.getLatitude(), atm.getLongitude()));
                    googleMap.addMarker(options);
                }
            }
        });
    }

    public void call(View view) {

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

    public void edit(View view) {

        sharedPreferences = getSharedPreferences("AtmData", Context.MODE_PRIVATE);
        String na = sharedPreferences.getString("name", "");
        Log.e("TAg", "edit:.........." + na);
        if (na.isEmpty()) {
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);


// 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Please set the status")
                    .setTitle("Please Enter your name")
                    .setView(editText);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String userName = editText.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", userName);
                    editor.commit();

                    Log.e("TAG", "onClick: " + userName);

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
        // sharedPreferences2=getSharedPreferences("AtmData",Context.MODE_PRIVATE);
        String na2 = sharedPreferences.getString("name", "");
        Log.e("TAG2", "edit: " + na2);
        if (!na2.isEmpty()) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);


// 2. Chain together various setter methods to set the dialog characteristics
            builder2.setMessage("Please set the status")
                    .setTitle("Status update");
            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

// 3. Get the AlertDialog from create()
            AlertDialog dialog = builder2.create();
            dialog.show();


        }
        Log.e("TAG", "edit:..... " + atm.toString());


    }
}

