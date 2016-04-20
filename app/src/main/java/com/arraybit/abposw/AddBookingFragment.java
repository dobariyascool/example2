package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.BookingMaster;
import com.arraybit.parser.BookingJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class AddBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.BookingRequestListener {


    EditText etCustomerName, etAdults, etChildren, etBookingDate, etMobile, etEmail, etRemark;
    Button btnBookTable;
    AppCompatSpinner spFromTime, spToTime;
    Date time, date;
    String fromTime, toTime;
    int selected = -1;
    ArrayList<SpinnerItem> alFromTime, alToTime;
    LinearLayout timeLinearLayout;
    View view;
    BookingMaster objBookingMaster;
    SharePreferenceManage objSharePreferenceManage;
    Activity activity;
    ProgressDialog progressDialog = new ProgressDialog();
    AddNewBookingListener objAddNewBookingListener;
    TextView txtFromError, txtToTimeError;

    public AddBookingFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
        timeLinearLayout = (LinearLayout) view.findViewById(R.id.timeLinearLayout);
        timeLinearLayout.setVisibility(View.GONE);

        txtFromError = (TextView) view.findViewById(R.id.txtFromError);
        txtToTimeError = (TextView) view.findViewById(R.id.txtToTimeError);

        etCustomerName = (EditText) view.findViewById(R.id.etCustomerName);
        etAdults = (EditText) view.findViewById(R.id.etAdults);
        etChildren = (EditText) view.findViewById(R.id.etChildren);

        etBookingDate = (EditText) view.findViewById(R.id.etBookingDate);
        etBookingDate.setInputType(InputType.TYPE_NULL);

        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etRemark = (EditText) view.findViewById(R.id.etRemark);

        spFromTime = (AppCompatSpinner) view.findViewById(R.id.spFromTime);
        spToTime = (AppCompatSpinner) view.findViewById(R.id.spToTime);
        spToTime.setVisibility(View.INVISIBLE);

        btnBookTable = (Button) view.findViewById(R.id.btnBookTable);

        SetUser(etEmail, etCustomerName, etMobile);

        etBookingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.HideKeyBoard(getActivity(),v);
                    Globals.ShowDatePickerDialog(etBookingDate, getActivity(), true);
                }
            }
        });

        etBookingDate.addTextChangedListener(new TextWatcher() {
            String strDate="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etBookingDate.getText().toString().equals("") && !(strDate.equals(etBookingDate.getText().toString()))) {
                    strDate = etBookingDate.getText().toString();
                    alFromTime = new ArrayList<>();
                    if (Service.CheckNet(getActivity())) {
                        timeLinearLayout.setVisibility(View.VISIBLE);
                        RequestTimeSlot();
                    } else {
                        Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
        spFromTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selected == -1) {
                    selected = parent.getSelectedItemPosition();
                } else {
                    selected = parent.getSelectedItemPosition();
                    if (selected == 0) {
                        spToTime.setVisibility(View.INVISIBLE);
                        txtToTimeError.setVisibility(View.INVISIBLE);
                    } else {
                        if (Service.CheckNet(getActivity())) {
                            spToTime.setVisibility(View.VISIBLE);
                            fromTime = (String) parent.getAdapter().getItem(position);
                            FillToTime();
                        } else {
                            Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spToTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toTime = (String) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBookTable.setOnClickListener(this);
        return view;
    }

    public void ShowDateTimePicker(int id) {
        if (id == R.id.etBookingDate) {
            Globals.ShowDatePickerDialog(etBookingDate, getActivity(), true);
        }
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(getActivity(),v);
        if (v.getId() == R.id.btnBookTable) {
            view = v;
            objBookingMaster = new BookingMaster();
            progressDialog.show(getFragmentManager(), "");
            if (!ValidateControls()) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) == null) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
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
                        date = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(etBookingDate.getText().toString());
                        objBookingMaster.setFromDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(etBookingDate.getText().toString());
                        objBookingMaster.setToDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        time = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).parse(fromTime);
                        objBookingMaster.setFromTime(new SimpleDateFormat("HH:mm:ss", Locale.US).format(time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        time = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).parse(toTime);
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
                    objBookingJSONParser.InsertBookingMaster(getActivity(), this, objBookingMaster);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                if (getTargetFragment() != null) {
                    objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                    objAddNewBookingListener.AddNewBooking(null);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            } else {
                if (getTargetFragment() != null) {
                    objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                    objAddNewBookingListener.AddNewBooking(null);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BookingResponse(String errorCode, ArrayList<BookingMaster> alBookingMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void TimeSlotsResponse(ArrayList<SpinnerItem> alTimeSlot) {
        progressDialog.dismiss();
        this.alFromTime.addAll(alTimeSlot);
        FillFromTime();
    }

    //region Private Methods
    private void RequestTimeSlot() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllTimeSlots(this, getActivity(), String.valueOf(Globals.linktoBusinessMasterId), etBookingDate.getText().toString());
    }

    private void FillFromTime() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("------From Time------");
        objSpinnerItem.setValue(0);

        alFromTime.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alFromTime, true);
        spFromTime.setAdapter(adapter);
    }

    private void FillToTime() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText("------To Time------");
        objSpinnerItem.setValue(0);

        alToTime = new ArrayList<>();
        for (int i = selected + 1; i < alFromTime.size(); i++) {
            alToTime.add(alFromTime.get(i));
        }
        alToTime.add(0, objSpinnerItem);

        SpinnerAdapter toTimeadapter = new SpinnerAdapter(getActivity(), alToTime, true);
        spToTime.setAdapter(toTimeadapter);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                    if (getTargetFragment() != null) {
                        objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                        objAddNewBookingListener.AddNewBooking(objBookingMaster);
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                } else {
                    objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
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
        if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.clearError();
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etEmail.getText().toString().equals("")) {
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            System.out.println("id" + spFromTime.getSelectedItem());
            txtFromError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            System.out.println("id" + spFromTime.getSelectedItem());
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etEmail.getText().toString().equals("")) {
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etCustomerName.clearError();
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && !etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etEmail.getText().toString().equals("")) {
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
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
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && etEmail.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etEmail.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            txtToTimeError.setVisibility(View.VISIBLE);
            etCustomerName.clearError();
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            etEmail.clearError();
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            txtToTimeError.setVisibility(View.VISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            etEmail.clearError();
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            txtFromError.setVisibility(View.VISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            etEmail.clearError();
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etEmail.clearError();
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etEmail.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                IsValid = false;
            } else {
                etEmail.clearError();
                IsValid = true;
            }
        }
        if (!etChildren.getText().toString().equals("") && etChildren.getText().toString().charAt(0) != '0') {
            etChildren.clearError();
        } else {
            etChildren.setError("Zero is not valid");
        }
        if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
            etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else {
            etMobile.clearError();
        }

        return IsValid;
    }

    public interface AddNewBookingListener {
        void AddNewBooking(BookingMaster objBookingMaster);
    }
    //endregion
}


//region Commented validation
//{//EditText etCustomerName, EditText etAdults, EditText etChildren, EditText etBookingDate, EditText etFromTime, EditText etToTime, EditText etPhone, EditText etEmail) {
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etAdults.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Entre " + getResources().getString(R.string.ybEmail));
//        etAdults.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etAdults.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etCustomerName.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etBookingDate.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etEmail.setError("ENter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etBookingDate.clearError();
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etChildren.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etAdults.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingDate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etBookingDate.clearError();
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
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etFromTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etFromTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etToTime.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etChildren.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etBookingDate.clearError();
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etToTime.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etToTime.clearError();
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && !etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etToTime.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
//        && !etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etCustomerName.clearError();
//        etAdults.clearError();
//        etChildren.clearError();
//        etFromTime.clearError();
//        etBookingDate.clearError();
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
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
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etEmail.setError("Enter " + getResources().getString(R.string.ybEmail));
//        etMobile.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && !etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        etEmail.clearError();
//        IsValid = false;
//        } else if (!etCustomerName.getText().toString().equals("")
//        && !etAdults.getText().toString().equals("")
//        && !etChildren.getText().toString().equals("")
//        && !etBookingDate.getText().toString().equals("")
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
//        etBookingDate.clearError();
//        IsValid = false;
//        }
//
//        //region Email
//
//
//        else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && !etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
//        etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
//        etEmail.clearError();
//        IsValid = false;
//        } else if (etCustomerName.getText().toString().equals("")
//        && etAdults.getText().toString().equals("")
//        && etChildren.getText().toString().equals("")
//        && etBookingDate.getText().toString().equals("")
//        && etFromTime.getText().toString().equals("")
//        && etToTime.getText().toString().equals("")
//        && etMobile.getText().toString().equals("")
//        && etEmail.getText().toString().equals("")) {
//        etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
//        etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
//        etChildren.setError("Enter " + getResources().getString(R.string.ybChildren));
//        etFromTime.setError("Enter " + getResources().getString(R.string.ybFromTime));
//        etToTime.setError("Enter " + getResources().getString(R.string.ybToTime));
//        etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
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