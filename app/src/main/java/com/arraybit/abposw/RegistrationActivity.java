package com.arraybit.abposw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.CityJSONParser;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerJSONParser.CustomerRequestListener {

    ArrayList<SpinnerItem> alCityMaster, alAreaMaster;
    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone, etDateOfBirth;
    AppCompatSpinner spArea, spCity;
    short cityMasterId, areaMasterId;
    Date birthDate;
    View view;
    RadioButton rbMale, rbFemale;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        FrameLayout registrationLayout = (FrameLayout) findViewById(R.id.registrationLayout);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etDateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
        //hide keyboard
        etDateOfBirth.setInputType(InputType.TYPE_NULL);

        spCity = (AppCompatSpinner) findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) findViewById(R.id.spArea);

        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);

        CompoundButton cbPrivacyPolicy = (CompoundButton) findViewById(R.id.cbPrivacyPolicy);
        CompoundButton cbTermsofService = (CompoundButton) findViewById(R.id.cbTermsofService);

        cbPrivacyPolicy.setOnClickListener(this);
        cbTermsofService.setOnClickListener(this);

        if (Service.CheckNet(this)) {
            RequestCityMaster();
        } else {
            Globals.ShowSnackBar(registrationLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        btnSignUp.setOnClickListener(this);
        cbSignIn.setOnClickListener(this);

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
        etDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etDateOfBirth, RegistrationActivity.this);
                }
            }
        });
    }

    public void EditTextOnClick(View view) {
        Globals.ShowDatePickerDialog(etDateOfBirth, RegistrationActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                if (spCity.getSelectedItemId() == 0 && !etFirstName.getText().toString().equals("")
                        && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    Globals.ShowSnackBar(view, "Select City", RegistrationActivity.this, 1000);
                } else if (spArea.getSelectedItemId() == 0 && !etFirstName.getText().toString().equals("")
                        && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    Globals.ShowSnackBar(view, "Select Area", RegistrationActivity.this, 1000);
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), RegistrationActivity.this, 1000);
                }
            } else {
                if (Service.CheckNet(this)) {
                    RegistrationRequest();
                } else {
                    Globals.ShowSnackBar(getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
        } else if (v.getId() == R.id.cbSignIn) {
            finish();
        } else if (v.getId() == R.id.cbPrivacyPolicy) {
            Globals.ReplaceFragment(new PolicyFragment(null), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_policy), R.id.registrationLayout);
        } else if (v.getId() == R.id.cbTermsofService) {
            Globals.ReplaceFragment(new PolicyFragment(null), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_policy), R.id.registrationLayout);
        }
    }

    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        progressDialog.dismiss();
        this.alCityMaster = alCityMaster;
        FillCity();
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        FillArea();
    }


    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //region Private Methods
    private void RequestCityMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        CityJSONParser objCityJSONParser = new CityJSONParser();
        objCityJSONParser.SelectAllCityMasterByState(RegistrationActivity.this, String.valueOf(Globals.linktoStateMasterId));
    }

    private void RequestAreaMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(RegistrationActivity.this, String.valueOf(cityMasterId));
    }

    private void RegistrationRequest() {
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        CustomerMaster objCustomerMaster = new CustomerMaster();
        objCustomerMaster.setCustomerName(etFirstName.getText().toString() + " " + etLastName.getText().toString());
        objCustomerMaster.setEmail1(etEmail.getText().toString());
        objCustomerMaster.setPassword(etPassword.getText().toString());
        objCustomerMaster.setConfirmPassword(etConfirmPassword.getText().toString());
        objCustomerMaster.setPhone1(etPhone.getText().toString());
        if (rbMale.isChecked()) {
            objCustomerMaster.setGender(rbMale.getText().toString());
        }
        if (rbFemale.isChecked()) {
            objCustomerMaster.setGender(rbFemale.getText().toString());
        }
        if (!etDateOfBirth.getText().toString().isEmpty()) {
            try {
                birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etDateOfBirth.getText().toString());
                objCustomerMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (areaMasterId != 0) {
            objCustomerMaster.setLinktoAreaMasterId(areaMasterId);
        }
        if (cityMasterId != 0) {
            objCustomerMaster.setLinktoCityMasterId(cityMasterId);
        }
        objCustomerMaster.setIsEnabled(true);
        objCustomerMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objCustomerMaster.setCustomerType(Globals.CustomerType);
        objCustomerMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);

        objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster, RegistrationActivity.this);
    }

    private void FillCity() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("--------SELECT CITY--------");
        objSpinnerItem.setValue(0);

        ArrayList<SpinnerItem> alSpinnerItem = new ArrayList<>();
        alSpinnerItem.add(objSpinnerItem);
        alCityMaster.addAll(0, alSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(RegistrationActivity.this, alCityMaster, true);
        spCity.setAdapter(adapter);
    }

    private void FillArea() {
        if (alAreaMaster.size() != 0) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText("--------SELECT AREA--------");
            objSpinnerItem.setValue(0);

            ArrayList<SpinnerItem> alSpinnerItem = new ArrayList<>();
            alSpinnerItem.add(objSpinnerItem);
            alAreaMaster.addAll(0, alSpinnerItem);

            SpinnerAdapter adapter = new SpinnerAdapter(RegistrationActivity.this, alAreaMaster, true);
            spArea.setVisibility(View.VISIBLE);
            spArea.setAdapter(adapter);
        } else {
            spArea.setVisibility(View.GONE);
        }
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), RegistrationActivity.this, 1000);
                break;
            case "-2":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgAlreadyExist), RegistrationActivity.this, 1000);
                ClearControls();
                break;
            default:
                Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), RegistrationActivity.this, 1000);

                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerMasterId", errorCode, this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", etEmail.getText().toString(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", etPassword.getText().toString(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", etFirstName.getText().toString() + " " + etLastName.getText().toString(), this);

                ClearControls();
                //Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }

    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (spCity.getSelectedItemId() == 0) {
            Globals.ShowSnackBar(view, "Select City", RegistrationActivity.this, 1000);
            IsValid = false;
        } else if (spArea.getSelectedItemId() == 0) {
            Globals.ShowSnackBar(view, "Select Area", RegistrationActivity.this, 1000);
            IsValid = false;
        }
        if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
                IsValid = false;
            } else {
                etConfirmPassword.clearError();
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            IsValid = false;
        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
                IsValid = false;
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            etFirstName.clearError();
            IsValid = false;
        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            etFirstName.clearError();
        } else if (etPassword.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            etFirstName.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
                IsValid = false;
            } else {
                etConfirmPassword.clearError();
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            IsValid = false;
        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
                IsValid = false;
            } else {
                etConfirmPassword.clearError();
            }
            etFirstName.clearError();
            etPassword.clearError();
        } else if (!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            etPassword.clearError();
            IsValid = false;
        }
        if (!etPhone.getText().toString().equals("") && etPhone.getText().length() != 10) {
            etPhone.setError("Enter 10 digit " + getResources().getString(R.string.suPhone));
            IsValid = false;
        } else {
            etPhone.clearError();
        }
        return IsValid;
    }

    private void ClearControls() {
        etFirstName.setText("");
        etLastName.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }
    //endregion
}
