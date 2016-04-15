package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.arraybit.modal.OrderMaster;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderMasterViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    View view;
    ArrayList<OrderMaster> alOrderMaster;

    public OrderAdapter(Context context,ArrayList<OrderMaster> alOrderMaster) {
        this.context = context;
        this.alOrderMaster = alOrderMaster;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderMasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_order, parent, false);
        return new OrderMasterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderMasterViewHolder holder, int position) {
        OrderMaster objOrderMaster = alOrderMaster.get(position);

        holder.txtOrderMasterId.setText(objOrderMaster.getTotalAmount()+" "+objOrderMaster.getOrderNumber()+" "+objOrderMaster.getOrderMasterId());
    }

    public void OrderDataChanged(ArrayList<OrderMaster> result) {
        alOrderMaster.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alOrderMaster.size();
    }

    class OrderMasterViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderMasterId;

        public OrderMasterViewHolder(View itemView) {
            super(itemView);

            txtOrderMasterId = (TextView) itemView.findViewById(R.id.txtOrderMasterId);

        }
    }
}
