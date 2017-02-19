package com.example.root.atmdata.ui;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.example.root.atmdata.R;
import com.example.root.atmdata.RecyclerAdapter;
import com.example.root.atmdata.base.BaseFragment;
import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Use {@link android.support.v7.widget.RecyclerView} to list out {@link Bank}
 */
public class BankListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Bank> bankList;

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

    /**
     * todo add item decoration for recycler view
     * <p>
     * use {@link DividerItemDecoration} to get a small line between items
     * for spacing extend {@link ItemDecoration}
     * and override it's {@link ItemDecoration#getItemOffsets(Rect, View, RecyclerView, RecyclerView.State)}
     * to add spacing between each content.
     * example:
     *
     * recyclerView.addItemDecoration(new ItemDecoration() {
     *      @Override
     *      public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
     *          super.getItemOffsets(outRect, view, parent, state);
     *          // rather than using direct values like 10, 11, etc. or 0 in the given example below,
     *          // use {@link Resources#getDimensionPixelOffset(int)}
     *
     *              outRect.left = 0;
     *              outRect.right = 0;
     *              outRect.top = 0 / 2;
     *              outRect.bottom = 0 / 2;
     *              // if is first position, adjust spacing, if necessary
     *              if (parent.getChildAdapterPosition(view) == 0) {
     *                   outRect.top = 0;
     *               }
     *           }
     *     });
     *
     * you can add each decoration via {@link RecyclerView#addItemDecoration(ItemDecoration)}
     * <p>
     * also consider adding in a search to recycler view to make it easier discovering banks
     * You can go <a href="https://goo.gl/Re9HUp"> here </a> for reference
     */
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize recycler view
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(bankList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // show bank list in recycler view
        adapter.setBankList(bankList);
    }
}
