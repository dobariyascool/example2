package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.arraybit.global.Globals;
import com.arraybit.modal.CheckOut;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmDialog extends DialogFragment {


    CheckOut objCheckOut;
    Date time;
    ConfirmationResponseListener objConfirmationResponseListener;

    public ConfirmDialog() {
        // Required empty public constructor
    }


    public ConfirmDialog(CheckOut objCheckOut) {
        // Required empty public constructor
        this.objCheckOut = objCheckOut;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_dialog,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(getResources().getString(R.string.cdfConfirm), null);
        builder.setNegativeButton(getResources().getString(R.string.cdfCancel), null);
        builder.setCancelable(false);


        final AlertDialog alertDialog = builder.create();
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        try {
            time = new SimpleDateFormat(Globals.TimeFormat, Locale.US).parse(objCheckOut.getOrderTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtMessage.setText(String.format(getActivity().getResources().getString(R.string.cdfMsg), objCheckOut.getOrderType()==Globals.OrderType.TakeAway.getValue()? "take away":"home delivery")+" "+objCheckOut.getOrderDate()+" "+new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(time));

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive = (alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objConfirmationResponseListener = (ConfirmationResponseListener)getActivity();
                        objConfirmationResponseListener.ConfirmResponse();
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

    public interface ConfirmationResponseListener{
        void ConfirmResponse();
    }

}
