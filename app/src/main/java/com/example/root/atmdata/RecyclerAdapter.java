package com.example.root.atmdata;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.root.atmdata.model.Bank;
import com.example.root.atmdata.ui.BankListFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 2/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
     public  List<Bank> banks;
    Context context;
    public RecyclerAdapter(List<Bank> banks, Context context){
        this.context=context;
        this.banks=banks;

    }
    public void setBanks(List<Bank> bankList){
        banks=bankList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        Log.e(":TAG.......", "onBindViewHolder: "+banks.get(position).getUrl());
        holder.tx_name.setText(banks.get(position).getName());
        holder.tx_phone.setText(banks.get(position).getPhone());
        Picasso.with(context)
                .load(banks.get(position).getUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }
    public  class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout relativeLayout;
        TextView tx_name;
        TextView tx_phone;
        ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);
            relativeLayout=(RelativeLayout)view.findViewById(R.id.row);
            tx_name=(TextView)view.findViewById(R.id.name);
            tx_phone=(TextView)view.findViewById(R.id.phone);
            imageView=(ImageView)view.findViewById(R.id.imageView);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    banks.get(getAdapterPosition());
                    //TODO send to atm details
                    Intent intent = new Intent(context, AtmDetails.class);
                    intent.putExtra("bank", (Serializable)  banks.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}
