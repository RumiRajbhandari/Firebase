package com.example.root.atmdata.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.atmdata.AtmDetails;
import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Plot {@link Atm} into map
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private List<Bank> bankList;
    private List<Marker> markerList;
    private GoogleMap googleMap;
    private HashMap<String, BankAtmMarkerMetadata> bankMap;
    private GoogleApiClient client;
    private LatLng latLng;
    Switch switchCompact;
    // only use one shared preference
    private SharedPreferences sharedPreferences;
    /**
     * todo if there's time implement marker clustering -> https://developers.google.com/maps/documentation/android-api/utility/marker-clustering
     */


    public static MapFragment newInstance(List<Bank> bankList, LatLng latLng) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.bankList = bankList;
        mapFragment.markerList = new ArrayList<>();
        mapFragment.bankMap = new HashMap<>();
        mapFragment.latLng = latLng;
        return mapFragment;
    }

    @Override
    public int layout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize map
        SupportMapFragment fragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.map_container, fragment).commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getContext())));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onInfoWindowClick(final Marker marker) {
                final BankAtmMarkerMetadata metadata = bankMap.get(marker.getId());
                // start dialog here
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Please chose an action");
                builder.setPositiveButton("ATM Details", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dialog should have two options,
//                 1) view bank details
                        if (bankMap.get(marker.getId()) != null) {
                            Intent intent = new Intent(getActivity(), AtmDetails.class);
                            intent.putExtra("bank", metadata.bank);
                            startActivity(intent);
                        }


                    }
                });
                builder.setNegativeButton("Edit ATM status", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 2) change atm status
                        // todo add atm status change here
                        Toast.makeText(getContext(), "Rumi", Toast.LENGTH_SHORT).show();
                        sharedPreferences = getContext().getSharedPreferences("AtmData", Context.MODE_PRIVATE);
                        final String name = sharedPreferences.getString("name", "");
                        Log.e("TAg", "edit:.........." + name);
                        if (name.isEmpty()) {
                            final EditText editText = new EditText(getContext());
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            // 2. Chain together various setter methods to set the dialog characteristics
                            builder.setTitle("Please Enter your name")
                                    .setView(editText);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String userName = editText.getText().toString();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name", userName);
                                    editor.apply();

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
                        // rather than initializing another variable, use else
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            // 2. Chain together various setter methods to set the dialog characteristics
                            LayoutInflater factory = LayoutInflater.from(getContext());
                            final View textEntryView = factory.inflate(R.layout.edit_atm, null);

                            builder.setTitle("Status update")
                                    .setView(textEntryView);
                            switchCompact = (Switch) textEntryView.findViewById(R.id.atm_status_switch);
                            switchCompact.setChecked(true);
                            switchCompact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    Log.e(TAG, "onCheckedChanged: " + isChecked);

                                }
                            });

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.e(TAG, "onClick: ");
                                    if (bankMap.get(marker.getId()) != null) {
                                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                        String date = df.format(Calendar.getInstance().getTime());
                                        Firebase ref = new Firebase(bankMap.get(marker.getId()).atm.getReference());
                                        ref.child("status").setValue("" + switchCompact.isChecked());
                                        ref.child("status_update_time").setValue(date);
                                        ref.child("updated_by").setValue(name);
                                    }
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
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        plotAtmList(bankList);
    }

    @Override
    public void onLocationUpdate(LatLng latLng) {
        // get user location here
        // add camera changes here if necessary
    }

    void plotAtmList(List<Bank> bankList) {
        Log.d(TAG, "plotAtmList() called with: bankList = [" + bankList + "]");
        // initial check
        if (googleMap != null && !bankList.isEmpty()) {
            // remove markers if there
            for (final Bank bank : bankList) {
                if (bank.getAtmList() != null && !bank.getAtmList().isEmpty())
                    for (Atm atm : bank.getAtmList()) {
                        if (atm.getLatitude() != Double.MIN_NORMAL) {
                            MarkerOptions options = new MarkerOptions();
                            // if atm is open AZURE
                            if (atm.getStatus() != null) {
                                options.icon((atm.getStatus().equalsIgnoreCase("true")) ?
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) :
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                options.position(new LatLng(atm.getLatitude(), atm.getLongitude()));
                                Marker marker = googleMap.addMarker(options);
                                BankAtmMarkerMetadata metadata = new BankAtmMarkerMetadata();
                                metadata.bank = bank;
                                metadata.atm = atm;
                                bankMap.put(marker.getId(), metadata);
                                markerList.add(marker);
                            }
                            // do not add zoom here. first get user location and then only request camera change
//                           googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                        }
                    }
            }
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // Remove all markers and plot new markers
        plotAtmList(bankList);
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private LayoutInflater inflater;

        View markerView;
        ViewGroup windowContainer;
        TextView status;
        TextView atmName;
        TextView updatedTime;
        TextView button;

        CustomInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        // window frame for marker popup
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        // window content for marker popup
        @Override
        public View getInfoContents(final Marker marker) {

            markerView = inflater.inflate(R.layout.custominfowindow, null, false);
            markerView.setLayoutParams(new ViewGroup.LayoutParams(350, 350));

            windowContainer = (ViewGroup) markerView.findViewById(R.id.window_container);
            status = (TextView) markerView.findViewById(R.id.status);
            atmName = (TextView) markerView.findViewById(R.id.atm_name);
            button = (TextView) markerView.findViewById(R.id.edit);
            updatedTime = (TextView) markerView.findViewById(R.id.updated_time);

            Atm atm = bankMap.get(marker.getId()).atm;
            Bank bank = bankMap.get(marker.getId()).bank;
            status.setText("Status: " + (atm.getStatus().equalsIgnoreCase("true") ? "Open" : "Close"));
            atmName.setText(bank.getName() + " ATM");
            //TODO: show updated time;
//            updatedTime.setText("Updated Time: "+atm.get);
            return markerView;
        }
    }

    /**
     * used to store bank and atm meta data for respective marker
     */
    private class BankAtmMarkerMetadata {
        Bank bank;
        Atm atm;
    }
}