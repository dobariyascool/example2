package com.arraybit.abposw;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.adapter.AddressSelectorAdapter;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.CustomerAddressTran;

import java.util.ArrayList;

public class AddressSelectorBottomDialog extends DialogFragment implements AddressSelectorAdapter.AddressSelectorListener {

    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    ArrayList<BusinessMaster> alBusinessMaster;
    AddressSelectorResponseListener objAddressSelectorResponseListener;
    AddressSelectorAdapter addressSelectorAdapter;
    ProgressDialog progressDialog = new ProgressDialog();

    public AddressSelectorBottomDialog(ArrayList<CustomerAddressTran> alCustomerAddressTran, ArrayList<BusinessMaster> alBusinessMaster) {
        this.alCustomerAddressTran = alCustomerAddressTran;
        this.alBusinessMaster = alBusinessMaster;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_address_selector_bottom_dialog, null, false);
        RecyclerView rvAddress = (RecyclerView) view.findViewById(R.id.rvAddress);
        if (alBusinessMaster != null) {
            addressSelectorAdapter = new AddressSelectorAdapter(getActivity(), null, alBusinessMaster, this);
        } else if (alCustomerAddressTran != null) {
            addressSelectorAdapter = new AddressSelectorAdapter(getActivity(), alCustomerAddressTran, null, this);
        }

        rvAddress.setAdapter(addressSelectorAdapter);
        rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Dialog mBottomSheetDialog = new Dialog(getActivity(),
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        return mBottomSheetDialog;
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_address_selector_bottom_dialog, container, false);
//        return view;
//    }

    @Override
    public void AddressSelectorOnClickListener(CustomerAddressTran objCustomerAddressTran, BusinessMaster objBusinessMaster) {
        objAddressSelectorResponseListener = (AddressSelectorResponseListener) getActivity();
        if (objCustomerAddressTran != null) {
            objAddressSelectorResponseListener.AddressSelectorResponse(objCustomerAddressTran, null);
        } else if (objBusinessMaster != null) {
            objAddressSelectorResponseListener.AddressSelectorResponse(null, objBusinessMaster);
        }
        dismiss();
    }


    public interface AddressSelectorResponseListener {
        void AddressSelectorResponse(CustomerAddressTran objCustomerAddressTran, BusinessMaster objBusinessMaster);
    }
}


