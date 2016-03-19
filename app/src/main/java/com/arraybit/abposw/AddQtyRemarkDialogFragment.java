package com.arraybit.abposw;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;

public class AddQtyRemarkDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageButton ibMinus, ibPlus;
    Button btnCancel, btnOk;

    public AddQtyRemarkDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_qty_remark_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {
            dismiss();
            Globals.counter = Globals.counter + 1;
            MenuActivity menuActivity = (MenuActivity)getActivity();
            menuActivity.CartItemChangeResponse();
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
            Globals.counter = 0;
            MenuActivity menuActivity = (MenuActivity)getActivity();
            menuActivity.CartItemChangeResponse();
        } else if (v.getId() == R.id.ibMinus) {

        } else if (v.getId() == R.id.ibPlus) {

        }
    }
}
