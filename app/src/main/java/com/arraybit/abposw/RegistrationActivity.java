package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
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
import com.arraybit.parser.StateJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerJSONParser.CustomerRequestListener {

    ArrayList<SpinnerItem> alCountryMaster, alStateMaster, alCityMaster, alAreaMaster;
    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone, etDateOfBirth;
    TextView txtCountryError, txtStateError, txtCityError, txtAreaError;
    AppCompatSpinner spCountry, spState, spCity, spArea;
    short countryMasterId, stateMasterId, cityMasterId, areaMasterId;
    Date birthDate;
    View view;
    LinearLayout cityAreaLayout;
    RadioButton rbMale, rbFemale;
    ProgressDialog progressDialog = new ProgressDialog();

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

        txtCountryError = (TextView) findViewById(R.id.txtCountryError);
        txtStateError = (TextView) findViewById(R.id.txtStateError);
        txtCityError = (TextView) findViewById(R.id.txtCityError);
        txtAreaError = (TextView) findViewById(R.id.txtAreaError);

        //hide keyboard
        etDateOfBirth.setInputType(InputType.TYPE_NULL);

        spCountry = (AppCompatSpinner) findViewById(R.id.spCountry);
        spState = (AppCompatSpinner) findViewById(R.id.spState);
        spCity = (AppCompatSpinner) findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) findViewById(R.id.spArea);

        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);

        cityAreaLayout = (LinearLayout)findViewById(R.id.cityAreaLayout);

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);

        CompoundButton cbPrivacyPolicy = (CompoundButton) findViewById(R.id.cbPrivacyPolicy);
        CompoundButton cbTermsofService = (CompoundButton) findViewById(R.id.cbTermsofService);

        cbPrivacyPolicy.setOnClickListener(this);
        cbTermsofService.setOnClickListener(this);

        alCountryMaster = new ArrayList<>();
        countryMasterId = 1;
        FillCountry();
        if (Service.CheckNet(this)) {
            RequestStateMaster();
        } else {
            Globals.ShowSnackBar(registrationLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        btnSignUp.setOnClickListener(this);
        cbSignIn.setOnClickListener(this);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v = parent.getAdapter().getView(position, view, parent);
                stateMasterId = (short) v.getId();
                if (stateMasterId == 0) {
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
                View v = parent.getAdapter().getView(position, view, parent);
                cityMasterId = (short) v.getId();
                if (cityMasterId == 0) {
                    spArea.setVisibility(View.INVISIBLE);
                    txtAreaError.setVisibility(View.INVISIBLE);
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
                    Globals.ShowDatePickerDialog(etDateOfBirth, RegistrationActivity.this, false);
                }
            }
        });
    }

    public void EditTextOnClick(View view) {
        Globals.ShowDatePickerDialog(etDateOfBirth, RegistrationActivity.this, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), RegistrationActivity.this, 1000);
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
            Globals.ReplaceFragment(new PolicyFragment("Privacy Policy"), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_policy), R.id.registrationLayout);
        } else if (v.getId() == R.id.cbTermsofService) {
            Globals.ReplaceFragment(new PolicyFragment("Terms of service"), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_terms_of_service), R.id.registrationLayout);
        }
    }

    @Override
    public void StateResponse(ArrayList<SpinnerItem> alStateMaster) {
        progressDialog.dismiss();
        this.alStateMaster = alStateMaster;
        if (alStateMaster.size() > 0) {
            FillState();
        } else {
            spState.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        progressDialog.dismiss();
        this.alCityMaster = alCityMaster;
        if (alCityMaster.size() > 0) {
            FillCity();
        } else {
            spCity.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        if (alAreaMaster.size() > 0) {
            FillArea();
        } else {
            spArea.setVisibility(View.INVISIBLE);
        }
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
    private void RequestStateMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        StateJSONParser objStateJSONParser = new StateJSONParser();
        objStateJSONParser.SelectStateMaster(null, this, String.valueOf(countryMasterId));
    }

    private void RequestCityMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        CityJSONParser objCityJSONParser = new CityJSONParser();
        objCityJSONParser.SelectAllCityMasterByState(null, this, String.valueOf(stateMasterId));
    }

    private void RequestAreaMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(null, this, String.valueOf(cityMasterId));
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
        if (countryMasterId != 0) {
            objCustomerMaster.setLinktoCountryMasterId(countryMasterId);
        }
        if (stateMasterId != 0) {
            objCustomerMaster.setLinktoStateMasterId(stateMasterId);
        }
        if (cityMasterId != 0) {
            objCustomerMaster.setLinktoCityMasterId(cityMasterId);
        }
        if (areaMasterId != 0) {
            objCustomerMaster.setLinktoAreaMasterId(areaMasterId);
        }
        objCustomerMaster.setIsEnabled(true);
        objCustomerMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objCustomerMaster.setCustomerType(Globals.CustomerType);
        objCustomerMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);

        objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster, RegistrationActivity.this);
    }

    private void FillCountry() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("India");
        objSpinnerItem.setValue(1);
        alCountryMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(RegistrationActivity.this, alCountryMaster, true);
        spCountry.setAdapter(adapter);
    }

    private void FillState() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getResources().getString(R.string.suSelect));
        objSpinnerItem.setValue(0);

        alStateMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(RegistrationActivity.this, alStateMaster, true);
        spState.setVisibility(View.VISIBLE);
        spState.setAdapter(adapter);
    }

    private void FillCity() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getResources().getString(R.string.suSelect));
        objSpinnerItem.setValue(0);

        alCityMaster.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(RegistrationActivity.this, alCityMaster, true);
        spCity.setVisibility(View.VISIBLE);
        spCity.setAdapter(adapter);
    }

    private void FillArea() {
        if (alAreaMaster.size() != 0) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.suSelect));
            objSpinnerItem.setValue(0);

            alAreaMaster.add(0, objSpinnerItem);

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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IsLogin", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }

    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etConfirmPassword.setError("Enter "+getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etPassword.clearError();
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter" + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etFirstName.clearError();
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            etFirstName.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etConfirmPassword.setError("Enter " +getResources().getString(R.string.suConfirmPassword));
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() == 0) {
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.VISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")

                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.clearError();
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.clearError();
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.suValidConfirmPassword));
            } else {
                etConfirmPassword.clearError();
            }
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && !etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() != 0) {
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
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
        }else if(!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId()!=0
                && spCity.getSelectedItemId()==0){
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
            etConfirmPassword.setError("Enter "+getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
        }else if(!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId()!=0
                && spCity.getSelectedItemId()!=0
                && spArea.getSelectedItemId()==0){
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
            etConfirmPassword.setError("Enter "+getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
        }else if(etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId()!=0
                && spCity.getSelectedItemId()==0){
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etConfirmPassword.setError("Enter "+getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
        }else if(etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId()!=0
                && spCity.getSelectedItemId()!=0
                && spArea.getSelectedItemId()==0){
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etConfirmPassword.setError("Enter "+getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
        }
        if (!etPhone.getText().toString().equals("")
                && etPhone.getText().length() != 10) {
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
