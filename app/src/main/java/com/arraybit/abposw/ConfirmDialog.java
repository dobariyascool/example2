package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.arraybit.modal.CheckOut;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmDialog extends DialogFragment implements View.OnClickListener {


    CheckOut objCheckOut;
    Date time;
    ConfirmationResponseListener objConfirmationResponseListener;
    boolean isDeleteConfirm;
    String message;

    public ConfirmDialog() {
        // Required empty public constructor
    }


    public ConfirmDialog(CheckOut objCheckOut, boolean isDeleteConfirm, String message) {
        // Required empty public constructor
        this.objCheckOut = objCheckOut;
        this.isDeleteConfirm = isDeleteConfirm;
        this.message = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextView txtHeader = (TextView) view.findViewById(R.id.txtHeader);
        TextView txtOrderMessage = (TextView) view.findViewById(R.id.txtOrderMessage);
        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);

        Button btnConfirm = (Button)view.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        if (isDeleteConfirm) {
            txtHeader.setVisibility(View.GONE);
            txtOrderMessage.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
            btnConfirm.setText(getActivity().getResources().getString(R.string.cdfRemove));
        } else {
            txtHeader.setVisibility(View.VISIBLE);
            txtOrderMessage.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.GONE);
            btnConfirm.setText(getActivity().getResources().getString(R.string.cdfConfirm));
        }

        if (isDeleteConfirm) {
            txtMessage.setText(message);
        } else {
            SetOrderMessage(txtOrderMessage);
        }



        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConfirm) {
            if(getTargetFragment()!=null){
                objConfirmationResponseListener = (ConfirmationResponseListener) getTargetFragment();
                objConfirmationResponseListener.ConfirmResponse();
            }else {
                objConfirmationResponseListener = (ConfirmationResponseListener) getActivity();
                objConfirmationResponseListener.ConfirmResponse();
            }
            dismiss();
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetOrderMessage(TextView txtMessage) {
        try {
            if (!objCheckOut.getOrderTime().equals(getActivity().getResources().getString(R.string.coaTime))) {
                Date targetTime = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).parse(objCheckOut.getOrderTime());
                targetTime.setTime(targetTime.getTime() + 20 * 60 * 1000); //add minute
                time = targetTime;
            } else {
                Date date = new Date();
                date.setTime(date.getTime() + 20 * 60 * 1000);
                time = date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtMessage.setText(String.format(getActivity().getResources().getString(R.string.cdfMsg), objCheckOut.getOrderType() == Globals.OrderType.TakeAway.getValue() ? "take away" : "home delivery") + " " + objCheckOut.getOrderDate() + " " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(time));
    }

    public interface ConfirmationResponseListener {
        void ConfirmResponse();
    }

//    @SuppressLint("SetTextI18n")
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirm_dialog,null);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        if(isDeleteConfirm){
//            builder.setPositiveButton(getResources().getString(R.string.cdfRemove), null);
//        }else{
//            builder.setPositiveButton(getResources().getString(R.string.cdfConfirm), null);
//        }
//        builder.setNegativeButton(getResources().getString(R.string.cdfCancel), null);
//        builder.setCancelable(false);
//
//
//        final AlertDialog alertDialog = builder.create();
//        TextView txtHeader = (TextView)view.findViewById(R.id.txtHeader);
//        TextView txtOrderMessage = (TextView) view.findViewById(R.id.txtOrderMessage);
//        TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
//        if (isDeleteConfirm) {
//            txtHeader.setVisibility(View.GONE);
//            txtOrderMessage.setVisibility(View.GONE);
//            txtMessage.setVisibility(View.VISIBLE);
//        } else {
//            txtHeader.setVisibility(View.VISIBLE);
//            txtOrderMessage.setVisibility(View.VISIBLE);
//            txtMessage.setVisibility(View.GONE);
//        }
//
//        if(isDeleteConfirm){
//           txtMessage.setText(String.format(getActivity().getResources().getString(R.string.cdfConfirmDeleteMsg),message));
//        }else{
//            SetOrderMessage(txtOrderMessage);
//        }
//
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(final DialogInterface dialog) {
//                Button positive = (alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        objConfirmationResponseListener = (ConfirmationResponseListener)getActivity();
//                        objConfirmationResponseListener.ConfirmResponse();
//                        dismiss();
//                    }
//                });
//
//                Button negative = (alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dismiss();
//                    }
//                });
//
//            }
//        });
//
//        return alertDialog;
//    }

}
