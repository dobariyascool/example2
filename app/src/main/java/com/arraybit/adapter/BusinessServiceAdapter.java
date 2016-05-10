package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.abposw.R;
import com.arraybit.modal.BusinessServiceTran;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessServiceAdapter extends RecyclerView.Adapter<BusinessServiceAdapter.BusinessServiceTranViewHolder> {

    Context context;
    LayoutInflater inflater;
    View ConvertView;
    ArrayList<BusinessServiceTran> alBusinessServiceTran;

    public BusinessServiceAdapter(Context context, ArrayList<BusinessServiceTran> result) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alBusinessServiceTran = result;
    }

    @Override
    public BusinessServiceTranViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConvertView = inflater.inflate(R.layout.row_service, parent, false);
        return new BusinessServiceTranViewHolder(ConvertView);
    }

    @Override
    public void onBindViewHolder(BusinessServiceTranViewHolder holder, int position) {
        BusinessServiceTran objBusinessServiceTran = alBusinessServiceTran.get(position);
        holder.txtService.setText(String.valueOf(objBusinessServiceTran.getService()));
        if(objBusinessServiceTran.getXSImagePhysicalName()!=null){
            Picasso.with(context).load(objBusinessServiceTran.getXSImagePhysicalName()).into(holder.ivService);
        }
    }

    @Override
    public int getItemCount() {
        return alBusinessServiceTran.size();
    }

    class BusinessServiceTranViewHolder extends RecyclerView.ViewHolder {
        TextView txtService;
        ImageView ivService;

        public BusinessServiceTranViewHolder(View view) {
            super(view);

            txtService = (TextView) view.findViewById(R.id.txtService);
            ivService = (ImageView) view.findViewById(R.id.ivService);
        }

    }
}

