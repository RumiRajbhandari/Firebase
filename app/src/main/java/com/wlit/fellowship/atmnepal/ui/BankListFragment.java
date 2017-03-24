package com.wlit.fellowship.atmnepal.ui;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wlit.fellowship.atmnepal.R;
import com.wlit.fellowship.atmnepal.RecyclerAdapter;
import com.wlit.fellowship.atmnepal.base.BaseFragment;
import com.wlit.fellowship.atmnepal.databinding.BankListBinding;
import com.wlit.fellowship.atmnepal.helper.SimplerItemTouchHelperCallback;
import com.wlit.fellowship.atmnepal.model.Bank;
import com.wlit.fellowship.atmnepal.utilities.OnBankListChangedListener;
import com.wlit.fellowship.atmnepal.utilities.OnStartDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Use {@link android.support.v7.widget.RecyclerView} to list out {@link Bank}
 */
public class BankListFragment extends BaseFragment implements OnBankListChangedListener, OnStartDragListener {

    private RecyclerAdapter adapter;
    private EditText search;
    private List<Bank> bankList;
    private ItemTouchHelper itemTouchHelper;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";

    List<Bank> customBankListOrder = new ArrayList<Bank>();

    //get the JSON array of the ordered of sorted customers

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
        bankListFragment.bankList = new ArrayList<>(bankList);
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

        search = (EditText) getView().findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if text is empty, revert back to old list
                filter(s.toString());
            }
        });


        sharedPreferences = this.getContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        // you can also sort bank before setting it to the adapter
        bankList = sortBankListViaName(bankList);
        adapter = new RecyclerAdapter(bankList, getContext(), this, this);
        adapter.setBankList(bankList);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimplerItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

    }

    @Override
    public void refreshData(List<Bank> bankList) {
        // show bank list in recycler view
        // you can also sort bank before setting it to the adapter
        customBankListOrder = sortBankListViaName(new ArrayList<Bank>(bankList));
        adapter.setBankList(customBankListOrder);
    }

    void filter(String text) {
        Log.e(TAG, "filter: " + text);
        List<Bank> temp = new ArrayList();
        for (Bank d : bankList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
                Log.e("TAG", "filter: " + temp.toString());
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    public List<Bank> sortBankListViaName(List<Bank> bankList) {
        String preferredBankListOrder = sharedPreferences.getString(PREFERENCE_FILE, "");

        if (!preferredBankListOrder.isEmpty()) {

            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<String> preferredOrderBankIdList = gson.fromJson(preferredBankListOrder, new TypeToken<List<String>>() {
            }.getType());

            //build sorted list
            if (preferredOrderBankIdList != null && preferredOrderBankIdList.size() > 0) {
                for (String name : preferredOrderBankIdList) {
                    for (Bank customer : bankList) {
                        if (customer.getName().equals(name)) {
                            customBankListOrder.add(customer);
                            bankList.remove(customer);
                            break;
                        }
                    }
                }
            }
            if (bankList.size() > 0) {
                customBankListOrder.addAll(bankList);
            }
            return customBankListOrder;


        } else {
            Collections.sort(bankList, new Comparator<Bank>() {
                @Override
                public int compare(Bank o1, Bank o2) {
                    // sorting via name
                    // we can basically sort via so many other params. like locations near to you
                    // we can also add headers and categorizations to our list
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return bankList;
        }

        // you can also sort bank before setting it to the adapter

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onBankListOrderChanged(List<Bank> banks) {
        List<String> preferredBankListOrder = new ArrayList<String>();

        for (Bank bank : banks) {
            preferredBankListOrder.add(bank.getName());
        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        String jsonListOfSortedBankNames = gson.toJson(preferredBankListOrder);

        //save to SharedPreference
        editor.putString(PREFERENCE_FILE, jsonListOfSortedBankNames).commit();
        editor.commit();
    }

    @Override
    public void onLocationUpdate(LatLng latLng) {

    }
}
