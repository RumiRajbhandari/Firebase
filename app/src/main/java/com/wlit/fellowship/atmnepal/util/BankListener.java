package com.wlit.fellowship.atmnepal.util;

import android.support.v4.app.Fragment;

import com.wlit.fellowship.atmnepal.model.Bank;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by root on 2/11/17.
 */

public interface BankListener {

    void onBankListUpdate(List<Bank> bankList);

    void onLocationUpdate(LatLng latLng);

    Fragment getFragment();
}
