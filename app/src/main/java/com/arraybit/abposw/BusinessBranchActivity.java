package com.arraybit.abposw;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.adapter.BusinessBranchAdapter;
import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.FCMMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.arraybit.parser.FCMJSONParser;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class BusinessBranchActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener, BusinessBranchAdapter.BranchSelectorListener {

    final private int requestCodeForPermission = 55;
    AppCompatSpinner spOrderCity;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isCityFilter;
    short businessGroupMasterId;
    ArrayList<BusinessMaster> alBusinessMaster;
    LinearLayout businessBranchLayout;
    RecyclerView rvBusinessBranch;
    short businessMasterId;
    TextView txtBranchHeader;
    GoogleApiClient googleApiClient;
    SharePreferenceManage objSharePreferenceManager = new SharePreferenceManage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_branch);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        businessBranchLayout = (LinearLayout) findViewById(R.id.businessBranchLayout);
        Globals.SetScaleImageBackground(BusinessBranchActivity.this, businessBranchLayout, null, null);

        businessGroupMasterId = getIntent().getShortExtra("linktoBusinessGroupMasterId", (short) 0);


        spOrderCity = (AppCompatSpinner) findViewById(R.id.spOrderCity);
        txtBranchHeader = (TextView) findViewById(R.id.txtBranchHeader);
        rvBusinessBranch = (RecyclerView) findViewById(R.id.rvBusinessBranch);

        if (Service.CheckNet(BusinessBranchActivity.this)) {
            RequestBusinessMaster(businessGroupMasterId, null);
        } else {
            Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
        }

        spOrderCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView spinnerText = (TextView) view.findViewById(R.id.txtSpinnerItem);
                if (spinnerText != null) {
                    spinnerText.setTextColor(ContextCompat.getColor(BusinessBranchActivity.this, R.color.white_blur));
                }
                if (Service.CheckNet(BusinessBranchActivity.this)) {
                    isCityFilter = true;
                    RequestBusinessMaster(alBusinessMaster.get(0).getLinktoBusinessGroupMasterId(), (String) parent.getAdapter().getItem(position));
                } else {
                    Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case requestCodeForPermission:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                } else {
                    // Permission Denied
                    Toast.makeText(BusinessBranchActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        this.alBusinessMaster = alBusinessMaster;
        if (isCityFilter) {
            SetRecyclerView();
        } else {
            FillCity();
        }

    }

    @Override
    public void BranchSelectorOnClickListener(BusinessMaster objBusinessMaster) {
        Globals.linktoBusinessMasterId = objBusinessMaster.getBusinessMasterId();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessMasterId", String.valueOf(objBusinessMaster.getBusinessMasterId()), BusinessBranchActivity.this);
        if (objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", BusinessBranchActivity.this) != null) {
            businessMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", BusinessBranchActivity.this));
            if (businessMasterId != Globals.linktoBusinessMasterId) {
                ClearGoogleAccountAndFacebook();
                Globals.ClearUserPreference(BusinessBranchActivity.this, BusinessBranchActivity.this);
                FCMMaster fcmMaster = new FCMMaster();
                fcmMaster.setFCMToken(SplashScreenActivity.token);
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", BusinessBranchActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", BusinessBranchActivity.this) != null) {
                    fcmMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", BusinessBranchActivity.this)));
                }
                FCMJSONParser fcmjsonParser = new FCMJSONParser();
                fcmjsonParser.UpdateFCMMasterByCustomerId(fcmMaster, BusinessBranchActivity.this);
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    private void RequestBusinessMaster(short linktoBusinessGroupMasterId, String city) {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        if (linktoBusinessGroupMasterId != 0) {
            objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(BusinessBranchActivity.this, String.valueOf(linktoBusinessGroupMasterId), city);
        }

    }

    private void FillCity() {
        ArrayList<SpinnerItem> alCity = new ArrayList<>();
        short cnt = 0;
        boolean isDuplicate = false;
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            for (BusinessMaster objBusiness : alBusinessMaster) {
                SpinnerItem objSpinnerItem = new SpinnerItem();
                if (alCity.size() == 0) {
                    objSpinnerItem.setText(objBusiness.getCity());
                    objSpinnerItem.setValue(cnt);
                    alCity.add(objSpinnerItem);
                } else {
                    for (SpinnerItem objSpinner : alCity) {
                        if (objSpinner.getText().equals(objBusiness.getCity())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        objSpinnerItem.setText(objBusiness.getCity());
                        objSpinnerItem.setValue(cnt);
                        alCity.add(objSpinnerItem);
                    }
                }
                cnt++;
            }
            SpinnerAdapter cityAdapter = new SpinnerAdapter(BusinessBranchActivity.this, alCity, true);
            spOrderCity.setAdapter(cityAdapter);
        }
    }

    private void SetRecyclerView() {
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            BusinessBranchAdapter adapter = new BusinessBranchAdapter(BusinessBranchActivity.this, alBusinessMaster, this);
            rvBusinessBranch.setVisibility(View.VISIBLE);
            txtBranchHeader.setVisibility(View.VISIBLE);
            rvBusinessBranch.setAdapter(adapter);
            rvBusinessBranch.setLayoutManager(new LinearLayoutManager(BusinessBranchActivity.this));
        } else {
            rvBusinessBranch.setVisibility(View.GONE);
            txtBranchHeader.setVisibility(View.GONE);
        }
    }

    private void ClearGoogleAccountAndFacebook() {
        if (objSharePreferenceManager.GetPreference("LoginPreference", "IntegrationId", BusinessBranchActivity.this) != null) {
            if (objSharePreferenceManager.GetPreference("LoginPreference", "isLoginWithFb", BusinessBranchActivity.this) != null) {
                if (objSharePreferenceManager.GetPreference("LoginPreference", "isLoginWithFb", BusinessBranchActivity.this).equals("true")) {
                    LoginManager.getInstance().logOut();
                }
            } else {
                googleApiClient =
                        new GoogleApiClient.Builder(BusinessBranchActivity.this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                int hasWriteContactsPermission = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS);
                                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                                        Plus.AccountApi.clearDefaultAccount(googleApiClient);
                                        googleApiClient.disconnect();
                                        googleApiClient.connect();
                                    } else {
                                        requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
                                                requestCodeForPermission);
                                        return;
                                    }
                                } else {
                                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                                    googleApiClient.disconnect();
                                    googleApiClient.connect();
                                }

                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                googleApiClient.connect();
                            }
                        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                            }
                        }).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
                googleApiClient.connect();

            }
        }
    }
}
