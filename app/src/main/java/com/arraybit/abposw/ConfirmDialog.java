package com.arraybit.abposw;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.arraybit.modal.CheckOut;
import com.rey.material.widget.TextView;

public class ConfirmDialog extends DialogFragment {


    CheckOut objCheckOut;
    LayoutInflater layoutInflater;

    public ConfirmDialog() {
        // Required empty public constructor
    }

    public ConfirmDialog(CheckOut objCheckOut) {
        // Required empty public constructor
        this.objCheckOut = objCheckOut;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_dialog,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(getResources().getString(R.string.cdfConfirm), null);
        builder.setNegativeButton(getResources().getString(R.string.cdfCancel), null);
        builder.setCancelable(false);


        final AlertDialog alertDialog = builder.create();
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        txtMessage.setText(String.format(getActivity().getResources().getString(R.string.cdfMessage), objCheckOut.getOrderType()));

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive = (alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

                Button negative = (alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

            }
        });

        return alertDialog;
    }

}
