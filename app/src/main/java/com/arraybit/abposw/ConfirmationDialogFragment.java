package com.arraybit.abposw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.TextView;

public class ConfirmationDialogFragment extends Fragment {

    TextView txtConfirmMessage;

    public ConfirmationDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation_dailog, container, false);

        txtConfirmMessage = (TextView) view.findViewById(R.id.txtConfirmMessage);
        txtConfirmMessage.setText(String.format(getResources().getString(R.string.cdltfMsg),"Cancel"));
        return view;
    }
}
