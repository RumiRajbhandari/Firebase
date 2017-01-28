package com.example.root.atmdata;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.atmdata.databinding.BankListBinding;
import com.example.root.atmdata.databinding.ItemBankListBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class BankListActivity extends AppCompatActivity {

    BankListBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.test_listing_activity);

        binding.bankList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.bankList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                outRect.top = getResources().getDimensionPixelOffset(R.dimen.spacing_8) / 2;
                outRect.bottom = getResources().getDimensionPixelOffset(R.dimen.spacing_8) / 2;
                outRect.right = getResources().getDimensionPixelOffset(R.dimen.spacing_8);
                outRect.left = getResources().getDimensionPixelOffset(R.dimen.spacing_8);

                if (parent.getChildAdapterPosition(view) == 0 && parent.getChildAdapterPosition(view) == 1)
                    outRect.top = getResources().getDimensionPixelOffset(R.dimen.spacing_16);
            }
        });
        binding.bankList.setAdapter(new BankAdapter(getDummyBankList()));
    }

    public List<Bank> getDummyBankList() {
        List<Bank> bankList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Bank bank = new Bank(String.format("Bank %d", i), String.format("Address %d", i),
                    String.format("Email %d", i), android.R.drawable.ic_menu_report_image);
            bankList.add(bank);
        }
        return bankList;
    }

    public class BankAdapter extends RecyclerView.Adapter<BankViewHolder> {

        List<Bank> bankList;

        public BankAdapter(List<Bank> bankList) {
            this.bankList = bankList;
        }

        @Override
        public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemBankListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()),
                    R.layout.item_bank_list, parent, false);
            return new BankViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BankViewHolder holder, int position) {
            holder.getBinding().setBank(bankList.get(position));
            holder.getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return bankList.size();
        }
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {

        ItemBankListBinding binding;

        public BankViewHolder(ItemBankListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemBankListBinding getBinding() {
            return binding;
        }
    }
}
