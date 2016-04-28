package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import java.util.Timer;
import java.util.TimerTask;

public class YourAddressFragment extends Fragment implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, CustomerAddressAdapter.CustomerAddressListener, AddAddressFragment.AddNewAddressListener {

    FloatingActionButton fabAddress;
    LinearLayout errorLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvAddress;
    ProgressDialog progressDialog = new ProgressDialog();
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    CustomerAddressAdapter adapter;
    Integer position = null;
    ItemTouchHelper.SimpleCallback simpleItemTouchHelper;

    public YourAddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_address, container, false);
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

//        simpleItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                if (direction == ItemTouchHelper.RIGHT) {
//                    progressDialog.show(getActivity().getSupportFragmentManager(), "");
//                    CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
//                    objCustomerAddressJSONParser.SelectCustomerAddressTranByMasterId(getActivity(), AddressFragment.this, String.valueOf(((CustomerAddressAdapter.CustomerAddressTranViewHolder) viewHolder).txtCustomerAddressTranId.getText()));
//                } else {
//                    progressDialog.show(getActivity().getSupportFragmentManager(), "");
//                    CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
//                    objCustomerAddressJSONParser.DeleteCustomerAddressTran(getActivity(), AddressFragment.this, String.valueOf(((CustomerAddressAdapter.CustomerAddressTranViewHolder) viewHolder).txtCustomerAddressTranId.getText()));
//                }
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
//        itemTouchHelper.attachToRecyclerView(rvAddress);
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
                    Globals.ReplaceFragment(addAddressFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_address_fragment), R.id.yourAddressFragment);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_your_address))) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        if (alCustomerAddressTran != null) {
            this.alCustomerAddressTran = alCustomerAddressTran;
            SetRecyclerView();
        } else if (errorCode != null) {
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
        this.position = position;
        fabAddress.hide();
        AddAddressFragment addAddressFragment = new AddAddressFragment(getActivity(), objCustomerAddressTran);
        addAddressFragment.setTargetFragment(this, 0);
        Globals.ReplaceFragment(addAddressFragment, getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_add_address_fragment), R.id.yourAddressFragment);
    }

    @Override
    public void AddNewAddress(CustomerAddressTran objCustomerAddressTran) {
        if (objCustomerAddressTran == null) {
            fabAddress.show();
        } else {
            if (adapter == null || adapter.getItemCount() == 0) {
                RequestCustomerAddress();
            } else {
                adapter.CustomerAddressDataChanged(objCustomerAddressTran, position);
                rvAddress.setAdapter(adapter);
                rvAddress.setLayoutManager(linearLayoutManager);
            }
            if (position == null) {
                Globals.ShowSnackBar(rvAddress, getActivity().getResources().getString(R.string.MsgInsertAddressSuccess), getActivity(), 1000);
            } else {
                Globals.ShowSnackBar(rvAddress, getActivity().getResources().getString(R.string.MsgUpdateAddressSuccess), getActivity(), 1000);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    fabAddress.show();
                }
            }, 1600);
        }
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
            adapter = new CustomerAddressAdapter(getActivity(), alCustomerAddressTran, this, getActivity().getSupportFragmentManager());
            //adapter.CustomerAddressDataChanged(alCustomerAddressTran);
            rvAddress.setAdapter(adapter);
            rvAddress.setLayoutManager(linearLayoutManager);
        }
    }

    private void SetErrorCode(String errorCode) {
        switch (errorCode) {
            case "0":
                adapter.DeleteCustomerAddress(position);
        }
    }
    //endregion
}
