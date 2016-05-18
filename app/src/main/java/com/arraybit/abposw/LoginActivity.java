package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CustomerJSONParser.CustomerRequestListener {

    EditText etUserName, etPassword;
    CustomerMaster objCustomerMaster;
    View view;
    ImageView ibClear;
    ToggleButton tbPasswordShow;
    ProgressDialog progressDialog;
    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        CompoundButton cbSignUp = (CompoundButton) findViewById(R.id.cbSignUp);
        ibClear = (ImageView) findViewById(R.id.ibClear);
        tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);

        btnSignIn.setOnClickListener(this);
        cbSignUp.setOnClickListener(this);
        ibClear.setOnClickListener(this);
        tbPasswordShow.setOnClickListener(this);

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ibClear.setVisibility(View.VISIBLE);
                if (etUserName.getText().toString().equals("")) {
                    ibClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tbPasswordShow.setVisibility(View.VISIBLE);
                if (etPassword.getText().toString().equals("")) {
                    tbPasswordShow.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.btnSignIn) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), LoginActivity.this, 1000);
            } else {
                if (Service.CheckNet(this)) {
                    LoginRequest();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
            Globals.HideKeyBoard(this, view);
        } else if (v.getId() == R.id.cbSignUp) {
            Globals.HideKeyBoard(this, view);
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.ibClear) {
            etUserName.setText("");
            ibClear.setVisibility(View.GONE);
        } else if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsLogin", true);
                    returnIntent.putExtra("IsShowMessage", true);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        this.objCustomerMaster = objCustomerMaster;
        SetError();
    }

    //region Private Methods
    private void LoginRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.SelectCustomerMaster(LoginActivity.this, etUserName.getText().toString(), etPassword.getText().toString(), null, null, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private boolean ValidateControls() {
        boolean IsValid = true;

        if (etUserName.getText().toString().equals("") && etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            IsValid = false;
        } else if (etUserName.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
            etUserName.setError("Enter " + getResources().getString(R.string.siUserName));
            etPassword.clearError();
            IsValid = false;
        } else if (etPassword.getText().toString().equals("") && !etUserName.getText().toString().equals("")) {
            etPassword.setError("Enter " + getResources().getString(R.string.siPassword));
            etUserName.clearError();
            IsValid = false;
        } else {
            etUserName.clearError();
            etPassword.clearError();
        }

        return IsValid;
    }

    private void ClearControls() {
        etUserName.setText("");
        etPassword.setText("");
    }

    private void SetError() {
        if (objCustomerMaster == null) {
            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), LoginActivity.this, 1000);
        } else {
            ClearControls();
            objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerMasterId", String.valueOf(objCustomerMaster.getCustomerMasterId()), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objCustomerMaster.getEmail1(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objCustomerMaster.getPassword(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", objCustomerMaster.getCustomerName(), this);
            if(objCustomerMaster.getPhone1()!=null && !objCustomerMaster.getPhone1().equals("")) {
                objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), this);
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("IsLogin", true);
            returnIntent.putExtra("IsShowMessage", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
    //endregion
}
