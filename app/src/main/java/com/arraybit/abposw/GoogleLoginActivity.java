package com.arraybit.abposw;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoogleLoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    SignInButton btn_sign_in;
    Button btnLogOut;
    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private TextView txtName, txtEmail, txtLocation, txtBirth, txtGender, txtAboutMe;
    private LinearLayout informationLayout;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        btn_sign_in = (SignInButton) findViewById(R.id.btn_sign_in);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        informationLayout = (LinearLayout) findViewById(R.id.informationLayout);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtBirth = (TextView) findViewById(R.id.txtBirth);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtAboutMe = (TextView) findViewById(R.id.txtAboutMe);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
        btn_sign_in.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

    }


    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        } else {
            getProfileInformation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_LONG).show();
        updateProfile(false);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_in) {
            if (!mGoogleApiClient.isConnecting()) {
                signedInUser = true;
                resolveSignInError();
            }
        }else if(v.getId()==R.id.btnLogOut){
            gPlusSignOut();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

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
                resolveSignInError();
            }
        }

    }

    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
//            } catch (IntentSender.SendIntentException e) {
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//
//            }
//        }
    }

    private void updateProfile(boolean isSignedIn) {
        if (isSignedIn) {
            btn_sign_in.setVisibility(View.GONE);
            informationLayout.setVisibility(View.VISIBLE);
        } else {
            btn_sign_in.setVisibility(View.VISIBLE);
            informationLayout.setVisibility(View.GONE);
        }
    }


    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

                Toast.makeText(this, "Profile Info", Toast.LENGTH_LONG).show();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                if (currentPerson.getDisplayName() != null && !currentPerson.getDisplayName().equals("")) {
                    String personName = currentPerson.getDisplayName();
                    txtName.setText(personName);
                }

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                txtEmail.setText(email);
                if (currentPerson.getCurrentLocation() != null && !currentPerson.getCurrentLocation().equals("")) {
                    txtLocation.setText(currentPerson.getCurrentLocation());
                }
                if (currentPerson.getBirthday() != null && !currentPerson.getBirthday().equals("")) {
                    txtBirth.setText(currentPerson.getBirthday());
                } else {
                    Boolean isBirthDate = currentPerson.hasBirthday();
                    Toast.makeText(this, isBirthDate ? "true" : "false", Toast.LENGTH_LONG).show();
                }
                if (currentPerson.getGender() == Person.Gender.MALE) {
                    txtGender.setText("MALE");
                } else if (currentPerson.getGender() == Person.Gender.FEMALE) {
                    txtGender.setText("FEMALE");
                }


                if (currentPerson.getImage() != null && currentPerson.getImage().getUrl() != null && !currentPerson.getImage().getUrl().equals("")) {
                    Picasso.with(GoogleLoginActivity.this).load(currentPerson.getImage().getUrl()).into(ivPhoto);
                }

                List<Person.PlacesLived> lst = currentPerson.getPlacesLived();

//                if (currentPerson.getAboutMe() != null && !currentPerson.getAboutMe().equals("")) {
//                    txtAboutMe.setText(currentPerson.getAboutMe());
//                }

                if (lst != null && lst.size() > 0) {
                    if (lst.get(0).hasPrimary()) {
                        txtAboutMe.setText(lst.get(0).getValue());
                    }
                }


                //new LoadProfileImage(image).execute(personPhotoUrl);
                // update profile frame with new info about Google Account
                updateProfile(true);
                // profile


            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getProfileInformation();
                } else {
                    // Permission Denied
                    Toast.makeText(GoogleLoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {

                    mGoogleApiClient.connect();

                }

                break;

        }

    }


    private void gPlusSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateProfile(false);
        }
    }

    private void revokeGooglePlusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            mGoogleApiClient.connect();
                            updateProfile(false);
                        }
                    });
        }
    }


}
