package com.example.root.atmdata.util;

import android.support.v4.app.Fragment;

import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Created by root on 2/11/17.
 */

public interface BankListener {

    void onBankListUpdate(List<Bank> bankList);
    Fragment getFragment();
}
