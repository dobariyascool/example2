package com.arraybit.abposw;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ContactUsMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

public class ContactUsActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText etContactUsName, etContactUsEmail, etContactUsMobile, etContactUsMessage;
    TextView txtCountry, txtAddress, txtWebSite, txtPhone1, txtPhone2;
    Button btnSend;
    LinearLayout linearLayoutContactUs;
    com.arraybit.abposw.ProgressDialog progressDialog = new ProgressDialog();
    BusinessMaster objBusinessMaster;
    ContactUsMaster objContactUsMaster;
    View view;

    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_activity_contactus));

        etContactUsName = (EditText) findViewById(R.id.etContactUsName);
        etContactUsEmail = (EditText) findViewById(R.id.etContactUsEmail);
        etContactUsMobile = (EditText) findViewById(R.id.etContactUsMobile);
        etContactUsMessage = (EditText) findViewById(R.id.etContactUsMessage);

        txtCountry = (TextView) findViewById(R.id.txtCountry);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtWebSite = (TextView) findViewById(R.id.txtWebSite);
        txtPhone1 = (TextView) findViewById(R.id.txtPhone1);
        txtPhone2 = (TextView) findViewById(R.id.txtPhone2);
        btnSend = (Button) findViewById(R.id.btnSend);
        linearLayoutContactUs = (LinearLayout) findViewById(R.id.linearLayoutContactUs);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        }

        if (Service.CheckNet(this)) {
            RequestBusinessInfoMaster();
        } else {
            Globals.ShowSnackBar(linearLayoutContactUs, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        btnSend.setOnClickListener(this);
        txtWebSite.setOnClickListener(this);
        txtPhone1.setOnClickListener(this);
        txtPhone2.setOnClickListener(this);
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster) {
        progressDialog.dismiss();
        if (errorCode == null) {
            this.objBusinessMaster = objBusinessMaster;
            SetBusinessDetail();
        } else {
            SetError(errorCode);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            view = v;
            progressDialog.show(ContactUsActivity.this.getSupportFragmentManager(), "");
            objContactUsMaster = new ContactUsMaster();
            if (!ValidationControls()) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(view, this.getResources().getString(R.string.MsgValidation), this, 1000);
            } else {
                objContactUsMaster.setName(etContactUsName.getText().toString());
                objContactUsMaster.setEmail(etContactUsEmail.getText().toString());
                objContactUsMaster.setMobile(etContactUsMobile.getText().toString());
                objContactUsMaster.setMessage(etContactUsMessage.getText().toString());

                BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
                objBusinessJSONParser.InsertContactUsMaster(this, objContactUsMaster);
            }
        } else if (v.getId() == R.id.txtWebSite) {
            Uri uri = Uri.parse(objBusinessMaster.getWebsite());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.txtPhone1) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + objBusinessMaster.getPhone1()));
            startActivity(intent);
        } else if (v.getId() == R.id.txtPhone2) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + objBusinessMaster.getPhone2()));
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    void getMyLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            map.setMyLocationEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
    }

    protected void connectClient() {
        // Connect the client.
        //if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
        //    mGoogleApiClient.connect();
        //}
    }

    //region Private Methods
    private void RequestBusinessInfoMaster() {
        progressDialog.show(ContactUsActivity.this.getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectBusinessMaster(this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetBusinessDetail() {
        txtCountry.setText(objBusinessMaster.getCountry());
        txtAddress.setText(objBusinessMaster.getAddress());

        String webSite = objBusinessMaster.getWebsite();
        SpannableString content = new SpannableString(webSite);
        content.setSpan(new UnderlineSpan(), 0, webSite.length(), 0);

        txtWebSite.setText(content);
        txtPhone1.setText(objBusinessMaster.getPhone1());
        txtPhone2.setText(objBusinessMaster.getPhone2());
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, this.getResources().getString(R.string.MsgServerNotResponding), this, 1000);
                break;
            default:
                Globals.ShowSnackBar(view, this.getResources().getString(R.string.MsgInsertSuccess), this, 1000);
                break;
        }
    }

    private boolean ValidationControls() {
        boolean IsValid = true;
        if (etContactUsEmail.getText().toString().equals("")
                && etContactUsMessage.getText().toString().equals("")) {
            etContactUsEmail.setError("Enter " + getResources().getString(R.string.cuEmail));
            etContactUsMessage.setError("Enter " + getResources().getString(R.string.cuMessage));
            IsValid = false;
        } else if (!etContactUsEmail.getText().toString().equals("")
                && etContactUsMessage.getText().toString().equals("")) {
            etContactUsMessage.setError("Enter " + getResources().getString(R.string.cuMessage));
            if (!Globals.IsValidEmail(etContactUsEmail.getText().toString())) {
                etContactUsEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                IsValid = false;
            } else {
                etContactUsEmail.clearError();
            }
            IsValid = false;
        } else if (etContactUsEmail.getText().toString().equals("")
                && !etContactUsMessage.getText().toString().equals("")) {
            etContactUsEmail.setError("Enter " + getResources().getString(R.string.cuEmail));
            etContactUsMessage.clearError();
            IsValid = false;
        } else if (!etContactUsEmail.getText().toString().equals("")
                && !etContactUsMessage.getText().toString().equals("")) {
            if (!Globals.IsValidEmail(etContactUsEmail.getText().toString())) {
                etContactUsEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                IsValid = false;
            } else {
                etContactUsEmail.clearError();
            }
            etContactUsMessage.clearError();
            IsValid = false;
        }
        if (!etContactUsMobile.getText().toString().equals("") && etContactUsMobile.getText().length() != 10) {
            etContactUsMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else {
            etContactUsMobile.clearError();
        }
        return IsValid;
    }
    //endregion
}
