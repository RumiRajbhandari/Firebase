package com.example.root.atmdata.util;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.root.atmdata.RecyclerAdapter;
import com.example.root.atmdata.model.Bank;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Helper class for our data binding
 */
public class BindingUtil {

    @BindingAdapter({"bank_list", "list_adapter"})
    public static void initializeRecyclerView(RecyclerView view, List<Bank> bankList, RecyclerAdapter adapter) {
        adapter.setBankList(bankList);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(adapter);
    }

    @BindingAdapter("image_url")
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }
}
