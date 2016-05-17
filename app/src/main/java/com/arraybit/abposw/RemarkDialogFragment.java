package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

public class RemarkDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    public static String strRemark;
    EditText etRemark;
    Button btnCancel, btnDone;
    String remark;
    RemarkResponseListener objRemarkResponseListener;

    public RemarkDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public RemarkDialogFragment(String strRemark) {
        // Required empty public constructor
        this.remark = strRemark;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remark_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        etRemark = (EditText) view.findViewById(R.id.etRemark);
        if(remark==null) {
            if (strRemark != null) {
                etRemark.setText(strRemark);
            } else {
                etRemark.setText("");
            }
        }else{
            etRemark.setText(remark);
        }

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            if(remark==null) {
                strRemark = etRemark.getText().toString();
            }
            if(getTargetFragment()!=null) {
                objRemarkResponseListener = (RemarkResponseListener) getTargetFragment();
                objRemarkResponseListener.RemarkResponse(etRemark.getText().toString());
            }else{
                objRemarkResponseListener = (RemarkResponseListener) getActivity();
                objRemarkResponseListener.RemarkResponse(etRemark.getText().toString());
            }
            dismiss();
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
        }
    }

    public interface RemarkResponseListener {
        void RemarkResponse(String strRemark);
    }
}
