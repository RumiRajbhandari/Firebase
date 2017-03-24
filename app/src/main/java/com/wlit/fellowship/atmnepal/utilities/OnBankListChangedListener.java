package com.wlit.fellowship.atmnepal.utilities;

import com.wlit.fellowship.atmnepal.model.Bank;

import java.util.List;

/**
 * Created by root on 2/24/17.
 */

public interface OnBankListChangedListener {
    void onBankListOrderChanged(List<Bank> banks);
}
