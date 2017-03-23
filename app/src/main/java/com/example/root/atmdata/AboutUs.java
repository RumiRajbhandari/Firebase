package com.example.root.atmdata;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.root.atmdata.base.BaseActivity;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.ui.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 3/23/17.
 */

public class AboutUs extends BaseFragment {
    public static AboutUs newInstance() {
        AboutUs aboutUs = new AboutUs();

        return aboutUs;
    }

    @Override
    public int layout() {
        return R.layout.fragment_about_us;
    }

    @Override
    public void refreshData(List<Bank> bankList) {

    }

    @Override
    public void onLocationUpdate(LatLng latLng) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
