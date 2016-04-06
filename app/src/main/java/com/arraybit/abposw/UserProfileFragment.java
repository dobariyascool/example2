package com.arraybit.abposw;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@SuppressWarnings("ConstantConditions")
public class UserProfileFragment extends Fragment implements CustomerJSONParser.CustomerRequestListener {

    CustomerMaster objCustomerMaster;
    EditText etFirstName, etMobile, etBirthDate;
    RadioButton rbMale, rbFemale;
    AppCompatSpinner spArea, spCity;
    Button btnUpdateProfile;
    TextView txtLoginChar, txtFullName, txtEmail;
    int customerMasterId;
    Date birthDate;
    View view;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //Toolbar start
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_your_profile));
        setHasOptionsMenu(true);
        //end

        //EditText start
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etBirthDate = (EditText) view.findViewById(R.id.etDateOfBirth);
        //end

        //Text View start
        txtLoginChar = (TextView) view.findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtFullName = (TextView) view.findViewById(R.id.txtFullName);
        //end

        //RadioButton start
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        //end

        //Spinner start
        spCity = (AppCompatSpinner) view.findViewById(R.id.spCity);
        spArea = (AppCompatSpinner) view.findViewById(R.id.spArea);
        //end

        //Button start
        btnUpdateProfile = (Button) view.findViewById(R.id.btnUpdate);
        //end

        setHasOptionsMenu(true);

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }

        if (Service.CheckNet(getActivity())) {
            UserRequest();
        } else {
            Globals.ShowSnackBar(view, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Globals.HideKeyBoard(getActivity(), getView());
                getActivity().getSupportFragmentManager().popBackStack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        this.objCustomerMaster = objCustomerMaster;
        SetUserName();
    }

    // region Private Methods
    private void SetUserName() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()) != null) {
            txtEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()));
            txtLoginChar.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()));
        } else {
            txtFullName.setVisibility(View.GONE);
        }

        if (objCustomerMaster != null) {
            etFirstName.setText(objCustomerMaster.getCustomerName());
            etMobile.setText(objCustomerMaster.getPhone1());
            if (objCustomerMaster.getGender().equals(rbFemale.getText().toString())) {
                rbFemale.setChecked(true);
            } else {
                rbMale.setChecked(true);
            }
            if (objCustomerMaster.getBirthDate() != null) {
                try {
                    birthDate = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(objCustomerMaster.getBirthDate());
                    etBirthDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void UserRequest() {
        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.SelectCustomerMaster(getActivity(), null, null, String.valueOf(customerMasterId), this);
    }
    //endregion
}
