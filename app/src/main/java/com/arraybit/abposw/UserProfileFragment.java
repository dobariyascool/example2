package com.arraybit.abposw;


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
import android.widget.RadioButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@SuppressWarnings("ConstantConditions")
public class UserProfileFragment extends Fragment implements CustomerJSONParser.CustomerRequestListener {

    CustomerMaster objCustomerMaster;
    EditText etFirstName, etMobile, etBirthDate;
    RadioButton rbMale, rbFemale;
    AppCompatSpinner spArea, spCity;
    Button btnUpdateProfile;
    TextView txtLoginChar, txtFullName, txtEmail;
    SharePreferenceManage objSharePreferenceManage;
    ProgressDialog progressDialog;
    int customerMasterId;
    Date birthDate;
    View view;
    UpdateResponseListener objUpdateResponseListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //region Toolbar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_your_profile));
        setHasOptionsMenu(true);
        //endregion

        //region EditText
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etBirthDate = (EditText) view.findViewById(R.id.etDateOfBirth);
        //endregion

        //region View
        txtLoginChar = (TextView) view.findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        //endregion

        //region RadioButton
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        //endregion

        //region Comment
       /* //Spinner start
        spCity = (AppCompatSpinner) view.findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) view.findViewById(R.id.spArea);
        //end*/
        //endregion

        //region Button
        btnUpdateProfile = (Button) view.findViewById(R.id.btnUpdate);
        //endregion

        setHasOptionsMenu(true);

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }

        if (Service.CheckNet(getActivity())) {
            UserRequest();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateControls()) {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                    return;
                }
                if (Service.CheckNet(getActivity())) {
                    UpdateUserProfileRequest();
                } else {
                    Globals.ShowSnackBar(btnUpdateProfile, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etBirthDate, getActivity(),false);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Globals.HideKeyBoard(getActivity(), getView());
                getActivity().getSupportFragmentManager().popBackStack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        if (objCustomerMaster != null) {
            this.objCustomerMaster = objCustomerMaster;
            SetUserName();
        } else {
            SetError(errorCode);
        }

    }

    public void EditTextOnClick() {
        Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
    }

    // region Private Methods
    private void SetUserName() {
        if (objCustomerMaster != null) {
            txtEmail.setText(objCustomerMaster.getEmail1());
            txtLoginChar.setText(objCustomerMaster.getEmail1().substring(0, 1).toUpperCase());
            txtFullName.setText(objCustomerMaster.getCustomerName());
            etFirstName.setText(objCustomerMaster.getCustomerName());
            etMobile.setText(objCustomerMaster.getPhone1());
            if (objCustomerMaster.getGender().equals(rbFemale.getText().toString())) {
                rbFemale.setChecked(true);
            } else {
                rbMale.setChecked(true);
            }
            if (objCustomerMaster.getBirthDate() != null) {
                try {
                    birthDate = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(objCustomerMaster.getBirthDate());
                    etBirthDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void CreateMyAccountPreference() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", etFirstName.getText().toString(), getActivity());
        objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", etMobile.getText().toString(), getActivity());
    }

    private void UserRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.SelectCustomerMaster(getActivity(), null, null, String.valueOf(customerMasterId), this);
    }

    private void UpdateUserProfileRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        CustomerMaster objCustomerMaster = new CustomerMaster();
        objSharePreferenceManage = new SharePreferenceManage();

        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            objCustomerMaster.setCustomerMasterId(Short.parseShort(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
        } else {
            objCustomerMaster.setCustomerMasterId(0);
        }
        objCustomerMaster.setCustomerName(etFirstName.getText().toString());
        objCustomerMaster.setPhone1(etMobile.getText().toString());
        if (rbMale.isChecked()) {
            objCustomerMaster.setGender(rbMale.getText().toString());
        }
        if (rbFemale.isChecked()) {
            objCustomerMaster.setGender(rbFemale.getText().toString());
        }
        if (!etBirthDate.getText().toString().isEmpty()) {
            try {
                birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etBirthDate.getText().toString());
                objCustomerMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        objCustomerJSONParser.UpdateCustomerMaster(objCustomerMaster, getActivity(), this);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(btnUpdateProfile, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "-2":
                Globals.ShowSnackBar(btnUpdateProfile, getResources().getString(R.string.MsgUpdateprofileFail), getActivity(), 1000);
                break;
            default:
                Globals.ShowSnackBar(btnUpdateProfile, getResources().getString(R.string.MsgUpdateProfile), getActivity(), 1000);
                CreateMyAccountPreference();
                ClearControls();
                getActivity().getSupportFragmentManager().popBackStack();
                objUpdateResponseListener = (UpdateResponseListener)getActivity();
                objUpdateResponseListener.UpdateResponse();
                break;
        }

    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
            etMobile.setError("Enter 10 digit " + getResources().getString(R.string.suPhone));
            IsValid = false;
        } else {
            etMobile.clearError();
        }
        return IsValid;
    }

    private void ClearControls() {
        etFirstName.setText("");
        etMobile.setText("");
        etBirthDate.setText("");
    }

    interface UpdateResponseListener{
        void UpdateResponse();
    }

    //endregion
}
