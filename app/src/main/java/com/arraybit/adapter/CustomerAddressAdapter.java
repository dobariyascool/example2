package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.CustomerAddressTran;

import java.util.ArrayList;

public class CustomerAddressAdapter extends RecyclerView.Adapter<CustomerAddressAdapter.CustomerAddressTranViewHolder> {
    public ArrayList<CustomerAddressTran> lstCustomerAddressTran;
    public boolean isAddressAnimate = false;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomerAddressTranViewHolder holder, int position) {
        CustomerAddressTran objCustomerAddressTran = lstCustomerAddressTran.get(position);

        if (objCustomerAddressTran.getIsPrimary()) {
            holder.txtDefault.setText(view.getResources().getString(R.string.yaDefault));
            if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
                holder.txtDefault.setText(Globals.AddressType.Home.toString() + " (" + holder.txtDefault.getText() + ")");
            } else {
                holder.txtDefault.setText(Globals.AddressType.Office.toString() + " (" + holder.txtDefault.getText() + ")");
            }
        } else {
            if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
                holder.txtDefault.setText(Globals.AddressType.Home.toString());
            } else {
                holder.txtDefault.setText(Globals.AddressType.Office.toString());
            }
        }

        holder.txtCustomer.setText(objCustomerAddressTran.getCustomerName());
        holder.txtAddress.setText(objCustomerAddressTran.getAddress());
        if (objCustomerAddressTran.getArea() != null) {
            holder.txtArea.setVisibility(View.VISIBLE);
            holder.txtArea.setText(objCustomerAddressTran.getArea());
        } else {
            holder.txtArea.setVisibility(View.GONE);
        }

        holder.txtCity.setText(objCustomerAddressTran.getCity());
        if(objCustomerAddressTran.getCity()==null || objCustomerAddressTran.getCity().equals("")){
            holder.txtZipCode.setText(objCustomerAddressTran.getZipCode());
        }else{
            holder.txtZipCode.setText(context.getResources().getString(R.string.minus) + objCustomerAddressTran.getZipCode());
        }
        if(objCustomerAddressTran.getState()==null || objCustomerAddressTran.getState().equals("")){
            holder.txtState.setVisibility(View.GONE);
        }else{
            holder.txtState.setVisibility(View.VISIBLE);
        }
        holder.txtState.setText(String.valueOf(objCustomerAddressTran.getState()));
        holder.txtPhone.setText(String.valueOf(objCustomerAddressTran.getMobileNum()));
        if (isAddressAnimate) {
            Globals.SetItemAnimator(holder);
        }
    }

    @Override
    public int getItemCount() {
        return lstCustomerAddressTran.size();
    }

    public void CustomerAddressDataChanged(CustomerAddressTran objCustomerAddressTran, Integer position) {
        if (position != null) {
            lstCustomerAddressTran.get(position).setCustomerName(objCustomerAddressTran.getCustomerName());
            lstCustomerAddressTran.get(position).setMobileNum(objCustomerAddressTran.getMobileNum());
            lstCustomerAddressTran.get(position).setlinktoCountryMasterId(objCustomerAddressTran.getlinktoStateMasterId());
            lstCustomerAddressTran.get(position).setState(objCustomerAddressTran.getState());
            lstCustomerAddressTran.get(position).setlinktoCityMasterId(objCustomerAddressTran.getlinktoCityMasterId());
            lstCustomerAddressTran.get(position).setCity(objCustomerAddressTran.getCity());
            lstCustomerAddressTran.get(position).setlinktoAreaMasterId(objCustomerAddressTran.getlinktoAreaMasterId());
            lstCustomerAddressTran.get(position).setArea(objCustomerAddressTran.getArea());
            lstCustomerAddressTran.get(position).setAddress(objCustomerAddressTran.getAddress());
            lstCustomerAddressTran.get(position).setZipCode(objCustomerAddressTran.getZipCode());
            lstCustomerAddressTran.get(position).setAddressType(objCustomerAddressTran.getAddressType());
            lstCustomerAddressTran.get(position).setIsPrimary(objCustomerAddressTran.getIsPrimary());
        } else {
            lstCustomerAddressTran.add(0, objCustomerAddressTran);
        }
        isAddressAnimate = false;
        notifyDataSetChanged();
    }

    public void DeleteCustomerAddress(int position) {
        lstCustomerAddressTran.remove(position);
        isAddressAnimate = false;
        notifyItemRemoved(position);
    }

    public void AddCustomerAddress(int position, CustomerAddressTran objCustomerAddressTran) {
        lstCustomerAddressTran.add(position, objCustomerAddressTran);
        isAddressAnimate = false;
        notifyItemInserted(position);
    }

    private PopupWindow ShowEditDeletePopup(View view, final int position) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View layout = mInflater.inflate(R.layout.row_popup, null);


        TextView txtEdit = (TextView) layout.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView) layout.findViewById(R.id.txtDelete);

        txtEdit.setText(context.getResources().getString(R.string.action_edit));
        txtDelete.setText(context.getResources().getString(R.string.action_delete));

        layout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
        Drawable background = ContextCompat.getDrawable(context, android.R.drawable.picture_frame);
        popupWindow.setBackgroundDrawable(background);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();

            }
        });
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                objCustomerAddressListener.OnClickListener(lstCustomerAddressTran.get(position), position);
            }
        });
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                objCustomerAddressListener.DeleteClickListener(lstCustomerAddressTran.get(position), position);
            }
        });
        return popupWindow;
    }

    public interface CustomerAddressListener {
        void DeleteClickListener(CustomerAddressTran objCustomerAddressTran, int position);

        void OnClickListener(CustomerAddressTran objCustomerAddressTran, int position);
    }


    class CustomerAddressTranViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDefault, txtCustomerAddressTranId, txtCustomer, txtPhone, txtAddress, txtState, txtCity, txtArea, txtZipCode;
        CardView cvAddress;
        ImageButton ibOverflowMenu;

        public CustomerAddressTranViewHolder(View view) {
            super(view);

            cvAddress = (CardView) view.findViewById(R.id.cvAddress);

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

            ibOverflowMenu = (ImageButton) view.findViewById(R.id.ibOverflowMenu);

            ibOverflowMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName() != null
                    && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(context.getResources().getString(R.string.title_fragment_your_address))) {
                if (v.getId() == R.id.ibOverflowMenu) {
                    ShowEditDeletePopup(v, getAdapterPosition());
                }
            }
        }

    }
}



