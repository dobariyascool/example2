package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.CustomerAddressTran;

import java.util.ArrayList;

public class CustomerAddressAdapter extends RecyclerView.Adapter<CustomerAddressAdapter.CustomerAddressTranViewHolder> {
    public ArrayList<CustomerAddressTran> lstCustomerAddressTran;
    Context context;
    LayoutInflater inflater;
    View view;
    CustomerAddressListener objCustomerAddressListener;
    int position;

    public CustomerAddressAdapter(Context context, ArrayList<CustomerAddressTran> result, CustomerAddressListener objCustomerAddressListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lstCustomerAddressTran = result;
        this.objCustomerAddressListener = objCustomerAddressListener;
    }

    @Override
    public CustomerAddressTranViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_address, parent, false);
        return new CustomerAddressTranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAddressTranViewHolder holder, int position) {
        CustomerAddressTran objCustomerAddressTran = lstCustomerAddressTran.get(position);

        if (objCustomerAddressTran.getIsPrimary()) {
            holder.txtDefault.setText("(" + view.getResources().getString(R.string.yaDefault) + ")");
        } else {
            holder.txtDefault.setVisibility(View.GONE);
        }

        if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
            holder.ivHome.setVisibility(View.VISIBLE);
            holder.ivOffice.setVisibility(View.GONE);
        } else {
            holder.ivHome.setVisibility(View.GONE);
            holder.ivOffice.setVisibility(View.VISIBLE);
        }

        //holder.txtCustomerAddressTranId.setText(String.valueOf(objCustomerAddressTran.getCustomerAddressTranId()));
        holder.txtCustomer.setText(objCustomerAddressTran.getCustomerName());
        holder.txtPhone.setText("(" + String.valueOf(objCustomerAddressTran.getMobileNum()) + ")");
        holder.txtAddress.setText(objCustomerAddressTran.getAddress());
        holder.txtCountry.setText(objCustomerAddressTran.getCountry());
        holder.txtState.setText(String.valueOf(objCustomerAddressTran.getState()));
        holder.txtZipCode.setText(objCustomerAddressTran.getZipCode());

        holder.btnDelete.setId(objCustomerAddressTran.getCustomerAddressTranId());
        holder.btnDelete.setTag(position);
    }

    @Override
    public int getItemCount() {
        return lstCustomerAddressTran.size();
    }

    public void EditCustomerAddress(ArrayList<CustomerAddressTran> alCustomerAddressTran) {
        lstCustomerAddressTran.addAll(alCustomerAddressTran);
        notifyDataSetChanged();
    }

    public void CustomerAddressDataChanged(CustomerAddressTran objCustomerAddressTran, int position) {
        if (position >= 0) {
            lstCustomerAddressTran.get(position).setState(objCustomerAddressTran.getState());
        } else {
            lstCustomerAddressTran.add(0, objCustomerAddressTran);
        }
        notifyDataSetChanged();
    }

    public void DeleteCustomerAddress(int position) {
        lstCustomerAddressTran.remove(position);
        notifyDataSetChanged();
    }

    public interface CustomerAddressListener {
        void DeleteClickListener(CustomerAddressTran objCustomerAddressTran, int position);

        void OnClickListener(CustomerAddressTran objCustomerAddressTran, int position);
    }

    class CustomerAddressTranViewHolder extends RecyclerView.ViewHolder {
        CardView cvAddress;
        ImageView ivHome, ivOffice;
        TextView txtDefault;
        TextView txtCustomerAddressTranId;
        TextView txtCustomer;
        TextView txtPhone;
        TextView txtAddress;
        TextView txtCountry;
        TextView txtState;
        TextView txtZipCode;

        ImageButton btnDelete;

        public CustomerAddressTranViewHolder(View view) {
            super(view);

            cvAddress = (CardView) view.findViewById(R.id.cvAddress);

            ivHome = (ImageView) view.findViewById(R.id.ivHome);
            ivOffice = (ImageView) view.findViewById(R.id.ivOffice);
            txtCustomerAddressTranId = (TextView) view.findViewById(R.id.txtCustomerAddressTranId);
            txtCustomerAddressTranId.setVisibility(View.GONE);
            txtDefault = (TextView) view.findViewById(R.id.txtDefault);
            txtCustomer = (TextView) view.findViewById(R.id.txtCustomerName);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtAddress = (TextView) view.findViewById(R.id.txtCustomerAddress);
            txtCountry = (TextView) view.findViewById(R.id.txtCountry);
            txtState = (TextView) view.findViewById(R.id.txtState);
            txtZipCode = (TextView) view.findViewById(R.id.txtZipCode);

            btnDelete = (ImageButton) view.findViewById(R.id.ibDeleteAddress);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objCustomerAddressListener.DeleteClickListener(lstCustomerAddressTran.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            cvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objCustomerAddressListener.OnClickListener(lstCustomerAddressTran.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}

