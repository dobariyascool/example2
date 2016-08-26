package com.arraybit.abposw;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings({"ConstantConditions", "UnnecessaryReturnStatement"})
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, StateJSONParser.StateRequestListener, CityJSONParser.CityRequestListener, AreaJSONParser.AreaRequestListener, CustomerJSONParser.CustomerRequestListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int requestCodeForSignIn = 22;
    final private int requestCodeForPermission = 113;
    final private int accountPickerRequest = 114;
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
    ImageView ivTakeImage;
    String imagePhysicalNameBytes, imageName,strImageName;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);
    Button btnLoginWithGPlus, btnLoginWithFb;
    CallbackManager callbackManager;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean signedInUser, isIntegrationLogin,isLoginWithFb;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_registration);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        GCMTokenRegistration();

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

        ivTakeImage = (ImageView) findViewById(R.id.ivTakeImage);

        //hide keyboard
        etDateOfBirth.setInputType(InputType.TYPE_NULL);

        spCountry = (AppCompatSpinner) findViewById(R.id.spCountry);
        spState = (AppCompatSpinner) findViewById(R.id.spState);
        spCity = (AppCompatSpinner) findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) findViewById(R.id.spArea);

        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);

        cityAreaLayout = (LinearLayout) findViewById(R.id.cityAreaLayout);

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);

        btnLoginWithGPlus = (Button) findViewById(R.id.btnLoginWithGPlus);
        btnLoginWithFb = (Button) findViewById(R.id.btnLoginWithFb);

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
        ivTakeImage.setOnClickListener(this);
        btnLoginWithGPlus.setOnClickListener(this);
        btnLoginWithFb.setOnClickListener(this);

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

        etPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(RegistrationActivity.this, v);
                }
                return false;
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
        Globals.HideKeyBoard(RegistrationActivity.this, v);
        if (v.getId() == R.id.btnSignUp) {
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), RegistrationActivity.this, 1000);
            } else {
                if (Service.CheckNet(this)) {
                    isIntegrationLogin = false;
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
        } else if (v.getId() == R.id.ivTakeImage) {
            Globals.SelectImage(RegistrationActivity.this, 100, 101);
        }else if (v.getId() == R.id.btnLoginWithGPlus) {
            if (Service.CheckNet(this)) {
                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                        false, null, null, null, null);
                startActivityForResult(intent, accountPickerRequest);
            }else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
            }
        } else if (v.getId() == R.id.btnLoginWithFb) {
            if (Service.CheckNet(this)) {
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
                                            isIntegrationLogin = true;
                                            CustomerMaster objCustomerMaster = new CustomerMaster();
                                            if (object != null) {
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
                                                objCustomerMaster.setIsEnabled(true);
                                                objCustomerMaster.setPhone1("");
                                                isLoginWithFb = true;
//                                                GCMTokenRegistration();
                                                objCustomerMaster.setGCMToken(token.replace(":", "2E2").replace("-", "3E3").replace("_", "4E4"));
                                                RequestInsertCustomerMaster(objCustomerMaster,false);
                                            }
                                        }  catch (Exception e) {
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
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), RegistrationActivity.this, 1000);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), RegistrationActivity.this, 1000);
                    }
                });
            }else {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgCheckConnection), this, 1000);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                GetGooglePlusProfileInformation();
            }else {
                requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        requestCodeForPermission);
                return;
            }
        } else {
            GetGooglePlusProfileInformation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginFailedMsg), RegistrationActivity.this, 1000);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Globals.ShowSnackBar(view, getResources().getString(R.string.siIntegrationLoginMsg), RegistrationActivity.this, 1000);
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
    public void StateResponse(ArrayList<SpinnerItem> alStateMaster) {
        progressDialog.dismiss();
        this.alStateMaster = alStateMaster;
        if (alStateMaster != null && alStateMaster.size() > 0) {
            FillState();
        } else {
            spState.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case requestCodeForPermission:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    GetGooglePlusProfileInformation();
                } else {
                    // Permission Denied
                    Toast.makeText(RegistrationActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                String picturePath = "";
                if (requestCode == 100) {
                    strImageName =  "CameraImage_"+ simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length())+".jpg";
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "CameraImage.jpg");
                    picturePath = f.getAbsolutePath();
                    imageName = f.getName();
                } else if (requestCode == 101 && data != null && data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    File file = new File(picturePath);
                    imageName = file.getName();
                    cursor.close();
                }
                if (!picturePath.equals("")) {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                    ivTakeImage.setImageBitmap(bitmap);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bytedata = bos.toByteArray();
                    imagePhysicalNameBytes = Base64.encodeToString(bytedata, Base64.DEFAULT);
                    return;
                }
                if (requestCode == requestCodeForSignIn) {
                    mIntentInProgress = false;
                    if (!mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    }
                }else if (requestCode == accountPickerRequest) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if(accountName!=null && !accountName.equals("")) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).setAccountName(accountName).build();
                        mGoogleApiClient.connect();
                        if (!mGoogleApiClient.isConnecting()) {
                            ResolveSignInError();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void CityResponse(ArrayList<SpinnerItem> alCityMaster) {
        progressDialog.dismiss();
        this.alCityMaster = alCityMaster;
        if (alCityMaster != null && alCityMaster.size() > 0) {
            FillCity();
        } else {
            spCity.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void AreaResponse(ArrayList<SpinnerItem> alAreaMaster) {
        progressDialog.dismiss();
        this.alAreaMaster = alAreaMaster;
        if (alAreaMaster != null && alAreaMaster.size() > 0) {
            FillArea();
        } else {
            spArea.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        SetError(errorCode, objCustomerMaster);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    //region Private Methods
    private void RequestStateMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        StateJSONParser objStateJSONParser = new StateJSONParser();
        objStateJSONParser.SelectAllStateMaster(null, this, String.valueOf(countryMasterId));
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

        try {

            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
            CustomerMaster objCustomerMaster = new CustomerMaster();
            objCustomerMaster.setCustomerName(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            objCustomerMaster.setEmail1(etEmail.getText().toString().trim());
            objCustomerMaster.setPassword(etPassword.getText().toString().trim());
            objCustomerMaster.setConfirmPassword(etConfirmPassword.getText().toString().trim());
            objCustomerMaster.setPhone1(etPhone.getText().toString().trim());
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
            if (imageName != null && !imageName.equals("")) {
                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                objCustomerMaster.setImageName(strImageName);
                objCustomerMaster.setImageNamePhysicalNameBytes(imagePhysicalNameBytes);
            }
//        GCMTokenRegistration();
            Log.e("Registrartion"," encoded token:"+token.replace(":", "2E2").replace("-","3E3").replace("_","4E4"));
            objCustomerMaster.setGCMToken(token.replace(":", "2E2").replace("-","3E3").replace("_","4E4"));
            objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster,false, RegistrationActivity.this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    private void SetError(String errorCode, CustomerMaster objCustomerMaster) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), RegistrationActivity.this, 1000);
                break;
            case "-2":
                if(isIntegrationLogin){
                    if (objCustomerMaster != null) {
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
                        if (objCustomerMaster.getXs_ImagePhysicalName() != null && !objCustomerMaster.getXs_ImagePhysicalName().equals("")) {
                            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objCustomerMaster.getXs_ImagePhysicalName(), this);
                        }
                        if (objCustomerMaster.getPhone1() != null && !objCustomerMaster.getPhone1().equals("")) {
                            objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), this);
                        }
                        objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objCustomerMaster.getlinktoBusinessMasterId()), this);
                    }
                    ClearControls();
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
                }else{
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgAlreadyExist), RegistrationActivity.this, 1000);
                    ClearControls();
                }
                break;
            default:
                if (objCustomerMaster != null) {
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
                    if (objCustomerMaster.getXs_ImagePhysicalName() != null && !objCustomerMaster.getXs_ImagePhysicalName().equals("")) {
                        objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objCustomerMaster.getXs_ImagePhysicalName(), this);
                    }
                    if (objCustomerMaster.getPhone1() != null && !objCustomerMaster.getPhone1().equals("")) {
                        objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), this);
                    }
                    objSharePreferenceManage.CreatePreference("LoginPreference", "BusinessMasterId", String.valueOf(objCustomerMaster.getlinktoBusinessMasterId()), this);
                }
                ClearControls();
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
        } else if (!etFirstName.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            if (Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.clearError();
            } else {
                etEmail.setError("Enter " + getResources().getString(R.string.suValidEmail));
                IsValid = false;
            }
            etFirstName.clearError();
            etPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.INVISIBLE);
            txtAreaError.setVisibility(View.VISIBLE);
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
            txtCountryError.setVisibility(View.INVISIBLE);
            txtStateError.setVisibility(View.INVISIBLE);
            txtCityError.setVisibility(View.VISIBLE);
            txtAreaError.setVisibility(View.INVISIBLE);
        } else if (etFirstName.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("")
                && etConfirmPassword.getText().toString().equals("")
                && spState.getSelectedItemId() != 0
                && spCity.getSelectedItemId() != 0
                && spArea.getSelectedItemId() == 0) {
            etFirstName.setError("Enter " + getResources().getString(R.string.suFirstName));
            etEmail.setError("Enter " + getResources().getString(R.string.suEmail));
            etPassword.clearError();
            etConfirmPassword.setError("Enter " + getResources().getString(R.string.suConfirmPassword));
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

    private void ResolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, requestCodeForSignIn);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void GetGooglePlusProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                isIntegrationLogin = true;

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
                objCustomerMaster.setIsEnabled(true);

//                GCMTokenRegistration();
                Log.e("Registrartion"," encoded token:"+token.replace(":", "2E2").replace("-","3E3").replace("_","4E4"));
                objCustomerMaster.setGCMToken(token.replace(":", "2E2").replace("-","3E3").replace("_","4E4"));
                RequestInsertCustomerMaster(objCustomerMaster,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RequestInsertCustomerMaster(CustomerMaster objCustomerMaster,boolean isSignIn) {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.InsertCustomerMaster(objCustomerMaster,isSignIn, RegistrationActivity.this);
    }

    private void GCMTokenRegistration()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    token = intent.getStringExtra("token");
                    //Displaying the token as toast
//                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    Log.e("registrartion token", " " + token);
                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    //endregion
}
