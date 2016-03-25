package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class BusinessInfoAdapter extends RecyclerView.Adapter<BusinessInfoAdapter.BusinessInfoViewHolder> {

    View view;
    Context context;
    ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster;

    public BusinessInfoAdapter(Context context, ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster) {
        this.context = context;
        this.alBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster;
    }

    @Override
    public BusinessInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_business_info, parent, false);
        return new BusinessInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessInfoViewHolder holder, int position) {
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster.get(position);

        holder.txtBusinessQue.setText(objBusinessInfoQuestionMaster.getQuestion());

    }

    public void BusinessInfoDataChanged(ArrayList<BusinessInfoQuestionMaster> result) {
        alBusinessInfoQuestionMaster.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alBusinessInfoQuestionMaster.size();
    }

    class BusinessInfoViewHolder extends RecyclerView.ViewHolder {

        TextView txtBusinessQue;

        public BusinessInfoViewHolder(View itemView) {
            super(itemView);

            txtBusinessQue = (TextView) itemView.findViewById(R.id.txtBusinessQue);
        }
    }
}
