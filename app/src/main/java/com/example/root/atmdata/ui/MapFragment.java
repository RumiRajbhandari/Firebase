package com.example.root.atmdata.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Plot {@link Atm} into map
 */
public class MapFragment extends BaseFragment {
    private List<Bank> bankList;
    public static MapFragment newInstance(List<Bank> bankList){
        MapFragment mapFragment=new MapFragment();
        mapFragment.bankList=bankList;
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
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void refreshData(List<Bank> bankList) {
    // Remove all markers and plot new markers
    }
}