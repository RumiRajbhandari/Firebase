package com.example.root.atmdata.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.root.atmdata.R;
import com.example.root.atmdata.RecyclerAdapter;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Bank;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Use {@link android.support.v7.widget.RecyclerView} to list out {@link Bank}
 */
public class BankListFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    String bankString;


    @Override
    public int layout() {
        return R.layout.fragment_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize recycler view

        Intent i=getActivity().getIntent();
      bankString=i.getStringExtra("rumi");
//        Log.e(TAG, "Rajbhandari "+aa);
//        Log.e(TAG, "After fetching data" );
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        Bank[] bankList=gson.fromJson(bankString,Bank[].class);
        Log.e(TAG, "Raj........"+bankList[0].toString() );
        /*Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();*/
//        Log.e("TAG", "Bank list is "+bankList.get(1).toString());

        recyclerView=(RecyclerView) getActivity().findViewById(R.id.recycler_view);
        adapter=new RecyclerAdapter(bankList);

        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // show bank list in recycler view
    }
}
