package com.arraybit.abposw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.CustomerAddressTran;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.CityJSONParser;
import com.arraybit.parser.CustomerAddressJSONParser;
import com.arraybit.parser.StateJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.util.ArrayList;

public class AddAddressFragment extends Fragment implements View.OnClickListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener {

    Activity activity;
    EditText etName, etAddress, etZip, etMobile;
    AppCompatSpinner spCountry, spState, spCity, spArea;
    Button btnAddress;
    ArrayList<SpinnerItem> alCountryMaster, alStateMaster, alCityMaster, alAreaMaster;
    short countryMasterId, stateMasterId, cityMasterId, areaMasterId;
    ProgressDialog progressDialog = new ProgressDialog();

    public AddAddressFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_add_address_fragment));
        setHasOptionsMenu(true);

        etName = (EditText) view.findViewById(R.id.etName);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etZip = (EditText) view.findViewById(R.id.etZip);
        etMobile = (EditText) view.findViewById(R.id.etMobile);

        spCountry = (AppCompatSpinner) view.findViewById(R.id.spCountry);
        spState = (AppCompatSpinner) view.findViewById(R.id.spState);
        spCity = (AppCompatSpinner) view.findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) view.findViewById(R.id.spArea);
        btnAddress = (Button) view.findViewById(R.id.btnAddress);

        alCountryMaster = new ArrayList<>();
        countryMasterId = 1;
        FillCountry();
        if (Service.CheckNet(getActivity())) {
            RequestStateMaster();
        } else {
            Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                stateMasterId = (short) v.getId();
                if (stateMasterId == 0) {
                    spCity.setVisibility(View.GONE);
                } else {
                    RequestCityMaster();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                cityMasterId = (short) v.getId();
                if (cityMasterId == 0) {
                    spArea.setVisibility(View.GONE);
                } else {
                    RequestAreaMaster();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                areaMasterId = (short) v.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddress.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddress) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            CustomerAddressTran objCustomerAddressTran = new CustomerAddressTran();
            objCustomerAddressTran.setCustomer(etName.getText().toString());
            objCustomerAddressTran.setAddress(etAddress.getText().toString());
            objCustomerAddressTran.setCountry(String.valueOf(spCountry.getSelectedItemId()));
            objCustomerAddressTran.setState(String.valueOf(spState.getSelectedItemId()));
            objCustomerAddressTran.setCity(String.valueOf(spCity.getSelectedItemId()));
            objCustomerAddressTran.setArea(String.valueOf(spArea.getSelectedItemId()));
            objCustomerAddressTran.setZipCode(etZip.getText().toString());
            objCustomerAddressTran.setMobileNum(etMobile.getText().toString());
            objCustomerAddressTran.setIsPrimary(true);
            objCustomerAddressTran.setIsDeleted(false);
            objCustomerAddressTran.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));

            CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
            objCustomerAddressJSONParser.InsertCustomerAddressTran(getActivity(), this, objCustomerAddressTran);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    activity.finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void StateResponse(ArrayList<SpinnerItem> alStateMaster) {
        progressDialog.dismiss();
        this.alStateMaster = alStateMaster;
        if (alStateMaster.size() > 0) {
            FillState();
        } else {
            spState.setVisibility(View.GONE);
        }
    }

    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        progressDialog.dismiss();
        this.alCityMaster = alCityMaster;
        if (alCityMaster.size() > 0) {
            FillCity();
        } else {
            spCity.setVisibility(View.GONE);
        }
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        if (alAreaMaster.size() > 0) {
            FillArea();
        } else {
            spArea.setVisibility(View.GONE);
        }
    }

    //region Private Methods
    private void RequestStateMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        StateJSONParser objStateJSONParser = new StateJSONParser();
        objStateJSONParser.SelectStateMaster(this, getActivity(), String.valueOf(countryMasterId));
    }

    private void RequestCityMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        CityJSONParser objCityJSONParser = new CityJSONParser();
        objCityJSONParser.SelectAllCityMasterByState(this, getActivity(), String.valueOf(stateMasterId));
    }

    private void RequestAreaMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(this, getActivity(), String.valueOf(cityMasterId));
    }

    private void FillCountry() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("India");
        objSpinnerItem.setValue(1);
        alCountryMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alCountryMaster, true);
        spCountry.setAdapter(adapter);
    }

    private void FillState() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getResources().getString(R.string.suState));
        objSpinnerItem.setValue(0);

        alStateMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alStateMaster, true);
        spState.setVisibility(View.VISIBLE);
        spState.setAdapter(adapter);
    }

    private void FillCity() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getResources().getString(R.string.suCity));
        objSpinnerItem.setValue(0);

        alCityMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alCityMaster, true);
        spCity.setVisibility(View.VISIBLE);
        spCity.setAdapter(adapter);
    }

    private void FillArea() {
        if (alAreaMaster.size() != 0) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.suArea));
            objSpinnerItem.setValue(0);

            alAreaMaster.add(0, objSpinnerItem);

            SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alAreaMaster, true);
            spArea.setVisibility(View.VISIBLE);
            spArea.setAdapter(adapter);
        } else {
            spArea.setVisibility(View.GONE);
        }
    }
    //endregion
}