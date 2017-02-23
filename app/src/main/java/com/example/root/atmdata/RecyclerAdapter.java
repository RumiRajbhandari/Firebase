package com.example.root.atmdata;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.atmdata.databinding.BankItemBinding;
import com.example.root.atmdata.databinding.BankListBinding;

import com.example.root.atmdata.model.Bank;

import java.util.List;


/**
 * Created by root on 2/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<Bank> bankList;
    private Context context;

    public RecyclerAdapter(List<Bank> bankList, Context context) {
        this.context = context;
        this.bankList = bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BankItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.single_row, parent, false);
        return new RecyclerViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.getBinding().setBank(bankList.get(position));
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // not BankListBinding, it's BankItemBinding
//        BankListBinding binding;
        private BankItemBinding binding;

        public RecyclerViewHolder(BankItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.executePendingBindings();
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AtmDetails.class);
                    intent.putExtra(Bank.EXTRA_KEY, bankList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
/*
            container = (RelativeLayout) view.findViewById(R.id.row);
            bankName = (TextView) view.findViewById(R.id.name);
            bankPhone = (TextView) view.findViewById(R.id.phone);
            bankImage = (ImageView) view.findViewById(R.id.imageView);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AtmDetails.class);
                    intent.putExtra(Bank.EXTRA_KEY, bankList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });*/

        }

        public BankItemBinding getBinding() {
            return binding;
        }
    }
}
