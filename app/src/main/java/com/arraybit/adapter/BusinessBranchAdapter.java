package com.arraybit.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.modal.BusinessMaster;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class BusinessBranchAdapter extends RecyclerView.Adapter<BusinessBranchAdapter.BusinessBranchSelectorViewHolder>{

    Context context;
    LayoutInflater inflater;
    View view;
    int position;
    BranchSelectorListener objBranchSelectorListener;
    ArrayList<BusinessMaster> alBusinessMaster;

    public BusinessBranchAdapter(Context context,ArrayList<BusinessMaster> alBusinessMaster,BranchSelectorListener objBranchSelectorListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.objBranchSelectorListener = objBranchSelectorListener;
        this.alBusinessMaster = alBusinessMaster;
    }

    @Override
    public BusinessBranchSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_business_branch, parent, false);
        return new BusinessBranchSelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessBranchSelectorViewHolder holder, int position) {
        BusinessMaster objBusinessMaster = alBusinessMaster.get(position);
        holder.txtBusinessName.setText(objBusinessMaster.getBusinessName());
        holder.txtBusinessAddress.setText(objBusinessMaster.getAddress());
        holder.txtBusinessPhone.setText(objBusinessMaster.getPhone1());
    }

    @Override
    public int getItemCount() {
        return alBusinessMaster.size();
    }

    public interface BranchSelectorListener {
        void BranchSelectorOnClickListener(BusinessMaster objBusinessMaster);
    }

    class BusinessBranchSelectorViewHolder extends RecyclerView.ViewHolder {

        TextView txtBusinessAddress,txtBusinessName,txtBusinessPhone;
        ImageView ivCall;
        LinearLayout branchLayout;

        public BusinessBranchSelectorViewHolder(View view) {
            super(view);
            branchLayout = (LinearLayout)view.findViewById(R.id.branchLayout);
            txtBusinessAddress = (TextView) view.findViewById(R.id.txtBusinessAddress);
            txtBusinessName = (TextView) view.findViewById(R.id.txtBusinessName);
            txtBusinessPhone = (TextView) view.findViewById(R.id.txtBusinessPhone);
            ivCall = (ImageView) view.findViewById(R.id.ivCall);

            branchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objBranchSelectorListener.BranchSelectorOnClickListener(alBusinessMaster.get(getAdapterPosition()));
                }
            });
        }
    }
}
