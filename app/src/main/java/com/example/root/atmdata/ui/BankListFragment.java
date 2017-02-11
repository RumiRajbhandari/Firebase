package com.example.root.atmdata.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.root.atmdata.R;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Use {@link android.support.v7.widget.RecyclerView} to list out {@link Bank}
 */
public class BankListFragment extends BaseFragment {

    @Override
    public int layout() {
        return R.layout.fragment_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize recycler view
    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // show bank list in recycler view
    }
}
