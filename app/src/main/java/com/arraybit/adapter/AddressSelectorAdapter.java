package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
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

    public AddressSelectorAdapter(Context context, ArrayList<CustomerAddressTran> result,AddressSelectorListener objAddressSelectorListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alCustomerAddressTran = result;
        this.objAddressSelectorListener = objAddressSelectorListener;
    }

    @Override
    public AddressSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_address_selector, parent, false);
        return new AddressSelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressSelectorViewHolder holder, int position) {
        CustomerAddressTran objCustomerAddressTran = alCustomerAddressTran.get(position);
        holder.txtAddress.setText(objCustomerAddressTran.getAddress());
        if(objCustomerAddressTran.getMobileNum()==null || objCustomerAddressTran.getMobileNum().equals("")){
            holder.ivCall.setVisibility(View.GONE);
            holder.txtPhone.setVisibility(View.GONE);
        }else{
            holder.ivCall.setVisibility(View.VISIBLE);
            holder.txtPhone.setVisibility(View.VISIBLE);
            holder.txtPhone.setText(objCustomerAddressTran.getMobileNum());
        }
        if(position==alCustomerAddressTran.size()-1){
            holder.txtHeader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return alCustomerAddressTran.size();
    }

    public interface AddressSelectorListener {
        void AddressSelectorOnClickListener(CustomerAddressTran objCustomerAddressTran);
    }


    class AddressSelectorViewHolder extends RecyclerView.ViewHolder {

        TextView txtAddress,txtHeader,txtPhone;
        LinearLayout addressLayout;
        ImageView ivCall;

        public AddressSelectorViewHolder(View view) {
            super(view);
            addressLayout = (LinearLayout)view.findViewById(R.id.addressLayout);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtHeader = (TextView) view.findViewById(R.id.txtHeader);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            ivCall = (ImageView) view.findViewById(R.id.ivCall);

            addressLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objAddressSelectorListener.AddressSelectorOnClickListener(alCustomerAddressTran.get(getAdapterPosition()));
                }
            });
        }
    }
}
