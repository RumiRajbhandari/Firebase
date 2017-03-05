package com.example.root.atmdata.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.atmdata.AtmDetails;
import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
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

        // todo move location request to MainActivity
        // request and ask for permission first
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        // if (location != null) {
        // Getting latitude of the current location
        //  double latitude = location.getLatitude();

        // Getting longitude of the current location
        // double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        // LatLng latLng = new LatLng(latitude, longitude);

//        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getContext())));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                BankAtmMarkerMetadata metadata = bankMap.get(marker.getId());
                // start dialog here
                // dialog should have two options,
                // 1) view bank details
//                if (bankMap.get(marker.getId()) != null) {
//                    Intent intent = new Intent(getActivity(), AtmDetails.class);
//                    intent.putExtra("bank", bankMap.get(marker.getId()).bank);
//                    startActivity(intent);
//                }
                // 2) change atm status
                // todo add atm status change here

            }
        });
        plotAtmList(bankList);
    }

    @Override
    public void onLocationUpdate(LatLng latLng) {

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
                            options.icon(atm.getStatus() ?
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
        Button button;

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
            markerView.setLayoutParams(new ViewGroup.LayoutParams(500, 500));

            windowContainer = (ViewGroup) markerView.findViewById(R.id.window_container);
            status = (TextView) markerView.findViewById(R.id.status);
            atmName = (TextView) markerView.findViewById(R.id.atm_name);

            Atm atm = bankMap.get(marker.getId()).atm;
            Bank bank = bankMap.get(marker.getId()).bank;
            status.setText("Status: " + (atm.getStatus() ? "Open" : "Close"));
            atmName.setText(bank.getName() + " ATM");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 3/1/17 implement atm status change here
                    Toast.makeText(getContext(), "This is toast", Toast.LENGTH_LONG).show();
                }
            });
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