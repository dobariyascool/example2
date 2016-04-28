package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public int getItemCount() {
        return alCustomerAddressTran.size();
    }

    public interface AddressSelectorListener {
        void AddressSelectorOnClickListener(CustomerAddressTran objCustomerAddressTran);
    }


    class AddressSelectorViewHolder extends RecyclerView.ViewHolder {

        TextView txtAddress;
        CardView cvAddress;

        public AddressSelectorViewHolder(View view) {
            super(view);
            cvAddress = (CardView)view.findViewById(R.id.cvAddress);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);

            cvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objAddressSelectorListener.AddressSelectorOnClickListener(alCustomerAddressTran.get(getAdapterPosition()));
                }
            });
        }
    }
}
