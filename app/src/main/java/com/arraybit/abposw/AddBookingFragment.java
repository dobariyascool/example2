package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BookingMaster;
import com.arraybit.parser.BookingJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.BookingRequestListener {


    EditText etCustomerName, etAdults, etChildren, etBookingdate, etFromTime, etToTime, etMobile, etEmail, etRemark;
    Button btnBookTable;
    Date time, date;
    View view;
    BookingMaster objBookingMaster;
    SharePreferenceManage objSharePreferenceManage;
    Activity activity;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    AddNewBookingListener objAddNewBookingListener;

    public AddBookingFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_booking, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_add_booking_fragment));
        setHasOptionsMenu(true);

        etCustomerName = (EditText) view.findViewById(R.id.etCustomerName);
        etAdults = (EditText) view.findViewById(R.id.etAdults);
        etChildren = (EditText) view.findViewById(R.id.etChildren);
        etBookingdate = (EditText) view.findViewById(R.id.etBookingdate);
        etFromTime = (EditText) view.findViewById(R.id.etFromTime);
        etToTime = (EditText) view.findViewById(R.id.etToTime);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etRemark = (EditText) view.findViewById(R.id.etRemark);
        btnBookTable = (Button) view.findViewById(R.id.btnBookTable);

        etBookingdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etBookingdate, getActivity());
                }
            }
        });
        etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowTimePickerDialog(etFromTime, getActivity());
                }
            }
        });
        etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowTimePickerDialog(etToTime, getActivity());
                }
            }
        });

        btnBookTable.setOnClickListener(this);
        return view;
    }

    public void ShowDateTimePicker(int id) {
        if (id == R.id.etBookingdate) {
            Globals.ShowDatePickerDialog(etBookingdate, getActivity());
        } else if (id == R.id.etFromTime) {
            Globals.ShowTimePickerDialog(etFromTime, getActivity());
        } else if (id == R.id.etToTime) {
            Globals.ShowTimePickerDialog(etToTime, getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBookTable) {
            view = v;
            objBookingMaster = new BookingMaster();
            progressDialog.show(getFragmentManager(), "");
            if (!ValidateControls()) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                    objBookingMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                    objBookingMaster.setlinktoUserMasterIdCreatedBy((short) Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                }
                objBookingMaster.setBookingPersonName(etCustomerName.getText().toString());
                objBookingMaster.setNoOfAdults((short) Integer.parseInt(etAdults.getText().toString()));
                if (!etChildren.getText().toString().equals("")) {
                    objBookingMaster.setNoOfChildren((short) Integer.parseInt(etChildren.getText().toString()));
                }
                objBookingMaster.setIsHourly(true);
                try {
                    date = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etBookingdate.getText().toString());
                    objBookingMaster.setFromDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    date = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etBookingdate.getText().toString());
                    objBookingMaster.setToDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    time = new SimpleDateFormat("HH:mm", Locale.US).parse(etFromTime.getText().toString());
                    objBookingMaster.setFromTime(new SimpleDateFormat("HH:mm:ss", Locale.US).format(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    time = new SimpleDateFormat("HH:mm", Locale.US).parse(etToTime.getText().toString());
                    objBookingMaster.setToTime(new SimpleDateFormat("HH:mm:ss", Locale.US).format(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                objBookingMaster.setEmail(etEmail.getText().toString());
                objBookingMaster.setPhone(etMobile.getText().toString());
                objBookingMaster.setTotalAmount(0.0);
                objBookingMaster.setDiscountPercentage((short) 0);
                objBookingMaster.setDiscountAmount(0.0);
                objBookingMaster.setExtraAmount(0.0);
                objBookingMaster.setNetAmount(0.0);
                objBookingMaster.setPaidAmount(0.0);
                objBookingMaster.setBalanceAmount(0.0);
                objBookingMaster.setRemark(etRemark.getText().toString());
                objBookingMaster.setIsPreOrder(false);
                objBookingMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
                objBookingMaster.setIsDeleted(false);
                objBookingMaster.setBookingStatus((short) Globals.BookingStatus.New.getValue());

                BookingJSONParser objBookingJSONParser = new BookingJSONParser();
                objBookingJSONParser.InsertBookingMaster(getActivity(),this, objBookingMaster);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                getActivity().finish();
            } else {

                getActivity().getSupportFragmentManager().popBackStack();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BookingResponse(String errorCode,ArrayList<BookingMaster> alBookingMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            SetUser(etEmail, etCustomerName, etMobile);
        }
    }

    //region Private Methods
    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.ybAddBookingSuccessMsg), getActivity(), 1000);
                if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                    getActivity().finish();
                } else {
                    objAddNewBookingListener = (AddNewBookingListener)getTargetFragment();
                    objAddNewBookingListener.AddNewBooking(objBookingMaster);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    private void SetUser(EditText etEmail, EditText etName, EditText etMobile) {
        objSharePreferenceManage = new SharePreferenceManage();

        etEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()));
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()) != null) {
            etName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()));
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "Phone", getActivity()) != null) {
            etMobile.setText(objSharePreferenceManage.GetPreference("LoginPreference", "Phone", getActivity()));
        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            //region Customer Name
            //endregion

            if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etBookingdate.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etFromTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etCustomerName.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etToTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etBookingdate.clearError();
                etFromTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.clearError();
                etAdults.clearError();
                etEmail.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.clearError();
                etCustomerName.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etBookingdate.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.clearError();
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            }

            //region Adults
            //endregion

            else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etFromTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etCustomerName.clearError();
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etCustomerName.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etCustomerName.clearError();
                etBookingdate.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etCustomerName.clearError();
                etBookingdate.clearError();
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etBookingdate.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etCustomerName.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etCustomerName.clearError();
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            }

            //region Booking Date
            //endregion

            else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etBookingdate.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etBookingdate.clearError();
                etFromTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etBookingdate.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            }

            //region From Time
            //endregion

            else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etFromTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etFromTime.clearError();
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etFromTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etBookingdate.clearError();
                etToTime.clearError();
                IsValid = false;
            }

            //region To Time
            //endregion

            else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etToTime.clearError();
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etBookingdate.clearError();
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                }
                etFromTime.clearError();
                etBookingdate.clearError();
                IsValid = false;
            }

            //region Email
            //endregion

            else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                    etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                } else {
                    etEmail.clearError();
                }
                IsValid = false;
            } else if (etCustomerName.getText().toString().equals("")
                    && etAdults.getText().toString().equals("")
                    && etBookingdate.getText().toString().equals("")
                    && etFromTime.getText().toString().equals("")
                    && etToTime.getText().toString().equals("")
                    && etEmail.getText().toString().equals("")) {
                etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
                IsValid = false;
            } else if (!etCustomerName.getText().toString().equals("")
                    && !etAdults.getText().toString().equals("")
                    && !etBookingdate.getText().toString().equals("")
                    && !etFromTime.getText().toString().equals("")
                    && !etToTime.getText().toString().equals("")
                    && !etEmail.getText().toString().equals("")) {
                etCustomerName.clearError();
                if (etAdults.getText().toString().charAt(0) != '0') {
                    etAdults.clearError();
                } else {
                    etAdults.setError("Zero is not valid");
                    IsValid = false;
                }
                etFromTime.clearError();
                etToTime.clearError();
                etBookingdate.clearError();
                etEmail.clearError();
            }
            if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
                IsValid = false;
            } else {
                etMobile.clearError();
            }
            if (!etChildren.getText().toString().equals("") &&  etChildren.getText().toString().charAt(0) == '0') {
                etChildren.setError("Zero is not valid");
                IsValid = false;
            } else {
                etChildren.clearError();
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 0);
            IsValid = false;
        }
        return IsValid;
    }

    public interface AddNewBookingListener{
        void AddNewBooking(BookingMaster objBookingMaster);
    }
    //endregion
}


//region Commented validation
//{//EditText etCustomerName, EditText etAdults, EditText etChildren, EditText etBookingdate, EditText etFromTime, EditText etToTime, EditText etPhone, EditText etEmail) {
//        boolean IsValid = true;
//        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
//
//        } else {
//        //region Customer Name
//        //
//
//        if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etBookingdate.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region Adults
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etChildren.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etBookingdate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etAdults.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etCustomerName.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etChildren.clearError();
//        etCustomerName.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etBookingdate.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etEmail.setError("ENter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etBookingdate.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region Children
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etBookingdate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etChildren.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region Booking Date
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etBookingdate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etBookingdate.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etBookingdate.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etBookingdate.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etBookingdate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region From Time
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etFromTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingdate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region To Time
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etBookingdate.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etBookingdate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etBookingdate.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        }
//
//        //region Mobile
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etBookingdate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingdate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etBookingdate.clearError();
//        IsValid = false;
//        }
//
//        //region Email
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingdate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        IsValid = false;
//        }
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivityForResult(intent, 0);
//        IsValid = false;
//        }
//
////        if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
////            etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
////            IsValid = false;
////        } else {
////            etMobile.clearError();
////        }
//        return IsValid;
//        }
//endregion