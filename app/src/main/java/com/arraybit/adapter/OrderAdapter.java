package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.arraybit.modal.OrderMaster;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderMasterViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    View view;
    ArrayList<OrderMaster> alOrderMaster;

    public OrderAdapter(ArrayList<OrderMaster> alOrderMaster, Context context) {
        this.alOrderMaster = alOrderMaster;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderMasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_order, parent, false);
        return new OrderMasterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderMasterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class OrderMasterViewHolder extends RecyclerView.ViewHolder {

        public OrderMasterViewHolder(View itemView) {
            super(itemView);

        }
    }
}
