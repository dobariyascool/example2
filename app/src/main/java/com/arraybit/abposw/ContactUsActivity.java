package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ContactUsMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class ContactUsActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener, View.OnClickListener, OnMapReadyCallback {

    EditText etContactUsName, etContactUsEmail, etContactUsMobile, etContactUsMessage;
    TextView txtOffice, txtCountry, txtAddress, txtWebSite, txtPhone1, txtPhone2;
    Button btnSend;
    LinearLayout linearLayoutContactUs,errorLayout;
    ProgressDialog progressDialog = new ProgressDialog();
    BusinessMaster objBusinessMaster;
    ContactUsMaster objContactUsMaster;
    View view;
    ImageView ivCall;
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


        ivCall = (ImageView) findViewById(R.id.ivCall);

        txtOffice = (TextView) findViewById(R.id.txtOffice);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtWebSite = (TextView) findViewById(R.id.txtWebSite);
        txtPhone1 = (TextView) findViewById(R.id.txtPhone1);
        txtPhone2 = (TextView) findViewById(R.id.txtPhone2);
        btnSend = (Button) findViewById(R.id.btnSend);

        linearLayoutContactUs = (LinearLayout) findViewById(R.id.linearLayoutContactUs);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        if (Service.CheckNet(this)) {
            errorLayout.setVisibility(View.GONE);
            RequestBusinessInfoMaster();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
        }

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment));
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnSend.setOnClickListener(this);
        txtWebSite.setOnClickListener(this);
        ivCall.setOnClickListener(this);
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster,ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        if (errorCode == null && objBusinessMaster != null) {
            this.objBusinessMaster = objBusinessMaster;
            SetBusinessDetail();
        } else if (errorCode != null) {
            SetError(errorCode);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            view = v;
            progressDialog.show(getSupportFragmentManager(), "");
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
        }  else if(v.getId() == R.id.ivCall){
            if(objBusinessMaster.getPhone2()!=null && !objBusinessMaster.getPhone2().equals("")){
                ShowPhonePopup(v,v.getId());
            }else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + objBusinessMaster.getPhone1()));
                startActivity(intent);
            }
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        //map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        LatLng arraybit = new LatLng(21.1904891, 72.7864842);
        map.addMarker(new MarkerOptions().position(arraybit).title("ArrayBit"));
        map.moveCamera(CameraUpdateFactory.newLatLng(arraybit));
        map.animateCamera(CameraUpdateFactory.newLatLng(arraybit));
    }

    //region Private Methods
    private void RequestBusinessInfoMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectBusinessMaster(this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private PopupWindow ShowPhonePopup(View view, final int position) {
        LayoutInflater mInflater = (LayoutInflater)ContactUsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View layout = mInflater.inflate(R.layout.row_popup, null);


        android.widget.TextView txtPhone1 = (android.widget.TextView) layout.findViewById(R.id.txtEdit);
        android.widget.TextView txtPhone2 = (android.widget.TextView) layout.findViewById(R.id.txtDelete);

        txtPhone1.setText(objBusinessMaster.getPhone1());
        txtPhone2.setText(objBusinessMaster.getPhone2());

        layout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
        Drawable background = ContextCompat.getDrawable(ContactUsActivity.this, android.R.drawable.picture_frame);
        popupWindow.setBackgroundDrawable(background);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();

            }
        });
        txtPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + objBusinessMaster.getPhone1()));
                startActivity(intent);
            }
        });
        txtPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + objBusinessMaster.getPhone2()));
                startActivity(intent);
            }
        });
        return popupWindow;
    }

    private void SetBusinessDetail() {
        txtOffice.setText(getResources().getString(R.string.cuOffice));
        txtCountry.setText(objBusinessMaster.getCountry());
        txtAddress.setText(objBusinessMaster.getAddress());

        String webSite = objBusinessMaster.getWebsite();
        SpannableString content = new SpannableString(webSite);
        content.setSpan(new UnderlineSpan(), 0, webSite.length(), 0);

        txtWebSite.setText(content);
        txtPhone1.setText(objBusinessMaster.getPhone1());
        if(objBusinessMaster.getPhone2()!=null && !objBusinessMaster.getPhone2().equals("")){
            txtPhone2.setVisibility(View.VISIBLE);
            txtPhone2.setText(objBusinessMaster.getPhone2());
        }else{
            txtPhone2.setVisibility(View.INVISIBLE);
        }
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
