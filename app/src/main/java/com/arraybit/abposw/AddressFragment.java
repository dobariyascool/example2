package com.arraybit.abposw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CustomerAddressAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerAddressTran;
import com.arraybit.parser.CustomerAddressJSONParser;

import java.util.ArrayList;

public class AddressFragment extends Fragment implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, CustomerAddressAdapter.CustomerAddressListener {

    FloatingActionButton fabAddress;
    LinearLayout errorLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvAddress;
    ProgressDialog progressDialog = new ProgressDialog();
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    CustomerAddressAdapter adapter;
    int position;

    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_your_address));
        setHasOptionsMenu(true);

        fabAddress = (FloatingActionButton) view.findViewById(R.id.fabAddress);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        rvAddress = (RecyclerView) view.findViewById(R.id.rvAddress);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        fabAddress.setOnClickListener(this);

        if (Service.CheckNet(getActivity())) {
            fabAddress.show();
            RequestCustomerAddress();
        } else {
            fabAddress.hide();
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvAddress, R.drawable.wifi_drawable);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddress) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_your_address))) {
                if (v.getId() == R.id.fabAddress) {
                    AddAddressFragment addAddressFragment = new AddAddressFragment(getActivity(), null);
                    addAddressFragment.setTargetFragment(this, 0);
                    fabAddress.hide();
                    Globals.ReplaceFragment(addAddressFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_booking_fragment), R.id.yourAddressFragment);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        if (alCustomerAddressTran != null) {
            this.alCustomerAddressTran = alCustomerAddressTran;
            SetRecyclerView();
        } else if (objCustomerAddressTran != null) {
            fabAddress.hide();
            Globals.ReplaceFragment(new AddAddressFragment(getActivity(), objCustomerAddressTran), getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_add_address_fragment), R.id.yourAddressFragment);
        }else if(errorCode != null){
            SetErrorCode(errorCode);
            SetRecyclerView();
        }
    }

    @Override
    public void DeleteClickListener(CustomerAddressTran objCustomerAddressTran, int position) {
        this.position = position;
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        objCustomerAddressJSONParser.DeleteCustomerAddressTran(getActivity(), this, String.valueOf(objCustomerAddressTran.getCustomerAddressTranId()));
    }

    @Override
    public void OnClickListener(CustomerAddressTran objCustomerAddressTran, int position) {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        objCustomerAddressJSONParser.SelectCustomerAddressTranByMasterId(getActivity(), this, String.valueOf(objCustomerAddressTran.getCustomerAddressTranId()));
    }

    //region Private Methods
    private void RequestCustomerAddress() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        objCustomerAddressJSONParser.SelectAllCustomerAddressTran(getActivity(), this, objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
    }

    private void SetRecyclerView() {
        if (alCustomerAddressTran == null) {
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvAddress, 0);
        } else if (alCustomerAddressTran.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvAddress, 0);
        } else {
            adapter = new CustomerAddressAdapter(getActivity(), alCustomerAddressTran, this);
            adapter.CustomerAddressDataChanged(alCustomerAddressTran);
            rvAddress.setAdapter(adapter);
            rvAddress.setLayoutManager(linearLayoutManager);
        }
    }

    private void SetErrorCode(String errorCode){
        switch (errorCode){
            case "0":
                adapter.DeleteCustomerAddress(position);
        }
    }
    //endregion
}
