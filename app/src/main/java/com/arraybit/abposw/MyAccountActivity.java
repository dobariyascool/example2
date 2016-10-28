package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.arraybit.adapter.MyAccountAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.FCMMaster;
import com.arraybit.parser.FCMJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


public class MyAccountActivity extends AppCompatActivity implements MyAccountAdapter.OptionClickListener, UserProfileFragment.UpdateResponseListener {

    final private int requestCodeForPermission = 55;
    ArrayList<String> alString;
    RecyclerView rvOptions;
    FloatingActionButton fabEdit;
    TextView txtLoginChar, txtFullName, txtEmail;
    FrameLayout myAccountLayout;
    ImageView ivProfile;
    boolean isIntegrationId;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        myAccountLayout = (FrameLayout)findViewById(R.id.myAccountLayout);
        txtLoginChar = (TextView) findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFullName = (TextView) findViewById(R.id.txtFullName);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        SetUserName();
        GetData();

        rvOptions = (RecyclerView) findViewById(R.id.rvOptions);
        MyAccountAdapter accountAdapter = new MyAccountAdapter(alString, MyAccountActivity.this, this);
        rvOptions.setAdapter(accountAdapter);
        rvOptions.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));


        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    ReplaceFragment(new UserProfileFragment(), getResources().getString(R.string.title_fragment_your_profile));
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    setResult(RESULT_OK);
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
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
                    Toast.makeText(MyAccountActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void OptionClick(int id) {
        if (id == 0 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ReplaceFragment(new YourOrderFragment(), getResources().getString(R.string.title_fragment_your_order));
        } else if (id == 1 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ReplaceFragment(new YourBookingFragment(), getResources().getString(R.string.title_fragment_your_booking));
        } else if (id == 2 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ReplaceFragment(new YourAddressFragment(), getResources().getString(R.string.title_fragment_your_address));
        } else if (id == 3 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if(objSharePreferenceManage.GetPreference("LoginPreference","IntegrationId",MyAccountActivity.this)!=null) {
                isIntegrationId = true;
                ClearGoogleAccountAndFacebook();
                Globals.ClearUserPreference(MyAccountActivity.this, MyAccountActivity.this);
                FCMMaster fcmMaster = new FCMMaster();
                fcmMaster.setFCMToken(SplashScreenActivity.token);
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this) != null) {
                    fcmMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this)));
                }
                FCMJSONParser fcmjsonParser = new FCMJSONParser();
                fcmjsonParser.UpdateFCMMasterByCustomerId(fcmMaster, MyAccountActivity.this);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IsLogin", true);
                returnIntent.putExtra("IsShowMessage", false);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else {
                ReplaceFragment(new ChangePasswordFragment(), getResources().getString(R.string.title_fragment_change_password));
            }
        } else if (id == 4 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ClearGoogleAccountAndFacebook();
            Globals.ClearUserPreference(MyAccountActivity.this, MyAccountActivity.this);
            FCMMaster fcmMaster = new FCMMaster();
            fcmMaster.setFCMToken(SplashScreenActivity.token);
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this) != null) {
                fcmMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", MyAccountActivity.this)));
            }
            FCMJSONParser fcmjsonParser = new FCMJSONParser();
            fcmjsonParser.UpdateFCMMasterByCustomerId(fcmMaster, MyAccountActivity.this);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("IsLogin",true);
            returnIntent.putExtra("IsShowMessage",false);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    public void EditTextOnClick(View view) {
        UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_your_profile));
        userProfileFragment.EditTextOnClick();
    }

    @Override
    public void UpdateResponse() {
        SetUserName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_your_profile));
                userProfileFragment.SelectImage(requestCode,data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void BookingDateOnClick(View view) {
        AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
        addBookingFragment.ShowDateTimePicker(view.getId());
    }

    @Override
    public void onBackPressed() {
        Globals.HideKeyBoard(MyAccountActivity.this, myAccountLayout);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_fragment_your_booking))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_booking), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_add_booking_fragment))) {
                AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
                addBookingFragment.objAddNewBookingListener = (AddBookingFragment.AddNewBookingListener) addBookingFragment.getTargetFragment();
                addBookingFragment.objAddNewBookingListener.AddNewBooking(null);
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_booking_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_fragment_your_address))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_address), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_add_address_fragment))) {
                AddAddressFragment addAddressFragment = (AddAddressFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_address_fragment));
                addAddressFragment.objAddNewAddressListener = (AddAddressFragment.AddNewAddressListener) addAddressFragment.getTargetFragment();
                addAddressFragment.objAddNewAddressListener.AddNewAddress(null);
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_address_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_fragment_change_password))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_change_password), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_fragment_your_order))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_order), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_fragment_your_profile))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_profile), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }


    //region Private Methods
    private void GetData() {
        alString = new ArrayList<>();
        if(objSharePreferenceManage.GetPreference("LoginPreference","IntegrationId",MyAccountActivity.this)!=null) {
            isIntegrationId = true;
        }

        for (int i = 0; i < getResources().getStringArray(R.array.Option).length; i++) {
            if(isIntegrationId && !getResources().getStringArray(R.array.Option)[i].equals(getResources().getString(R.string.cpChangePassword))){
                alString.add(getResources().getStringArray(R.array.Option)[i]);
            }else if(!isIntegrationId){
                alString.add(getResources().getStringArray(R.array.Option)[i]);
            }
        }
    }

    private void SetUserName() {
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this) != null) {
            txtEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this));
            txtLoginChar.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", MyAccountActivity.this) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", MyAccountActivity.this));
        } else {
            txtFullName.setVisibility(View.GONE);
        }

        if(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerProfileUrl", MyAccountActivity.this)!=null){
            ivProfile.setVisibility(View.VISIBLE);
            txtLoginChar.setVisibility(View.GONE);
            String url = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerProfileUrl", MyAccountActivity.this);
            Glide.with(MyAccountActivity.this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else{
            ivProfile.setVisibility(View.GONE);
            txtLoginChar.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = MyAccountActivity.this.getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.myAccountLayout, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    private void ClearGoogleAccountAndFacebook(){
        if (objSharePreferenceManage.GetPreference("LoginPreference", "IntegrationId", MyAccountActivity.this) != null) {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "isLoginWithFb", MyAccountActivity.this) != null) {
                if (objSharePreferenceManage.GetPreference("LoginPreference", "isLoginWithFb", MyAccountActivity.this).equals("true")) {
                    LoginManager.getInstance().logOut();
                }
            } else {
                googleApiClient =
                        new GoogleApiClient.Builder(MyAccountActivity.this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                int hasWriteContactsPermission = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS);
                                    if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                                        Plus.AccountApi.clearDefaultAccount(googleApiClient);
                                        googleApiClient.disconnect();
                                        googleApiClient.connect();
                                    }else {
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
    //endregion

}
