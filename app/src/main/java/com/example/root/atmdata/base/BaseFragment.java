package com.example.root.atmdata.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.atmdata.util.BankListener;
import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Created by root on 2/11/17.
 */

public abstract class BaseFragment extends Fragment implements BankListener {

    List<Bank> bankList;

    public abstract int layout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout(), container, false);
    }

    @Override
    public void onBankListUpdate(List<Bank> bankList) {
        this.bankList = bankList;
        refreshData(bankList);
    }

    public void refreshData(List<Bank> bankList) {

    }
}
