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
import android.view.KeyEvent;
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
public class AddBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.BookingRequestListener, ConfirmDialog.ConfirmationResponseListener {

    EditText etCustomerName, etAdults, etChildren, etBookingDate, etMobile, etEmail, etRemark;
    Button btnBookTable;
    AppCompatSpinner spFromTime, spToTime;
    Date time, date;
    String fromTime, toTime;
    int selected = -1;
    ArrayList<SpinnerItem> alFromTime, alToTime;
    LinearLayout timeLinearLayout, addBookingFragment;
    View view;
    BookingMaster objBookingMaster;
    SharePreferenceManage objSharePreferenceManage;
    Activity activity;
    ProgressDialog progressDialog = new ProgressDialog();
    AddNewBookingListener objAddNewBookingListener;
    TextView txtFromError, txtToTimeError;
    boolean ShowBookingMessage = false;

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
        addBookingFragment = (LinearLayout) view.findViewById(R.id.addBookingFragment);
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
                    Globals.HideKeyBoard(getActivity(), v);
                    Globals.ShowDatePickerDialog(etBookingDate, getActivity(), true);
                }
            }
        });

        etBookingDate.addTextChangedListener(new TextWatcher() {
            String strDate = "";

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
                System.out.println("SelectedItemId" + spFromTime.getSelectedItemId());
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
                            if (alFromTime != null && alFromTime.size() != 0) {
                                FillToTime();
                            }
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

        etRemark.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });
        return view;
    }


    public void ShowDateTimePicker(int id) {
        if (id == R.id.etBookingDate) {
            Globals.ShowDatePickerDialog(etBookingDate, getActivity(), true);
        }
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.btnBookTable) {
            view = v;
            objBookingMaster = new BookingMaster();
            progressDialog.show(getFragmentManager(), "");
            if (!ValidateControls()) {
                progressDialog.dismiss();
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
            } else {
                progressDialog.dismiss();
                if (Service.CheckNet(getActivity())) {
                    ConfirmDialog confirmDialog = new ConfirmDialog(null, true, getActivity().getResources().getString(R.string.cdfConfirmBookingMsg));
                    confirmDialog.setTargetFragment(this, 0);
                    confirmDialog.show(getActivity().getSupportFragmentManager(), "");
                }else {
                    Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), addBookingFragment);
            if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                if (getTargetFragment() != null) {
                    objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                    objAddNewBookingListener.AddNewBooking(null);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsLogin", true);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
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
    public void ConfirmResponse() {
        objBookingMaster.setNoOfAdults((short) Integer.parseInt(etAdults.getText().toString().trim()));
        if (!etChildren.getText().toString().equals("")) {
            objBookingMaster.setNoOfChildren((short) Integer.parseInt(etChildren.getText().toString().trim()));
        }
        try {
            date = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(etBookingDate.getText().toString());
            objBookingMaster.setFromDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
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

        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectBookingIsAAvailable(getActivity(), this, objBookingMaster);
    }

    @Override
    public void TimeSlotsResponse(ArrayList<SpinnerItem> alTimeSlot) {
        progressDialog.dismiss();
        if (alTimeSlot != null && alTimeSlot.size() != 0) {
            this.alFromTime.addAll(alTimeSlot);
            FillFromTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null && ShowBookingMessage) {
            SetBookingMasterForInsert();
        }
    }

    //region Private Methods
    private void RequestTimeSlot() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllTimeSlots(this, getActivity(), String.valueOf(Globals.linktoBusinessMasterId), etBookingDate.getText().toString(), false);
    }

    private void FillFromTime() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getActivity().getResources().getString(R.string.ybFromTimeHeader));
        objSpinnerItem.setValue(0);

        alFromTime.add(0, objSpinnerItem);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), alFromTime, true);
        spFromTime.setAdapter(adapter);
    }

    private void FillToTime() {
        SpinnerItem objSpinnerItem = new SpinnerItem();
        objSpinnerItem.setText(getActivity().getResources().getString(R.string.ybToTimeHeader));
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
        if (errorCode.equals("-1")) {
            Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
        } else if (errorCode.equals("-2")) {
            Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgBookingIsAAvailable), getActivity(), 1000);
        } else if (errorCode.equals("1")) {
            SetBookingMasterForInsert();
        } else if (!errorCode.equals("0")) {
            if (activity.getTitle().equals(getActivity().getResources().getString(R.string.title_activity_booking))) {
                if (getTargetFragment() != null) {
                    objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                    objBookingMaster.setBookingMasterId(Integer.parseInt(errorCode));
                    objAddNewBookingListener.AddNewBooking(objBookingMaster);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ShowBookingMessage", true);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                }
            } else {
                objAddNewBookingListener = (AddNewBookingListener) getTargetFragment();
                objBookingMaster.setBookingMasterId(Integer.parseInt(errorCode));
                objAddNewBookingListener.AddNewBooking(objBookingMaster);
                getActivity().getSupportFragmentManager().popBackStack();
            }
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

    private void SetBookingMasterForInsert() {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) == null) {
            progressDialog.dismiss();
            ShowBookingMessage = true;
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivityForResult(intent, 0);
        } else {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                objBookingMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                objBookingMaster.setlinktoUserMasterIdCreatedBy((short) Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
            }

            objBookingMaster.setBookingPersonName(etCustomerName.getText().toString().trim());
            objBookingMaster.setNoOfAdults((short) Integer.parseInt(etAdults.getText().toString().trim()));
            if (!etChildren.getText().toString().equals("")) {
                objBookingMaster.setNoOfChildren((short) Integer.parseInt(etChildren.getText().toString().trim()));
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
            objBookingMaster.setEmail(etEmail.getText().toString().trim());
            objBookingMaster.setPhone(etMobile.getText().toString().trim());
            objBookingMaster.setTotalAmount(0.0);
            objBookingMaster.setDiscountPercentage((short) 0);
            objBookingMaster.setDiscountAmount(0.0);
            objBookingMaster.setExtraAmount(0.0);
            objBookingMaster.setNetAmount(0.0);
            objBookingMaster.setPaidAmount(0.0);
            objBookingMaster.setBalanceAmount(0.0);
            objBookingMaster.setRemark(etRemark.getText().toString().trim());
            objBookingMaster.setIsPreOrder(false);
            objBookingMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
            objBookingMaster.setIsDeleted(false);
            objBookingMaster.setBookingStatus((short) Globals.BookingStatus.New.getValue());

            BookingJSONParser objBookingJSONParser = new BookingJSONParser();
            objBookingJSONParser.InsertBookingMaster(getActivity(), this, objBookingMaster);

        }
    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.clearError();
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            System.out.println("id" + spFromTime.getSelectedItem());
            txtFromError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            System.out.println("id" + spFromTime.getSelectedItem());
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && etMobile.getText().toString().equals("")) {
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etCustomerName.clearError();
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && !etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etMobile.getText().toString().equals("")) {
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() != 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0) {
            etCustomerName.clearError();
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")
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
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0) {
            etCustomerName.clearError();
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.VISIBLE);
            txtToTimeError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && etMobile.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            etMobile.setError("Enter " + getResources().getString(R.string.ybPhone));
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            txtToTimeError.setVisibility(View.VISIBLE);
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etMobile.getText().toString().equals("")) {
            etAdults.setError("Enter " + getResources().getString(R.string.ybAdults));
            txtToTimeError.setVisibility(View.VISIBLE);
            etCustomerName.clearError();
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() != 0
                && spToTime.getSelectedItemId() == 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            txtToTimeError.setVisibility(View.VISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            txtFromError.setVisibility(View.INVISIBLE);
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && spFromTime.getSelectedItemId() == 0
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            txtFromError.setVisibility(View.VISIBLE);
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            etBookingDate.clearError();
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")) {
            etCustomerName.setError("Enter " + getResources().getString(R.string.ybCustomerName));
            etBookingDate.setError("Enter " + getResources().getString(R.string.ybBookingdate));
            if (etAdults.getText().toString().charAt(0) != '0') {
                etAdults.clearError();
            } else {
                etAdults.setError("Zero is not valid");
            }
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
            } else {
                etMobile.clearError();
            }
            IsValid = false;
        } else if (!etCustomerName.getText().toString().equals("")
                && !etAdults.getText().toString().equals("")
                && !etBookingDate.getText().toString().equals("")
                && !etMobile.getText().toString().equals("")
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
            if (etMobile.getText().length() != 10) {
                etMobile.setError("Enter 10 digit " + getResources().getString(R.string.ybPhone));
                IsValid = false;
            } else {
                etMobile.clearError();
                IsValid = true;
            }
        }
//        if (!etChildren.getText().toString().equals("")) {
//            if (etChildren.getText().toString().charAt(0)=='0') {
//                IsValid = false;
//                etChildren.setError("Zero is not valid");
//            } else {
//                etChildren.clearError();
//            }
//        }
        if (!etEmail.getText().toString().equals("") && !Globals.IsValidEmail(etEmail.getText().toString())) {
            IsValid = false;
            etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
        } else {
            etEmail.clearError();
        }

        return IsValid;
    }

    //endregion

    public interface AddNewBookingListener {
        void AddNewBooking(BookingMaster objBookingMaster);
    }
}