package com.example.root.atmdata.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Atm;
import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Plot {@link Atm} into map
 */
public class MapFragment extends BaseFragment {

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
    public void onBankListUpdate(List<Bank> bankList) {
        super.onBankListUpdate(bankList);
        // collect atm list detail and plot it into map
    }
}
