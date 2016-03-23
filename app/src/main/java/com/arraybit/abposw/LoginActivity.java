package com.arraybit.abposw;

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
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RegisteredUserJSONParser.RegisteredUserRequestListener {

    EditText etUserName, etPassword;
    RegisteredUserMaster objRegisteredUserMaster;
    View view;
    ImageView ibClear;
    ToggleButton tbPasswordShow;
    com.arraybit.abposw.ProgressDialog progressDialog;
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            startActivity(intent);
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
    public void RegisteredUserResponse(String errorCode, RegisteredUserMaster objRegisteredUserMaster) {
        progressDialog.dismiss();
        this.objRegisteredUserMaster = objRegisteredUserMaster;
        SetError();
    }

    //region Private Methods
    private void LoginRequest() {
        progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        RegisteredUserJSONParser objRegisteredUserJSONParser = new RegisteredUserJSONParser();
        objRegisteredUserJSONParser.SelectRegisteredUserMasterUserName(LoginActivity.this, etUserName.getText().toString(), etPassword.getText().toString());
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
        if (objRegisteredUserMaster == null) {
            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), LoginActivity.this, 1000);
            ClearControls();
        } else {

            objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.CreatePreference("LoginPreference", "RegisteredUserMasterId", String.valueOf(objRegisteredUserMaster.getRegisteredUserMasterId()), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objRegisteredUserMaster.getEmail(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objRegisteredUserMaster.getPassword(), this);

            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), LoginActivity.this, 2000);
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("RegisteredUserMaster", objRegisteredUserMaster);
            startActivity(i);
        }
    }
    //endregion
}
