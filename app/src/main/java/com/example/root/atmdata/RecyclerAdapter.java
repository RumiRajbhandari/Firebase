package com.example.root.atmdata;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.atmdata.databinding.BankItemBinding;

import com.example.root.atmdata.helper.ItemTouchHelperAdapter;
import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.utilities.OnBankListChangedListener;
import com.example.root.atmdata.utilities.OnStartDragListener;

import java.util.Collections;
import java.util.List;


/**
 * Created by root on 2/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> implements ItemTouchHelperAdapter {

    private List<Bank> bankList;
    private Context context;
    private OnStartDragListener startDragListener;
    private OnBankListChangedListener bankListChangedListner;

    public RecyclerAdapter(List<Bank> bankList, Context context, OnStartDragListener dragListener, OnBankListChangedListener bankListChangedListner) {
        this.context = context;
        this.bankList = bankList;
        this.startDragListener = dragListener;
        this.bankListChangedListner = bankListChangedListner;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
        notifyDataSetChanged();
    }

    public void updateList(List<Bank> bankList) {
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
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.getBinding().setBank(bankList.get(position));
        holder.getBinding().getRoot().findViewById(R.id.image).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(holder);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(bankList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(bankList, i, i - 1);
            }
        }
        bankListChangedListner.onBankListOrderChanged(bankList);
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // not BankListBinding, it's BankItemBinding
//        BankListBinding binding;
        ImageView handleView;
        private BankItemBinding binding;

        public RecyclerViewHolder(final BankItemBinding binding) {
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
