package com.example.root.atmdata.util;

import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Created by root on 2/11/17.
 */

public interface BankListener {
    List<Bank> bankList = null;

    void onBankListUpdate(List<Bank> bankList);
}
