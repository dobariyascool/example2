package com.arraybit.abposw;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_add_qty_remark_dialog, container, false);

        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnAdd);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {

        } else if (v.getId() == R.id.btnCancel) {

        } else if (v.getId() == R.id.ibMinus) {

        } else if (v.getId() == R.id.ibPlus) {

        }
    }
}
