package com.example.root.atmdata.utilities;

import com.example.root.atmdata.model.Bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/24/17.
 */

public interface OnBankListChangedListner {
    void onNoteListChanged(List<Bank> banks);
}
