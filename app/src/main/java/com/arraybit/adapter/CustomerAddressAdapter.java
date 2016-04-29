package com.arraybit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
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
    FragmentManager fragmentManager;

    public CustomerAddressAdapter(Context context, ArrayList<CustomerAddressTran> result, CustomerAddressListener objCustomerAddressListener, FragmentManager fragmentManager) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lstCustomerAddressTran = result;
        this.objCustomerAddressListener = objCustomerAddressListener;
        this.fragmentManager = fragmentManager;
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
            holder.txtDefault.setText(view.getResources().getString(R.string.yaDefault));
            //} else {
            //    holder.txtDefault.setVisibility(View.GONE);
            if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
                //holder.ivHomeOffice.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_drawable));
                holder.txtDefault.setText(holder.txtDefault.getText() + " (" + Globals.AddressType.Home.toString() + ")");
            } else {
                //holder.ivHomeOffice.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.office_drawable));
                holder.txtDefault.setText(holder.txtDefault.getText() + " (" + Globals.AddressType.Office.toString() + ")");
            }
        } else {
            if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
                //holder.ivHomeOffice.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_drawable));
                holder.txtDefault.setText(Globals.AddressType.Home.toString());
            } else {
                //holder.ivHomeOffice.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.office_drawable));
                holder.txtDefault.setText(Globals.AddressType.Office.toString());
            }
        }

        //holder.txtCustomerAddressTranId.setText(String.valueOf(objCustomerAddressTran.getCustomerAddressTranId()));
        holder.txtCustomer.setText(objCustomerAddressTran.getCustomerName());
        holder.txtAddress.setText(objCustomerAddressTran.getAddress());
        holder.txtArea.setText(objCustomerAddressTran.getArea());
        holder.txtCity.setText(objCustomerAddressTran.getCity());
        holder.txtZipCode.setText(" - " + objCustomerAddressTran.getZipCode());
        holder.txtState.setText(String.valueOf(objCustomerAddressTran.getState()));
        holder.txtPhone.setText(String.valueOf(objCustomerAddressTran.getMobileNum()));

//        holder.btnDelete.setId(objCustomerAddressTran.getCustomerAddressTranId());
//        holder.btnDelete.setTag(position);
    }

    @Override
    public int getItemCount() {
        return lstCustomerAddressTran.size();
    }

    public void CustomerAddressDataChanged(CustomerAddressTran objCustomerAddressTran, Integer position) {
        if (position != null) {
            lstCustomerAddressTran.get(position).setCustomerName(objCustomerAddressTran.getCustomerName());
            lstCustomerAddressTran.get(position).setMobileNum(objCustomerAddressTran.getMobileNum());
            lstCustomerAddressTran.get(position).setState(objCustomerAddressTran.getState());
            lstCustomerAddressTran.get(position).setCity(objCustomerAddressTran.getCity());
            lstCustomerAddressTran.get(position).setArea(objCustomerAddressTran.getArea());
            lstCustomerAddressTran.get(position).setAddress(objCustomerAddressTran.getAddress());
            lstCustomerAddressTran.get(position).setZipCode(objCustomerAddressTran.getZipCode());
            lstCustomerAddressTran.get(position).setAddressType(objCustomerAddressTran.getAddressType());
            lstCustomerAddressTran.get(position).setIsPrimary(objCustomerAddressTran.getIsPrimary());
        } else {
            lstCustomerAddressTran.add(0, objCustomerAddressTran);
        }
        notifyDataSetChanged();
    }

    public void DeleteCustomerAddress(int position) {
        lstCustomerAddressTran.remove(position);
        notifyItemRemoved(position);
    }

    public interface CustomerAddressListener {
        void DeleteClickListener(CustomerAddressTran objCustomerAddressTran, int position);

        void OnClickListener(CustomerAddressTran objCustomerAddressTran, int position);
    }

    public class CustomerAddressTranViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDefault, txtCustomerAddressTranId, txtCustomer, txtPhone, txtAddress, txtState, txtCity, txtArea, txtZipCode;
        CardView cvAddress;
        ImageView ivHomeOffice;
        ImageButton btnDelete, ibEditAddress;

        public CustomerAddressTranViewHolder(View view) {
            super(view);

            cvAddress = (CardView) view.findViewById(R.id.cvAddress);

            ivHomeOffice = (ImageView) view.findViewById(R.id.ivHomeOffice);
            txtCustomerAddressTranId = (TextView) view.findViewById(R.id.txtCustomerAddressTranId);
            txtCustomerAddressTranId.setVisibility(View.GONE);
            txtDefault = (TextView) view.findViewById(R.id.txtDefault);
            txtCustomer = (TextView) view.findViewById(R.id.txtCustomerName);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtAddress = (TextView) view.findViewById(R.id.txtCustomerAddress);
            txtState = (TextView) view.findViewById(R.id.txtState);
            txtCity = (TextView) view.findViewById(R.id.txtCity);
            txtArea = (TextView) view.findViewById(R.id.txtArea);
            txtZipCode = (TextView) view.findViewById(R.id.txtZipCode);

            btnDelete = (ImageButton) view.findViewById(R.id.ibDeleteAddress);
            ibEditAddress = (ImageButton) view.findViewById(R.id.ibEditAddress);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                            && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_your_address))) {
                        objCustomerAddressListener.DeleteClickListener(lstCustomerAddressTran.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

            cvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                            && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_your_address))) {
                        objCustomerAddressListener.OnClickListener(lstCustomerAddressTran.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

            ibEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                            && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_your_address))) {
                        objCustomerAddressListener.OnClickListener(lstCustomerAddressTran.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }
}

