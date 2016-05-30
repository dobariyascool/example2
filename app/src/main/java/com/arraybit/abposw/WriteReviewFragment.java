package com.arraybit.abposw;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ReviewMaster;
import com.arraybit.parser.ReviewJSONParser;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

public class WriteReviewFragment extends DialogFragment implements View.OnClickListener, ReviewJSONParser.InsertReviewMasterRequestListener {

    RatingBar rtbReview;
    EditText etReview;
    TextView txtReviewClose, txtReviewSubmit;
    ReviewMaster objReviewMaster;
    SharePreferenceManage objSharePreferenceManage;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        rtbReview = (RatingBar) view.findViewById(R.id.rtbReview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = rtbReview.getProgressDrawable();
            drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

            LayerDrawable stars = (LayerDrawable) rtbReview.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        } else {
            //stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            Drawable drawable = rtbReview.getProgressDrawable();
            drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }

        etReview = (EditText) view.findViewById(R.id.etReview);
        txtReviewClose = (TextView) view.findViewById(R.id.txtReviewClose);
        txtReviewSubmit = (TextView) view.findViewById(R.id.txtReviewSubmit);

        txtReviewClose.setOnClickListener(this);
        txtReviewSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        view = v;
        if (v.getId() == R.id.txtReviewClose) {
            dismiss();
        } else if (v.getId() == R.id.txtReviewSubmit) {
            ReuestWriteReview();
        }
    }

    @Override
    public void InsertReviewMasterResponse(String errorCode, ReviewMaster objReviewMaster) {
        SetError(errorCode);
    }

    //region Private Methods
    private void ReuestWriteReview() {
        objReviewMaster = new ReviewMaster();
        objSharePreferenceManage = new SharePreferenceManage();
        ReviewJSONParser objReviewJSONParser = new ReviewJSONParser();

        objReviewMaster.setStarRating(rtbReview.getRating());
        objReviewMaster.setReview(etReview.getText().toString());
        objReviewMaster.setIsShow(false);
        objReviewMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
        objReviewMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);

        objReviewJSONParser.InsertReviewMaster(objReviewMaster, getActivity(), this);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "0":
                Globals.ShowSnackBar(view, getResources().getString(R.string.MsgReviewInsertSuccess), getActivity(), 1000);
                dismiss();
                break;
        }
    }
    //endregion
}
