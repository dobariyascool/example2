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
import com.arraybit.modal.CustomerAddressTran;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class AddressSelectorAdapter extends RecyclerView.Adapter<AddressSelectorAdapter.AddressSelectorViewHolder>{

    Context context;
    LayoutInflater inflater;
    View view;
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    int position;
    AddressSelectorListener objAddressSelectorListener;
    ArrayList<BusinessMaster> alBusinessMaster;

    public AddressSelectorAdapter(Context context, ArrayList<CustomerAddressTran> result,ArrayList<BusinessMaster> alBusinessMaster,AddressSelectorListener objAddressSelectorListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alCustomerAddressTran = result;
        this.objAddressSelectorListener = objAddressSelectorListener;
        this.alBusinessMaster = alBusinessMaster;
    }

    @Override
    public AddressSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_address_selector, parent, false);
        return new AddressSelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressSelectorViewHolder holder, int position) {
        if(alCustomerAddressTran!=null) {
            CustomerAddressTran objCustomerAddressTran = alCustomerAddressTran.get(position);
            holder.businessAddressLayout.setVisibility(View.GONE);
            holder.customerAddressLayout.setVisibility(View.VISIBLE);
            holder.txtAddress.setText(objCustomerAddressTran.getAddress());
            if (objCustomerAddressTran.getMobileNum() == null || objCustomerAddressTran.getMobileNum().equals("")) {
                holder.ivCall.setVisibility(View.GONE);
                holder.txtPhone.setVisibility(View.GONE);
            } else {
                holder.ivCall.setVisibility(View.VISIBLE);
                holder.txtPhone.setVisibility(View.VISIBLE);
                holder.txtPhone.setText(objCustomerAddressTran.getMobileNum());
            }
            if (position == alCustomerAddressTran.size() - 1) {
                holder.txtHeader.setVisibility(View.VISIBLE);
            }
        }else{
            holder.businessAddressLayout.setVisibility(View.VISIBLE);
            holder.customerAddressLayout.setVisibility(View.GONE);
            BusinessMaster objBusinessMaster = alBusinessMaster.get(position);
            holder.txtBusinessName.setText(objBusinessMaster.getBusinessName());
            holder.txtBusinessAddress.setText(objBusinessMaster.getAddress());
            holder.txtBusinessPhone.setText(objBusinessMaster.getPhone1());
        }
    }

    @Override
    public int getItemCount() {
        if(alCustomerAddressTran!=null){
            return alCustomerAddressTran.size();
        }else{
            return alBusinessMaster.size();
        }
    }

    public interface AddressSelectorListener {
        void AddressSelectorOnClickListener(CustomerAddressTran objCustomerAddressTran,BusinessMaster objBusinessMaster);
    }


    class AddressSelectorViewHolder extends RecyclerView.ViewHolder {

        TextView txtAddress,txtHeader,txtPhone,txtBusinessAddress,txtBusinessName,txtBusinessPhone;
        LinearLayout addressLayout,customerAddressLayout,businessAddressLayout;
        ImageView ivCall;

        public AddressSelectorViewHolder(View view) {
            super(view);
            addressLayout = (LinearLayout)view.findViewById(R.id.addressLayout);
            customerAddressLayout = (LinearLayout)view.findViewById(R.id.customerAddressLayout);
            businessAddressLayout = (LinearLayout)view.findViewById(R.id.businessAddressLayout);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtHeader = (TextView) view.findViewById(R.id.txtHeader);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtBusinessAddress = (TextView) view.findViewById(R.id.txtBusinessAddress);
            txtBusinessName = (TextView) view.findViewById(R.id.txtBusinessName);
            txtBusinessPhone = (TextView) view.findViewById(R.id.txtBusinessPhone);
            ivCall = (ImageView) view.findViewById(R.id.ivCall);

            addressLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(alCustomerAddressTran!=null) {
                        objAddressSelectorListener.AddressSelectorOnClickListener(alCustomerAddressTran.get(getAdapterPosition()),null);
                    }else{
                        objAddressSelectorListener.AddressSelectorOnClickListener(null,alBusinessMaster.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
