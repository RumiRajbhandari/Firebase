package com.example.root.atmdata.ui;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.atmdata.R;
import com.example.root.atmdata.RecyclerAdapter;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.databinding.BankListBinding;
import com.example.root.atmdata.model.Bank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Use {@link android.support.v7.widget.RecyclerView} to list out {@link Bank}
 */
public class BankListFragment extends BaseFragment {

    private RecyclerAdapter adapter;
    private List<Bank> bankList;
    /**
     * one of the reasons we could not run this was because of our implementation in {@link BaseFragment}
     * Because the {@link BaseFragment} takes in {@link #layout()} and initializes the layout itself,
     * we cannot initialize our binding, so here we must override our {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * <p>
     * <p>
     * and BTW :) I loved that you tried Data Binding. Yaaaay!
     */
    private BankListBinding binding;

    public static BankListFragment newInstance(List<Bank> bankList) {
        BankListFragment bankListFragment = new BankListFragment();
        bankListFragment.bankList = bankList;
        return bankListFragment;
    }

    @Override
    public int layout() {
        return R.layout.fragment_list;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layout(), container, false);
        return binding.getRoot();
    }

    /**
     * todo add item decoration for recycler view
     * <p>
     * use {@link DividerItemDecoration} to get a small line between items
     * for spacing extend {@link ItemDecoration}
     * and override it's {@link ItemDecoration#getItemOffsets(Rect, View, RecyclerView, RecyclerView.State)}
     * to add spacing between each content.
     * example:
     * <p>
     * recyclerView.addItemDecoration(new ItemDecoration() {
     *
     * @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
     * super.getItemOffsets(outRect, view, parent, state);
     * // rather than using direct values like 10, 11, etc. or 0 in the given example below,
     * // use {@link Resources#getDimensionPixelOffset(int)}
     * <p>
     * outRect.left = 0;
     * outRect.right = 0;
     * outRect.top = 0 / 2;
     * outRect.bottom = 0 / 2;
     * // if is first position, adjust spacing, if necessary
     * if (parent.getChildAdapterPosition(view) == 0) {
     * outRect.top = 0;
     * }
     * }
     * });
     * <p>
     * you can add each decoration via {@link RecyclerView#addItemDecoration(ItemDecoration)}
     * <p>
     * also consider adding in a search to recycler view to make it easier discovering banks
     * You can go <a href="https://goo.gl/Re9HUp"> here </a> for reference
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // you can also sort bank before setting it to the adapter
        sortBankListViaName(bankList);
        adapter = new RecyclerAdapter(bankList, getContext());
        adapter.setBankList(bankList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
//        binding.setBankList(bankList);
//        binding.setAdapter(adapter);
//        // initialization code can be found in BindingUtil
    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // show bank list in recycler view
        // you can also sort bank before setting it to the adapter
        sortBankListViaName(bankList);
        adapter.setBankList(bankList);
    }

    public void sortBankListViaName(List<Bank> bankList) {
        // you can also sort bank before setting it to the adapter
        Collections.sort(bankList, new Comparator<Bank>() {
            @Override
            public int compare(Bank o1, Bank o2) {
                // sorting via name
                // we can basically sort via so many other params. like locations near to you
                // we can also add headers and categorizations to our list
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
}
