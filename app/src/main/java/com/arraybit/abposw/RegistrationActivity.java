package com.arraybit.abposw;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.AreaJSONParser;
import com.arraybit.parser.CityJSONParser;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, RegisteredUserJSONParser.RegisteredUserRequestListener {

    ArrayList<SpinnerItem> alCityMaster, alAreaMaster;
    EditText etFirstName, etLastName, etEmail, etPassword, etPhone, etDateOfBirth;
    AppCompatSpinner spArea, spCity;
    short cityMasterId, areaMasterId;
    Date birthDate;
    View view;
    RadioButton rbMale, rbFemale;
    com.arraybit.abposw.ProgressDialog progressDialog;

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

        LinearLayout registrationLayout = (LinearLayout) findViewById(R.id.registrationLayout);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
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
            finish();
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
                } else{
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
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
    public void RegisteredUserResponse(String errorCode, RegisteredUserMaster objRegisteredUserMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    //region Private Methods
    private void RequestCityMaster() {
        progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        CityJSONParser objCityJSONParser = new CityJSONParser();
        objCityJSONParser.SelectAllCityMasterByState(RegistrationActivity.this, String.valueOf(Globals.linktoStateMasterId));
    }

    private void RequestAreaMaster() {
        progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        AreaJSONParser objAreaJSONParser = new AreaJSONParser();
        objAreaJSONParser.SelectAllAreaMasterAreaByCity(RegistrationActivity.this, String.valueOf(cityMasterId));
    }

    private void RegistrationRequest() {
        progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        RegisteredUserJSONParser objRegisteredUserJSONParser = new RegisteredUserJSONParser();

        RegisteredUserMaster objRegisteredUserMaster = new RegisteredUserMaster();
        objRegisteredUserMaster.setFirstName(etFirstName.getText().toString());
        objRegisteredUserMaster.setLastName(etLastName.getText().toString());
        objRegisteredUserMaster.setEmail(etEmail.getText().toString());
        objRegisteredUserMaster.setPassword(etPassword.getText().toString());
        objRegisteredUserMaster.setPhone(etPhone.getText().toString());
        if (rbMale.isChecked()) {
            objRegisteredUserMaster.setGender(rbMale.getText().toString());
        }
        if (rbFemale.isChecked()) {
            objRegisteredUserMaster.setGender(rbFemale.getText().toString());
        }
        if (!etDateOfBirth.getText().toString().isEmpty()) {
            try {
                birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etDateOfBirth.getText().toString());
                objRegisteredUserMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (areaMasterId != 0) {
            objRegisteredUserMaster.setlinktoAreaMasterId(areaMasterId);
        }
        if (cityMasterId != 0) {
            objRegisteredUserMaster.setlinktoCityMasterId(cityMasterId);
        }
        objRegisteredUserMaster.setIsEnabled(true);
        objRegisteredUserMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objRegisteredUserMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);

        objRegisteredUserJSONParser.InsertRegisteredUserMaster(objRegisteredUserMaster, RegistrationActivity.this);
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
                ClearControls();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
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
                && !etPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.clearError();
            IsValid = false;

        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etFirstName.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")) {
            etPassword.clearError();
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
            }
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            IsValid = false;
        } else if (etEmail.getText().toString().equals("")
                && !etFirstName.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etFirstName.clearError();
            IsValid = false;
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && etPassword.getText().toString().equals("")) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.setError("Enter " + getResources().getString(R.string.suPassword));
            IsValid = false;
        } else if (!etFirstName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
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

//    private void RequestCityMaster() {
//        final com.arraybit.webpos.ProgressDialog progressDialog = new com.arraybit.webpos.ProgressDialog();
//        progressDialog.show(getSupportFragmentManager(), "");
//        final CityJSONParser objCityJSONParser = new CityJSONParser();
//        String url = Service.Url + objCityJSONParser.SelectAllCityMasterByState + "/" + Globals.linktoStateMasterId;
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                alCityMaster = objCityJSONParser.SelectAllCityMasterByState(jsonObject);
//                FillCity();
//                progressDialog.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                progressDialog.dismiss();
//            }
//        });
//        queue.add(jsonObjectRequest);
//    }

//    private void RequestAreaMaster() {
//        final com.arraybit.webpos.ProgressDialog progressDialog = new com.arraybit.webpos.ProgressDialog();
//        progressDialog.show(getSupportFragmentManager(), "");
//        final AreaJSONParser objAreaJSONParser = new AreaJSONParser();
//        String url = Service.Url + objAreaJSONParser.SelectAllAreaMasterByCity + "/" + cityMasterId;
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                alAreaMaster = objAreaJSONParser.SelectAllAreaMasterAreaByCity(jsonObject);
//                FillArea();
//                progressDialog.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                progressDialog.dismiss();
//            }
//        });
//        queue.add(jsonObjectRequest);
//    }

//    private void RegistrationRequest() {
//        final com.arraybit.webpos.ProgressDialog progressDialog = new com.arraybit.webpos.ProgressDialog();
//        progressDialog.show(getSupportFragmentManager(), "");
//
//        final RegisteredUserJSONParser objRegisteredUserJSONParser = new RegisteredUserJSONParser();
//        String url = Service.Url + objRegisteredUserJSONParser.InsertRegisteredUserMaster;
//
//        RegisteredUserMaster objRegisteredUserMaster = new RegisteredUserMaster();
//        objRegisteredUserMaster.setFirstName(etFirstName.getText().toString());
//        objRegisteredUserMaster.setLastName(etLastName.getText().toString());
//        objRegisteredUserMaster.setEmail(etEmail.getText().toString());
//        objRegisteredUserMaster.setPassword(etPassword.getText().toString());
//        objRegisteredUserMaster.setPhone(etPhone.getText().toString());
//        if (rbMale.isChecked()) {
//            objRegisteredUserMaster.setGender(rbMale.getText().toString());
//        }
//        if (rbFemale.isChecked()) {
//            objRegisteredUserMaster.setGender(rbFemale.getText().toString());
//        }
//        if (!etDateOfBirth.getText().toString().isEmpty()) {
//            try {
//                birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etDateOfBirth.getText().toString());
//                objRegisteredUserMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        if(areaMasterId!=0){
//            objRegisteredUserMaster.setlinktoAreaMasterId(areaMasterId);
//        }
//        if(cityMasterId!=0){
//            objRegisteredUserMaster.setlinktoCityMasterId(cityMasterId);
//        }
//        objRegisteredUserMaster.setIsEnabled(true);
//        objRegisteredUserMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
//        objRegisteredUserMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);
//
//        JSONStringer stringer = objRegisteredUserJSONParser.InsertRegisteredUserMaster(objRegisteredUserMaster);
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = null;
//        try {
//            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject jsonObject) {
//                    try {
//                        JSONObject jsonResponse = jsonObject.getJSONObject(objRegisteredUserJSONParser.InsertRegisteredUserMaster + "Result");
//                        errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
//                        SetError(errorCode);
//                    } catch (JSONException e) {
//                        errorCode = "-1";
//                    }
//                    progressDialog.dismiss();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    progressDialog.dismiss();
//                    errorCode = "-1";
//                    SetError(errorCode);
//                }
//            });
//        } catch (JSONException e) {
//            errorCode = "-1";
//            SetError(errorCode);
//        }
//        queue.add(jsonObjectRequest);
//    }

    //endregion
}
