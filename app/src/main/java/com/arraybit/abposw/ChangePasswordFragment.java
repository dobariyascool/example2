package com.arraybit.abposw;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


@SuppressWarnings("ConstantConditions")
public class ChangePasswordFragment extends Fragment implements CustomerJSONParser.CustomerRequestListener {

    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnChangePassword;
    ToggleButton tbPasswordShowOld, tbPasswordShowNew, tbPasswordShowConfirm;
    View view;
    ProgressDialog progressDialog;
    SharePreferenceManage objSharePreferenceManage;
    int currentPage = 1;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_change_password));

        setHasOptionsMenu(true);

        //edittext
        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        //end

        //button
        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        //end

        //togglebutton
        tbPasswordShowOld = (ToggleButton) view.findViewById(R.id.tbPasswordShowOld);
        tbPasswordShowNew = (ToggleButton) view.findViewById(R.id.tbPasswordShowNew);
        tbPasswordShowConfirm = (ToggleButton) view.findViewById(R.id.tbPasswordShowConfirm);
        //end

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Service.CheckNet(getActivity())) {
                    RequestCustomerMaster();
                } else {
                    Globals.ShowSnackBar(btnChangePassword, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestCustomerMaster() {
        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        CustomerMaster objCustomerMaster = new CustomerMaster();
        objSharePreferenceManage = new SharePreferenceManage();

        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            objCustomerMaster.setCustomerMasterId(Short.parseShort(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
        } else {
            objCustomerMaster.setCustomerMasterId(0);
        }
        objCustomerMaster.setPassword(etNewPassword.getText().toString());

        objCustomerJSONParser.UpdateCustomerMasterPassword(objCustomerMaster, getActivity(), this);
    }


    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        if (currentPage > 3) {
            progressDialog.dismiss();
        }
        SetError(errorCode);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "-2":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgUpdateFail), getActivity(), 1000);
                ClearControls();
                break;
            default:
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgUpdatePassword), getActivity(), 1000);
                ClearControls();
                break;
        }

    }

    private void ClearControls() {
        etOldPassword.setText("");
        etConfirmPassword.setText("");
        etNewPassword.setText("");
    }

}
