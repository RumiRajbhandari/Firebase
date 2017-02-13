package com.example.root.atmdata;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.atmdata.model.Bank;

/**
 * Created by root on 2/13/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    Bank[] banks;
    public RecyclerAdapter(Bank[] banks){
        this.banks=banks;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        Log.e(":TAG.......", "onBindViewHolder: "+banks[position].getName() );
        holder.tx_name.setText(banks[position].getName());

    }

    @Override
    public int getItemCount() {
        return banks.length;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView tx_name;

        public RecyclerViewHolder(View view){
            super(view);
            tx_name=(TextView)view.findViewById(R.id.name);

        }
    }
}
