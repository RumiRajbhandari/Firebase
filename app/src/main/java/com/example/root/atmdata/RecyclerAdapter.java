package com.example.root.atmdata;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.root.atmdata.model.Bank;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        holder.bankName.setText(bankList.get(position).getName());
        holder.bankPhone.setText(bankList.get(position).getPhone());
        Picasso.with(context)
                .load(bankList.get(position).getUrl())
                .into(holder.bankImage);
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView bankName;
        TextView bankPhone;
        ImageView bankImage;

        public RecyclerViewHolder(View view) {
            super(view);
            // TODO: 2/19/17 maybe use butter knife or data binding
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
            });

        }
    }
}
