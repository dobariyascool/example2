package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"ConstantConditions", "ResourceType"})
public class YourAddressFragment extends Fragment implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, CustomerAddressAdapter.CustomerAddressListener, AddAddressFragment.AddNewAddressListener,ConfirmDialog.ConfirmationResponseListener {

    FloatingActionButton fabAddress;
    LinearLayout errorLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvAddress;
    ProgressDialog progressDialog = new ProgressDialog();
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    CustomerAddressAdapter adapter;
    Integer position = null;
    int customerMasterId;
    boolean isNewBooking;
    CoordinatorLayout yourAddressFragment;
    CustomerAddressTran objCustomerAddress;

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

        yourAddressFragment = (CoordinatorLayout) view.findViewById(R.id.yourAddressFragment);

        fabAddress = (FloatingActionButton) view.findViewById(R.id.fabAddress);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        rvAddress = (RecyclerView) view.findViewById(R.id.rvAddress);
        rvAddress.setVisibility(View.GONE);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvAddress.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!adapter.isAddressAnimate) {
                    adapter.isAddressAnimate = true;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_your_address))) {
            if (v.getId() == R.id.fabAddress) {
                AddAddressFragment addAddressFragment = new AddAddressFragment(getActivity(), null);
                addAddressFragment.setTargetFragment(this, 0);
                fabAddress.hide();
                rvAddress.setVisibility(View.GONE);
                Globals.ReplaceFragment(addAddressFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_address_fragment), R.id.yourAddressFragment);
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
        this.objCustomerAddress = objCustomerAddressTran;
        ConfirmDialog confirmDialog = new ConfirmDialog(null,true,String.format(getActivity().getResources().getString(R.string.cdfConfirmDeleteMsg),"address"));
        confirmDialog.setTargetFragment(this,0);
        confirmDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void ConfirmResponse() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        objCustomerAddressJSONParser.DeleteCustomerAddressTran(getActivity(), this, String.valueOf(objCustomerAddress.getCustomerAddressTranId()));
    }

    @Override
    public void OnClickListener(CustomerAddressTran objCustomerAddressTran, int position) {
        this.position = position;
        fabAddress.hide();
        rvAddress.setVisibility(View.GONE);
        AddAddressFragment addAddressFragment = new AddAddressFragment(getActivity(), objCustomerAddressTran);
        addAddressFragment.setTargetFragment(this, 0);
        Globals.ReplaceFragment(addAddressFragment, getActivity().getSupportFragmentManager(), getResources().getString(R.string.title_add_address_fragment), R.id.yourAddressFragment);
    }

    @Override
    public void AddNewAddress(CustomerAddressTran objCustomerAddressTran) {
        if (objCustomerAddressTran == null) {
            fabAddress.show();
            rvAddress.setVisibility(View.VISIBLE);
        } else {
            rvAddress.setVisibility(View.VISIBLE);
            if (adapter == null || adapter.getItemCount() == 0) {
                isNewBooking = true;
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
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }
        if (customerMasterId == 0) {
            progressDialog.dismiss();
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvAddress, 0);
        } else {
            CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
            objCustomerAddressJSONParser.SelectAllCustomerAddressTran(getActivity(), this, String.valueOf(customerMasterId));
        }
    }

    private void SetRecyclerView() {
        if (alCustomerAddressTran == null) {
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvAddress, 0);
        } else if (alCustomerAddressTran.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvAddress, 0);
        } else {
            rvAddress.setVisibility(View.VISIBLE);
            adapter = new CustomerAddressAdapter(getActivity(), alCustomerAddressTran, this, getActivity().getSupportFragmentManager());
            rvAddress.setAdapter(adapter);
            rvAddress.setLayoutManager(linearLayoutManager);
            if (isNewBooking) {
                isNewBooking = false;
                //prevent floating action button animation
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        fabAddress.show();
                    }
                }, 1600);
            }
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
