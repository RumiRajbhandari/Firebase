package com.example.root.atmdata.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.root.atmdata.AtmDetails;
import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Plot {@link Atm} into map
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private List<Bank> bankList;
    private List<Marker> markerList;
    private GoogleMap googleMap;
    private HashMap<String, Bank> bankMap;

    public static MapFragment newInstance(List<Bank> bankList){
        MapFragment mapFragment=new MapFragment();
        mapFragment.bankList=bankList;
        mapFragment.markerList = new ArrayList<>();
        mapFragment.bankMap = new HashMap<>();
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
        SupportMapFragment fragment=new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.map_container,fragment).commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        plotAtmList(bankList);
    }

    private static final String TAG = "MapFragment";

    void plotAtmList(List<Bank> bankList){
        Log.d(TAG, "plotAtmList() called with: bankList = [" + bankList + "]");
        // initial check
        if(googleMap != null && !bankList.isEmpty()) {
            // remove markers if there

            for (final Bank bank : bankList) {
                if(bank.getAtmList() != null && !bank.getAtmList().isEmpty())
                for (Atm atm : bank.getAtmList()) {
                    if(atm.getLatitude() != Double.MIN_NORMAL) {
                        MarkerOptions options = new MarkerOptions();
                        options.title(bank.getName() + " atm");
                        options.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        options.position(new LatLng(atm.getLatitude(), atm.getLongitude()));
                        Marker marker = googleMap.addMarker(options);
                        bankMap.put(marker.getId(), bank);
                        markerList.add(marker);

                        googleMap.setOnInfoWindowClickListener(this);
                    }
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(bankMap.get(marker.getId()) != null) {
            Intent intent = new Intent(getActivity(), AtmDetails.class);
            intent.putExtra("bank", bankMap.get(marker.getId()));
            startActivity(intent);
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
}