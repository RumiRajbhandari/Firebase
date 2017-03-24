package com.wlit.fellowship.atmnepal;

import android.support.v4.app.Fragment;

import com.wlit.fellowship.atmnepal.base.BaseFragment;
import com.wlit.fellowship.atmnepal.model.Bank;
import com.google.android.gms.maps.model.LatLng;

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
