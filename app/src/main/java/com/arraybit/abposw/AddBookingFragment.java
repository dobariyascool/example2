package com.arraybit.abposw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class AddBookingFragment extends Fragment implements View.OnClickListener{

    EditText etCustomerName, etAdults, etChildren, etBirthdate, etFromTime, etToTime, etEmail, etRemark;
    Button btnBookTable;

    public AddBookingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_booking, container, false);
        etCustomerName = (EditText) view.findViewById(R.id.etCustomerName);
        etAdults = (EditText) view.findViewById(R.id.etAdults);
        etChildren = (EditText) view.findViewById(R.id.etChildren);
        etBirthdate = (EditText) view.findViewById(R.id.etBirthdate);
        etFromTime = (EditText) view.findViewById(R.id.etFromTime);
        etToTime = (EditText) view.findViewById(R.id.etToTime);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etRemark = (EditText) view.findViewById(R.id.etRemark);
        btnBookTable = (Button) view.findViewById(R.id.btnBookTable);

        btnBookTable.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
