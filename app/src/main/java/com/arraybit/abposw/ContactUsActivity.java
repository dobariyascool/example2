package com.arraybit.abposw;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ContactUsMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

public class ContactUsActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    EditText etContactUsName, etContactUsEmail, etContactUsMobile, etContactUsMessage;
    TextView txtCountry, txtAddress, txtWebSite, txtPhone1, txtPhone2;
    Button btnSend;
    LinearLayout linearLayoutContactUs;
    com.arraybit.abposw.ProgressDialog progressDialog = new ProgressDialog();
    BusinessMaster objBusinessMaster;
    ContactUsMaster objContactUsMaster;
    View view;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

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
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            //MapDemoActivityPermissionsDispatcher.getMyLocationWithCheck(this);
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void connectClient() {
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = (Dialog) GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }
    //endregion

    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
