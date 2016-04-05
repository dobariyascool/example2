package com.arraybit.abposw;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


@SuppressWarnings("ConstantConditions")
public class UserProfileFragment extends Fragment {

    EditText etFirstName, etMobile, etBirthDate;
    RadioButton rbMale, rbFemale;
    AppCompatSpinner spArea, spCity;
    Button btnUpdateProfile;

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

        return view;

    }

}
