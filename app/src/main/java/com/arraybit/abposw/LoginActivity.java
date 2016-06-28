package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, CustomerJSONParser.CustomerRequestListener {

    private static final int RC_SIGN_IN = 22;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 113;
    EditText etUserName, etPassword;
    CustomerMaster objCustomerMaster;
    View view;
    ImageView ibClear;
    ToggleButton tbPasswordShow;
    ProgressDialog progressDialog;
    SharePreferenceManage objSharePreferenceManage;
    Toolbar app_bar;
    Button btnLoginWithGPlus, btnLoginWithFb;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean signedInUser, isIntegrationLogin,isLoginWithFb,isLoginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
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

        btnLoginWithGPlus = (Button) findViewById(R.id.btnLoginWithGPlus);
        btnLoginWithFb = (Button) findViewById(R.id.btnLoginWithFb);

        ibClear = (ImageView) findViewById(R.id.ibClear);
        tbPasswordShow = (ToggleButton) findViewById(R.id.tbPasswordShow);

        btnSignIn.setOnClickListener(this);
        cbSignUp.setOnClickListener(this);
        btnLoginWithGPlus.setOnClickListener(this);
        btnLoginWithFb.setOnClickListener(this);
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
        Globals.HideKeyBoard(LoginActivity.this, app_bar);
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
                    isIntegrationLogin = false;
                    LoginRequest();
                } else {
                    Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
            Globals.HideKeyBoard(this, view);
        } else if (v.getId() == R.id.cbSignUp) {
            Globals.HideKeyBoard(this, view);
            if (getIntent().getStringExtra("Booking") != null && getIntent().getStringExtra("Booking").equals("Booking")) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra("Booking", "Booking");
                startActivityForResult(intent, 123);
            } else if (getIntent().getStringExtra("Order") != null && getIntent().getStringExtra("Order").equals("Order")) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra("Order", "Order");
                startActivityForResult(intent, 123);
            } else {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent, 0);
            }
        } else if (v.getId() == R.id.ibClear) {
            etUserName.setText("");
            ibClear.setVisibility(View.GONE);
        } else if (v.getId() == R.id.tbPasswordShow) {
            if (tbPasswordShow.isChecked()) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        } else if (v.getId() == R.id.btnLoginWithGPlus) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
            mGoogleApiClient.connect();
            if (!mGoogleApiClient.isConnecting()) {
                ResolveSignInError();
            }
        } else if (v.getId() == R.id.btnLoginWithFb) {
            final LoginButton loginButton = new LoginButton(this);
            loginButton.setReadPermissions("public_profile", "email", "user_birthday", "user_location", "user_friends");
            loginButton.performClick();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    // Application code
                                    try {
                                    Toast.makeText(LoginActivity.this, "get Info", Toast.LENGTH_LONG).show();
                                        isIntegrationLogin = true;
                                        CustomerMaster objCustomerMaster = new CustomerMaster();
                                        if(object!=null) {
                                            if (object.optString("name") != null && !object.optString("name").equals("")) {
                                                objCustomerMaster.setCustomerName(object.optString("name"));
                                            }
                                            if (object.optString("email") != null && !object.optString("email").equals("")) {
                                                objCustomerMaster.setEmail1(object.optString("email"));
                                            }
                                            if (object.optString("first_name") != null && !object.optString("first_name").equals("")) {
                                                objCustomerMaster.setShortName(object.optString("first_name"));
                                            } else {
                                                objCustomerMaster.setShortName(object.optString("name"));
                                            }
                                            if (object.optString("gender") != null && !object.optString("gender").equals("")) {
                                                objCustomerMaster.setGender(object.optString("gender"));
                                            }
                                            if (object.optString("id") != null && !object.optString("id").equals("")) {
                                                objCustomerMaster.setFacebookUserId(object.optString("id"));
                                                objCustomerMaster.setImageName("https://graph.facebook.com/" + object.optString("id") + "/picture?type=large");
                                            }
                                            objCustomerMaster.setIsVerified(object.optBoolean("verified"));
                                            if (object.optString("birthday") != null && !object.optString("birthday").equals("")) {
                                                Date birthDate = new SimpleDateFormat("MM/DD/yyyy", Locale.US).parse(object.optString("birthday"));
                                                objCustomerMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
                                            }
                                            JSONObject ageObject = object.getJSONObject("age_range");
                                            if (ageObject != null) {
                                                if (ageObject.optString("min") != null && !ageObject.optString("min").equals("")) {
                                                    objCustomerMaster.setAgeMinRange(Integer.parseInt(ageObject.optString("min")));
                                                }
                                                if (ageObject.optString("max") != null && !ageObject.optString("max").equals("")) {
                                                    objCustomerMaster.setAgeMaxRange(Integer.parseInt(ageObject.optString("max")));
                                                }
                                            }
                                            objCustomerMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
                                            objCustomerMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);
                                            objCustomerMaster.setCustomerType(Globals.CustomerType);
                                            objCustomerMaster.setLinktoCountryMasterId((short) 0);
                                            objCustomerMaster.setPhone1("");
                                            Toast.makeText(LoginActivity.this, "Request For Insert", Toast.LENGTH_LONG).show();
                                            isLoginWithFb = true;
                                            RequestInsertCustomerMaster(objCustomerMaster);
                                        }

                                    } catch (JSONException e) {
                                        System.out.println("Error Message "+e.getMessage());
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "name,email,first_name,gender,id,verified,birthday,age_range");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
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
            } else if (requestCode == 123) {
                if (data.getBooleanExtra("IsRedirect", false)) {
                    if (data.getStringExtra("TargetActivity") != null && data.getStringExtra("TargetActivity").equals("Booking")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("IsRedirect", true);
                        returnIntent.putExtra("TargetActivity", "Booking");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (data.getStringExtra("TargetActivity") != null && data.getStringExtra("TargetActivity").equals("Order")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("IsRedirect", true);
                        returnIntent.putExtra("TargetActivity", "Order");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
            } else if (requestCode == RC_SIGN_IN) {
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                GetGooglePlusProfileInformation();
            }else {
                requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        } else {
            GetGooglePlusProfileInformation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            // store mConnectionResult
            mConnectionResult = connectionResult;
            if (signedInUser) {
                ResolveSignInError();
            }
        }
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        if (isIntegrationLogin) {
            this.objCustomerMaster = objCustomerMaster;
            SetError();
        } else {
            this.objCustomerMaster = objCustomerMaster;
            SetError();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    GetGooglePlusProfileInformation();
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //region Private Methods
    private void LoginRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.SelectCustomerMaster(LoginActivity.this, etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), null, null, String.valueOf(Globals.linktoBusinessMasterId));
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
            isLoginWithFb = false;
            Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), LoginActivity.this, 1000);
        } else {
            if (objCustomerMaster.getErrorCode() != null && !objCustomerMaster.getErrorCode().equals("")) {
                if (objCustomerMaster.getErrorCode().equals("0")) {
                    isLoginSuccess = true;
                } else if (objCustomerMaster.getErrorCode().equals("-2")) {
                     if(isIntegrationLogin) {
                        isLoginSuccess = true;
                    }else{
                        isLoginSuccess = false;
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), LoginActivity.this, 1000);
                    }
                }
            } else {
                isLoginSuccess = false;
            }
            if (isLoginSuccess) {
                ClearControls();
                objSharePreferenceManage = new SharePreferenceManage();
                if (isIntegrationLogin) {
                    if (objCustomerMaster.getGooglePlusUserId() != null && !objCustomerMaster.getGooglePlusUserId().equals("")) {
                        objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objCustomerMaster.getGooglePlusUserId(), this);
                    } else if (objCustomerMaster.getFacebookUserId() != null && !objCustomerMaster.getFacebookUserId().equals("")) {
                        objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objCustomerMaster.getFacebookUserId(), this);
                    }
                }
                if (isLoginWithFb) {
                    objSharePreferenceManage.CreatePreference("LoginPreference", "isLoginWithFb", "true", this);
                }
                objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerMasterId", String.valueOf(objCustomerMaster.getCustomerMasterId()), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objCustomerMaster.getEmail1(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objCustomerMaster.getPassword(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", objCustomerMaster.getCustomerName(), this);
                objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objCustomerMaster.getlinktoBusinessMasterId()), this);
                if (objCustomerMaster.getXs_ImagePhysicalName() != null && !objCustomerMaster.getXs_ImagePhysicalName().equals("")) {
                    objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objCustomerMaster.getXs_ImagePhysicalName(), this);
                }
                if (objCustomerMaster.getPhone1() != null && !objCustomerMaster.getPhone1().equals("")) {
                    objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), this);
                }
                if (getIntent().getStringExtra("Booking") != null && getIntent().getStringExtra("Booking").equals("Booking")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsRedirect", true);
                    returnIntent.putExtra("TargetActivity", "Booking");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (getIntent().getStringExtra("Order") != null && getIntent().getStringExtra("Order").equals("Order")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsRedirect", true);
                    returnIntent.putExtra("TargetActivity", "Order");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsLogin", true);
                    returnIntent.putExtra("IsShowMessage", true);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }
    }

    private void ResolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void RequestInsertCustomerMaster(CustomerMaster objCustomerMaster) {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster, LoginActivity.this);
    }

    private void GetGooglePlusProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                isIntegrationLogin = true;
                Toast.makeText(this, "Profile Info", Toast.LENGTH_LONG).show();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                CustomerMaster objCustomerMaster = new CustomerMaster();
                objCustomerMaster.setCustomerName(currentPerson.getDisplayName());
                objCustomerMaster.setEmail1(Plus.AccountApi.getAccountName(mGoogleApiClient));
                if (currentPerson.getNickname() != null && !currentPerson.getNickname().equals("")) {
                    objCustomerMaster.setShortName(currentPerson.getNickname());
                } else {
                    if (currentPerson.getName().hasGivenName()) {
                        objCustomerMaster.setShortName(currentPerson.getName().getGivenName());
                    } else {
                        objCustomerMaster.setShortName(currentPerson.getDisplayName());
                    }
                }
                if (currentPerson.getGender() == Person.Gender.MALE) {
                    objCustomerMaster.setGender("Male");
                } else if (currentPerson.getGender() == Person.Gender.FEMALE) {
                    objCustomerMaster.setGender("Female");
                }
                objCustomerMaster.setGooglePlusUserId(currentPerson.getId());
                objCustomerMaster.setIsVerified(currentPerson.isVerified());
                objCustomerMaster.setPhone1("");
                if (currentPerson.hasAgeRange()) {
                    objCustomerMaster.setAgeMinRange(currentPerson.getAgeRange().getMin());
                    objCustomerMaster.setAgeMaxRange(currentPerson.getAgeRange().getMax());
                }
                if (currentPerson.getBirthday() != null && currentPerson.getBirthday().equals("")) {
                    Date birthDate = new SimpleDateFormat("yyyy-MM-DD", Locale.US).parse(objCustomerMaster.getBirthDate());
                    objCustomerMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
                }
                if (currentPerson.getImage().hasUrl()) {
                    objCustomerMaster.setImageName(currentPerson.getImage().getUrl());
                }
                objCustomerMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
                objCustomerMaster.setlinktoSourceMasterId(Globals.linktoSourceMasterId);
                objCustomerMaster.setCustomerType(Globals.CustomerType);
                objCustomerMaster.setLinktoCountryMasterId((short) 0);
                Toast.makeText(this, "Request For Insert", Toast.LENGTH_LONG).show();
                RequestInsertCustomerMaster(objCustomerMaster);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //endregion
}
