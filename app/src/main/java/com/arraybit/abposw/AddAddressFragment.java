package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

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

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class AddAddressFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerAddressJSONParser.CustomerAddressRequestListener {

    Activity activity;
    ToggleButton btnHome, btnOffice;
    EditText etName, etAddress, etZip, etMobile;
    AppCompatSpinner spCountry, spState, spCity, spArea;
    Button btnAddress;
    com.rey.material.widget.CheckBox chkIsPrimary;
    boolean flag = false;
    CustomerAddressTran objCustomerAddressTran;
    ArrayList<SpinnerItem> alCountryMaster, alStateMaster, alCityMaster, alAreaMaster;
    short CustomerAddressTranId, countryMasterId;
    ProgressDialog progressDialog = new ProgressDialog();
    View view;
    LinearLayout cityAreaLayout;
    AddNewAddressListener objAddNewAddressListener;

    public AddAddressFragment(Activity activity, CustomerAddressTran objCustomerAddressTran) {
        this.activity = activity;
        if (objCustomerAddressTran != null) {
            this.objCustomerAddressTran = objCustomerAddressTran;
        }
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

        btnHome = (ToggleButton) view.findViewById(R.id.btnHome);
        btnOffice = (ToggleButton) view.findViewById(R.id.btnOffice);
        etName = (EditText) view.findViewById(R.id.etName);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etZip = (EditText) view.findViewById(R.id.etZip);
        etMobile = (EditText) view.findViewById(R.id.etMobile);

        spCountry = (AppCompatSpinner) view.findViewById(R.id.spCountry);
        spState = (AppCompatSpinner) view.findViewById(R.id.spState);
        spCity = (AppCompatSpinner) view.findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) view.findViewById(R.id.spArea);
        chkIsPrimary = (com.rey.material.widget.CheckBox) view.findViewById(R.id.chkIsPrimary);
        btnAddress = (Button) view.findViewById(R.id.btnAddress);
        cityAreaLayout = (LinearLayout) view.findViewById(R.id.cityAreaLayout);

        chkIsPrimary.setVisibility(View.INVISIBLE);
        alCountryMaster = new ArrayList<>();
        countryMasterId = 1;
        FillCountry();
        if (Service.CheckNet(getActivity())) {
            RequestStateMaster();
        } else {
            Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        btnHome.setOnClickListener(this);
        btnOffice.setOnClickListener(this);
        spState.setOnTouchListener(this);
        spCity.setOnTouchListener(this);
        btnAddress.setOnClickListener(this);

        if (objCustomerAddressTran != null) {
            CustomerAddressTranId = (short) objCustomerAddressTran.getCustomerAddressTranId();
            chkIsPrimary.setVisibility(View.VISIBLE);
            btnAddress.setText(getResources().getString(R.string.yaEdit));
            cityAreaLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (view.getId() == 0) {
                        cityAreaLayout.setVisibility(View.GONE);
                    } else {
                        cityAreaLayout.setVisibility(View.VISIBLE);
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
                if (view.getId() == 0) {
                    spArea.setVisibility(View.INVISIBLE);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnHome) {
            btnHome.setChecked(true);
            btnOffice.setChecked(false);
        } else if (v.getId() == R.id.btnOffice) {
            btnHome.setChecked(false);
            btnOffice.setChecked(true);
        } else if (v.getId() == R.id.btnAddress) {
            objCustomerAddressTran = new CustomerAddressTran();
            view = v;
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objCustomerAddressTran.setCustomerName(etName.getText().toString());
            objCustomerAddressTran.setAddress(etAddress.getText().toString());
            if (btnHome.isChecked()) {
                objCustomerAddressTran.setAddressType((short) Globals.AddressType.Home.getValue());
            } else {
                objCustomerAddressTran.setAddressType((short) Globals.AddressType.Office.getValue());
            }
            objCustomerAddressTran.setlinktoCountryMasterId(countryMasterId);
            objCustomerAddressTran.setlinktoStateMasterId((short)spState.getAdapter().getItemId(spState.getSelectedItemPosition()));
            objCustomerAddressTran.setlinktoCityMasterId((short)spCity.getAdapter().getItemId(spCity.getSelectedItemPosition()));
            objCustomerAddressTran.setlinktoAreaMasterId((short)spArea.getAdapter().getItemId(spArea.getSelectedItemPosition()));
            objCustomerAddressTran.setZipCode(etZip.getText().toString());
            objCustomerAddressTran.setMobileNum(etMobile.getText().toString());
            objCustomerAddressTran.setIsPrimary(true);
            objCustomerAddressTran.setIsDeleted(false);
            objCustomerAddressTran.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));

            CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
            if (btnAddress.getText().equals(getResources().getString(R.string.yaAdd))) {
                objCustomerAddressJSONParser.InsertCustomerAddressTran(getActivity(), this, objCustomerAddressTran);
            } else {
                objCustomerAddressTran.setCustomerAddressTranId(CustomerAddressTranId);
                objCustomerAddressTran.setIsPrimary(chkIsPrimary.isChecked());
                objCustomerAddressJSONParser.UpdateCustomerAddressTran(getActivity(), this, objCustomerAddressTran);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        flag = true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getTargetFragment() != null) {
            objAddNewAddressListener = (AddNewAddressListener) getTargetFragment();
            objAddNewAddressListener.AddNewAddress(null);
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void StateResponse(ArrayList<SpinnerItem> alStateMaster) {
        if (objCustomerAddressTran == null) {
            progressDialog.dismiss();
            this.alStateMaster = alStateMaster;
            FillState();
        } else {
            this.alStateMaster = alStateMaster;
            FillState();
            if(!flag) {
                spState.setSelection(SpinnerItem.GetSpinnerItemIndex(alStateMaster, objCustomerAddressTran.getlinktoStateMasterId()));
            }
        }
    }

    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        if (objCustomerAddressTran == null) {
            progressDialog.dismiss();
            this.alCityMaster = alCityMaster;
            FillCity();
        } else {
            this.alCityMaster = alCityMaster;
            FillCity();
            if(!flag) {
                spCity.setSelection(SpinnerItem.GetSpinnerItemIndex(alCityMaster, objCustomerAddressTran.getlinktoCityMasterId()));
            }
        }
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        FillArea();
        if (!flag) {
            SetAddress();
        }
    }

    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        if (errorCode != null) {
            SetError(errorCode);
        }
    }

    //region Private Methods
    private void RequestStateMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        StateJSONParser objStateJSONParser = new StateJSONParser();
        objStateJSONParser.SelectStateMaster(this, getActivity(), String.valueOf(countryMasterId));
    }

    private void RequestCityMaster() {
        if (objCustomerAddressTran == null) {
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }
        CityJSONParser objCityJSONParser = new CityJSONParser();
        objCityJSONParser.SelectAllCityMasterByState(this, getActivity(), String.valueOf(spState.getAdapter().getItemId(spState.getSelectedItemPosition())));
    }

    private void RequestAreaMaster() {
        if (objCustomerAddressTran == null) {
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(this, getActivity(), String.valueOf(spCity.getAdapter().getItemId(spCity.getSelectedItemPosition())));
    }

    private void FillCountry() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("India");
        objSpinnerItem.setValue(1);
        alCountryMaster.add(0, objSpinnerItem);

        SpinnerAdapter countryAdapter = new SpinnerAdapter(getActivity(), alCountryMaster, true);
        spCountry.setAdapter(countryAdapter);
    }

    private void FillState() {
        if (alStateMaster.size() > 0 && alStateMaster != null) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.yaState));
            objSpinnerItem.setValue(0);

            alStateMaster.add(0, objSpinnerItem);

            SpinnerAdapter stateAdapter = new SpinnerAdapter(getActivity(), alStateMaster, true);
            spState.setVisibility(View.VISIBLE);
            spState.setAdapter(stateAdapter);
        } else {
            spState.setVisibility(View.GONE);
        }
    }

    private void FillCity() {
        if (alCityMaster.size() > 0 && alCityMaster != null) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.yaCity));
            objSpinnerItem.setValue(0);

            alCityMaster.add(0, objSpinnerItem);

            SpinnerAdapter cityAdapter = new SpinnerAdapter(getActivity(), alCityMaster, true);
            spCity.setVisibility(View.VISIBLE);
            spCity.setAdapter(cityAdapter);
        } else {
            spCity.setVisibility(View.GONE);
        }
    }

    private void FillArea() {
        if (alAreaMaster.size() > 0 && alAreaMaster != null) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.yaArea));
            objSpinnerItem.setValue(0);

            alAreaMaster.add(0, objSpinnerItem);

            SpinnerAdapter areaAdapter = new SpinnerAdapter(getActivity(), alAreaMaster, true);
            spArea.setVisibility(View.VISIBLE);
            spArea.setAdapter(areaAdapter);
        } else {
            spArea.setVisibility(View.GONE);
        }
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "0":
                objAddNewAddressListener = (AddNewAddressListener) getTargetFragment();
                objAddNewAddressListener.AddNewAddress(objCustomerAddressTran);
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void SetAddress() {
        if (objCustomerAddressTran.getAddressType() == Globals.AddressType.Home.getValue()) {
            btnHome.setChecked(true);
            btnOffice.setChecked(false);
        } else {
            btnHome.setChecked(false);
            btnOffice.setChecked(true);
        }
        etName.setText(objCustomerAddressTran.getCustomerName());
        etAddress.setText(objCustomerAddressTran.getAddress());

        spCountry.setSelection(0);

        spArea.setSelection(SpinnerItem.GetSpinnerItemIndex(alAreaMaster, objCustomerAddressTran.getlinktoAreaMasterId()));

        etZip.setText(objCustomerAddressTran.getZipCode());
        etMobile.setText(objCustomerAddressTran.getMobileNum());
        chkIsPrimary.setChecked(objCustomerAddressTran.getIsPrimary());
    }
    //endregion

    public interface AddNewAddressListener {
        void AddNewAddress(CustomerAddressTran objCustomerAddressTran);
    }
}