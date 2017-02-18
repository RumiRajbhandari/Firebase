package com.example.root.atmdata;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.atmdata.model.Bank;

import java.util.List;

/**
 * Created by root on 2/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
     public  List<Bank> banks;
    public RecyclerAdapter(List<Bank> banks){
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
        Log.e(":TAG.......", "onBindViewHolder: "+banks.get(position).getName() );
        holder.tx_name.setText(banks.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return banks.size();
    }
    public  class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout;
        TextView tx_name;

        public RecyclerViewHolder(View view){
            super(view);
            linearLayout=(LinearLayout)view.findViewById(R.id.container);
            tx_name=(TextView)view.findViewById(R.id.name);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    banks.get(getAdapterPosition());
                    //TODO send to atm details
                }
            });

        }
    }
}
