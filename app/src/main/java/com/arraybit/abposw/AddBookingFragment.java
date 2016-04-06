package com.arraybit.abposw;

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
import java.util.Date;
import java.util.Locale;

public class AddBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.AddBooingRequestListener {

    EditText etCustomerName, etAdults, etChildren, etBookingdate, etFromTime, etToTime, etMobile, etEmail, etRemark;
    Button btnBookTable;
    Date time, date;
    View view;
    SharePreferenceManage objSharePreferenceManage;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();

    public AddBookingFragment() {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBookTable) {
            view = v;
            BookingMaster objBookingMaster = new BookingMaster();
            progressDialog.show(getFragmentManager(), "");
            if (!ValidateControls()) {//etCustomerName, etAdults, etChildren, etBookingdate, etFromTime, etToTime, etPhone, etEmail)) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                    objBookingMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                    objBookingMaster.setlinktoUserMasterIdCreatedBy((short) Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                }
                objBookingMaster.setBookingPersonName(etCustomerName.getText().toString());
                objBookingMaster.setNoOfAdults((short) Integer.parseInt(etAdults.getText().toString()));
                objBookingMaster.setNoOfChildren((short) Integer.parseInt(etChildren.getText().toString()));
                objBookingMaster.setIsHourly(true);
                try {
                    date = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etBookingdate.getText().toString());
                    objBookingMaster.setFromDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
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
                objBookingJSONParser.InsertBookingMaster(getActivity(), this.getTargetFragment(), objBookingMaster);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void AddBookingResponse(String errorCode) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            SetUser(etEmail, etCustomerName, etMobile);
        } else {
            etCustomerName.setText("");
            etEmail.setText("");
            etMobile.setText("");
            etCustomerName.setEnabled(true);
            etEmail.setEnabled(true);
            etMobile.setEnabled(true);
        }
    }

    //region Private Methods
    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                Globals.ShowSnackBar(view, getResources().getString(R.string.siLoginSucessMsg), getActivity(), 1000);
//                (getActivity()).setResult(Activity.RESULT_OK);
//                (getActivity()).finish();
                break;
        }

    }

    private void SetUser(EditText etEmail, EditText etName, EditText etMobile) {
        objSharePreferenceManage = new SharePreferenceManage();

        etEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()));
        etEmail.setEnabled(false);
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()) != null) {
            etName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()));
            etName.setEnabled(false);
        } else {
            etName.setEnabled(true);
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "Phone", getActivity()) != null) {
            etMobile.setText(objSharePreferenceManage.GetPreference("LoginPreference", "Phone", getActivity()));
            etMobile.setEnabled(false);
        } else {
            etMobile.setEnabled(true);
        }
    }

    private boolean ValidateControls() {//EditText etCustomerName, EditText etAdults, EditText etChildren, EditText etBookingdate, EditText etFromTime, EditText etToTime, EditText etPhone, EditText etEmail) {
        boolean IsValid = true;
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            if (!etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("")) {
                etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etAdults.clearError();
                IsValid = false;
            } else if (etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etChildren.clearError();
                IsValid = false;
            } else if (etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etBookingdate.clearError();
                IsValid = false;
            } else if (etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                etFromTime.clearError();
                IsValid = false;
            } else if (etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.clearError();
                IsValid = false;
            } else if (etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("")) {
                etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
                etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
                etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
                etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
                etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
                IsValid = false;
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 0);
            IsValid = false;
        }

//        if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
//            etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            IsValid = false;
//        } else {
//            etMobile.clearError();
//        }
        return IsValid;
    }
    //endregion
    
}


//region Commented validation
//        if (etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.clearError();
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            etEmail.clearError();
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etAdults.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etAdults.clearError();
//            etChildren.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            etAdults.clearError();
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//            etAdults.clearError();
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//            etAdults.clearError();
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("") && !etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && etMobile.getText().toString().equals("") && etEmail.getText().toString().equals("")) {
//            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//            etAdults.clearError();
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("") && etMobile.getText().toString().equals("") && etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && !etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            etChildren.clearError();
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && !etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            etBookingdate.clearError();
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && !etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            etFromTime.clearError();
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && !etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            etToTime.clearError();
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("") && etMobile.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
//                etEmail.setError("Enter Valid " + getResources().getString(R.string.ybEmail));
//            } else {
//                etEmail.clearError();
//            }
//            IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("") && etAdults.getText().toString().equals("") && etChildren.getText().toString().equals("") && etBookingdate.getText().toString().equals("") && etFromTime.getText().toString().equals("") && etToTime.getText().toString().equals("") && !etMobile.getText().toString().equals("") && etEmail.getText().toString().equals("")) {
//            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//            etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//            etBookingdate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//            etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//            etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//            if (etMobile.getText().length() != 10) {
//                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
//            } else {
//                etMobile.clearError();
//            }
//            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//            IsValid = false;
//        }
//endregion