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
import android.view.KeyEvent;
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
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class AddAddressFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerAddressJSONParser.CustomerAddressRequestListener {

    Activity activity;
    ToggleButton btnHome, btnOffice;
    EditText etName, etAddress, etZip, etMobile;
    AppCompatSpinner spCountry, spState, spCity, spArea;
    TextView txtCountryError, txtStateError, txtCityError;
    Button btnAddress;
    com.rey.material.widget.CheckBox chkIsPrimary;
    boolean flag = false;
    CustomerAddressTran objCustomerAddressTran;
    ArrayList<SpinnerItem> alCountryMaster, alStateMaster, alCityMaster, alAreaMaster;
    short CustomerAddressTranId, countryMasterId;
    ProgressDialog progressDialog = new ProgressDialog();
    View view;
    LinearLayout cityAreaLayout,addAddressFragment;
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

        addAddressFragment = (LinearLayout)view.findViewById(R.id.addAddressFragment);

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
        txtCountryError = (TextView) view.findViewById(R.id.txtCountryError);
        txtStateError = (TextView) view.findViewById(R.id.txtStateError);
        txtCityError = (TextView) view.findViewById(R.id.txtCityError);
        cityAreaLayout = (LinearLayout) view.findViewById(R.id.cityAreaLayout);

        chkIsPrimary.setVisibility(View.INVISIBLE);
        alCountryMaster = new ArrayList<>();
        countryMasterId = 1;
        FillCountry();
        if (Service.CheckNet(getActivity())) {
            RequestStateMaster();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
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

        etAddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Globals.HideKeyBoard(getActivity(), view);
                if (view.getId() == 0) {
                    cityAreaLayout.setVisibility(View.GONE);
                } else {
                    cityAreaLayout.setVisibility(View.VISIBLE);
                    RequestCityMaster();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Globals.HideKeyBoard(getActivity(), parent);
            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Globals.HideKeyBoard(getActivity(), view);
                if (view.getId() == 0) {
                    spArea.setVisibility(View.INVISIBLE);
                } else {
                    RequestAreaMaster();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Globals.HideKeyBoard(getActivity(), parent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.btnHome) {
            btnHome.setChecked(true);
            btnOffice.setChecked(false);
        } else if (v.getId() == R.id.btnOffice) {
            btnHome.setChecked(false);
            btnOffice.setChecked(true);
        } else if (v.getId() == R.id.btnAddress) {
            view = v;
            if (!ValidateControls()) {
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                if (Service.CheckNet(getActivity())) {
                    objCustomerAddressTran = new CustomerAddressTran();
                    progressDialog.show(getActivity().getSupportFragmentManager(), "");
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                    objCustomerAddressTran.setCustomerName(etName.getText().toString().trim());
                    objCustomerAddressTran.setAddress(etAddress.getText().toString().trim());
                    if (btnHome.isChecked()) {
                        objCustomerAddressTran.setAddressType((short) Globals.AddressType.Home.getValue());
                    } else {
                        objCustomerAddressTran.setAddressType((short) Globals.AddressType.Office.getValue());
                    }
                    objCustomerAddressTran.setlinktoCountryMasterId(countryMasterId);
                    objCustomerAddressTran.setCountry(spCountry.getSelectedItem().toString());
                    objCustomerAddressTran.setlinktoStateMasterId((short) spState.getAdapter().getItemId(spState.getSelectedItemPosition()));
                    objCustomerAddressTran.setState(spState.getSelectedItem().toString());
                    if (spCity != null && spCity.getAdapter() != null) {
                        objCustomerAddressTran.setlinktoCityMasterId((short) spCity.getAdapter().getItemId(spCity.getSelectedItemPosition()));
                        objCustomerAddressTran.setCity(spCity.getSelectedItem().toString());
                    }
                    if (spArea != null && spArea.getAdapter() != null) {
                        objCustomerAddressTran.setlinktoAreaMasterId((short) spArea.getAdapter().getItemId(spArea.getSelectedItemPosition()));
                        objCustomerAddressTran.setArea(spArea.getSelectedItem().toString());
                    }
                    objCustomerAddressTran.setZipCode(etZip.getText().toString().trim());
                    objCustomerAddressTran.setMobileNum(etMobile.getText().toString().trim());
                    if (objCustomerAddressTran == null) {
                        objCustomerAddressTran.setIsPrimary(true);
                    }
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
                }else {
                    Globals.ShowSnackBar(addAddressFragment, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
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
        Globals.HideKeyBoard(getActivity(), addAddressFragment);
        if (getTargetFragment() != null) {
            objAddNewAddressListener = (AddNewAddressListener) getTargetFragment();
            objAddNewAddressListener.AddNewAddress(null);
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (activity.getTitle().equals(getResources().getString(R.string.title_activity_check_out))) {
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
            if (!flag) {
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
            if (!flag) {
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

    //region Private Methods and Interface
    private void RequestStateMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        StateJSONParser objStateJSONParser = new StateJSONParser();
        objStateJSONParser.SelectAllStateMaster(this, getActivity(), String.valueOf(countryMasterId));
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
            spState.setVisibility(View.INVISIBLE);
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
            spCity.setVisibility(View.INVISIBLE);
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
            spArea.setVisibility(View.INVISIBLE);
        }
    }

    private void SetError(String errorCode) {
        if(btnAddress.getText().equals(getResources().getString(R.string.yaAdd))){
            if (errorCode.equals("-1")) {
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (!errorCode.equals("0")) {
                if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_check_out))) {
                    objAddNewAddressListener = (AddNewAddressListener) getActivity();
                    objCustomerAddressTran.setCustomerAddressTranId(Integer.parseInt(errorCode));
                    objAddNewAddressListener.AddNewAddress(objCustomerAddressTran);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    objAddNewAddressListener = (AddNewAddressListener) getTargetFragment();
                    objCustomerAddressTran.setCustomerAddressTranId(Integer.parseInt(errorCode));
                    objAddNewAddressListener.AddNewAddress(objCustomerAddressTran);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }else{
            if (errorCode.equals("-1")) {
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
            } else if (errorCode.equals("0")) {
                if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_check_out))) {
                    objAddNewAddressListener = (AddNewAddressListener) getActivity();
                    objAddNewAddressListener.AddNewAddress(objCustomerAddressTran);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    objAddNewAddressListener = (AddNewAddressListener) getTargetFragment();
                    objAddNewAddressListener.AddNewAddress(objCustomerAddressTran);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
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

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.clearError();
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etAddress.clearError();
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etAddress.clearError();
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etAddress.clearError();
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.VISIBLE);
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            etName.clearError();
            txtCityError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.VISIBLE);
            etName.clearError();
            etAddress.clearError();
            txtCityError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            etAddress.clearError();
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            etAddress.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            etName.clearError();
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etName.getText().toString().equals("")
                && etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() == 0
                && etZip.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etName.setError("Enter " + getResources().getString(R.string.yaName));
            etAddress.setError("Enter " + getResources().getString(R.string.yaAddress));
            txtStateError.setVisibility(View.VISIBLE);
            etZip.setError("Enter " + getResources().getString(R.string.yaZip));
            etMobile.setError("Enter " + getResources().getString(R.string.yaMobileNum));
            txtCityError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etName.getText().toString().equals("")
                && !etAddress.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && !etZip.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etName.clearError();
            etAddress.clearError();
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            if (!etZip.getText().toString().equals("") && etZip.getText().length() != 6) {
                etZip.setError("Enter " + getResources().getString(R.string.yaValidZip));
                IsValid = false;
            } else {
                etZip.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.yaMobileNum));
                IsValid = false;
            } else {
                etMobile.clearError();
            }
        }
        return IsValid;
    }

    public interface AddNewAddressListener {
        void AddNewAddress(CustomerAddressTran objCustomerAddressTran);
    }
    //endregion
}