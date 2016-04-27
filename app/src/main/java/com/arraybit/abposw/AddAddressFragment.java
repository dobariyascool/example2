package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
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

@SuppressLint("ValidFragment")
public class AddAddressFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerAddressJSONParser.CustomerAddressRequestListener {

    Activity activity;
    ToggleButton btnHome, btnOffice;
    EditText etName, etAddress, etZip, etMobile;
    AppCompatSpinner spCountry, spState, spCity, spArea;
    Button btnAddress;
    com.rey.material.widget.CheckBox chkIsPrimary;
    SpinnerAdapter countryAdapter, stateAdapter, cityAdapter, areaAdapter;
    boolean flag = false;
    CustomerAddressTran objCustomerAddressTran;
    ArrayList<SpinnerItem> alCountryMaster, alStateMaster, alCityMaster, alAreaMaster;
    short CustomerAddressTranId, countryMasterId, stateMasterId, cityMasterId, areaMasterId;
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

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                if(stateMasterId==-1) {
                    stateMasterId = (short) v.getId();
                }
                if (stateMasterId == 0) {
                    cityAreaLayout.setVisibility(View.GONE);
                } else {
                    if (!flag) {
                        cityAreaLayout.setVisibility(View.VISIBLE);
                        RequestCityMaster();
                        flag = true;
                    }
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
                    spArea.setVisibility(View.INVISIBLE);
                } else {
                    if (!flag) {
                        RequestAreaMaster();
                        flag = true;
                    }
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
            objCustomerAddressTran.setlinktoStateMasterId((short) spState.getSelectedItemId());
            objCustomerAddressTran.setlinktoCityMasterId((short) spCity.getSelectedItemId());
            objCustomerAddressTran.setlinktoAreaMasterId((short) spArea.getSelectedItemId());
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
        stateMasterId = -1;
        return flag = false;
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
        } else {
            RequestCityMaster();
        }
        this.alStateMaster = alStateMaster;
        FillState();
    }

    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        if (objCustomerAddressTran == null) {
            progressDialog.dismiss();
        } else {
            RequestAreaMaster();
        }
        this.alCityMaster = alCityMaster;
        FillCity();
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        FillArea();
        if (!flag) {
            stateMasterId = objCustomerAddressTran.getlinktoStateMasterId();
            cityMasterId = objCustomerAddressTran.getlinktoCityMasterId();
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
        objCityJSONParser.SelectAllCityMasterByState(this, getActivity(), String.valueOf(stateMasterId));
    }

    private void RequestAreaMaster() {
        if (objCustomerAddressTran == null) {
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(this, getActivity(), String.valueOf(cityMasterId));
    }

    private void FillCountry() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("India");
        objSpinnerItem.setValue(1);
        alCountryMaster.add(0, objSpinnerItem);

        countryAdapter = new SpinnerAdapter(getActivity(), alCountryMaster, true);
        spCountry.setAdapter(countryAdapter);
    }

    private void FillState() {
        if (alStateMaster.size() > 0 && alStateMaster != null) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.yaState));
            objSpinnerItem.setValue(0);

            alStateMaster.add(0, objSpinnerItem);

            stateAdapter = new SpinnerAdapter(getActivity(), alStateMaster, true);
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

            cityAdapter = new SpinnerAdapter(getActivity(), alCityMaster, true);
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

            areaAdapter = new SpinnerAdapter(getActivity(), alAreaMaster, true);
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
       // flag = true;
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
//        for (int i = 0; i < stateAdapter.getCount(); i++) {
//            if (objCustomerAddressTran.getState().trim().equals(stateAdapter.getItem(i).toString())) {
//                spState.setSelection(i);
//                break;
//            }
//        }

        spState.setSelection(SpinnerItem.GetSpinnerItemIndex(alStateMaster, objCustomerAddressTran.getlinktoStateMasterId()));
        spCity.setSelection(SpinnerItem.GetSpinnerItemIndex(alCityMaster, objCustomerAddressTran.getlinktoCityMasterId()));
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