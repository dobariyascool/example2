package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.arraybit.adapter.ReviewAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ReviewMaster;
import com.arraybit.parser.ReviewJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ReviewFragment extends Fragment implements ReviewJSONParser.ReviewMasterRequestListener, ReviewAdapter.ReviewListener {

    RecyclerView rvReview;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayoutManager linearLayoutManager;
    BusinessMaster objBusinessMaster;
    ReviewAdapter adapter;
    int currentPage = 1;
    boolean isPause, isDataAppend;
    double totalReview;
    RelativeLayout reviewLayout;
    TextView txtAverage, txtTotal;
    RatingBar rtbReview;
    Button btnReview;
    LinearLayout errorLayout;

    public ReviewFragment(BusinessMaster objBusinessMaster) {
        this.objBusinessMaster = objBusinessMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        reviewLayout = (RelativeLayout) view.findViewById(R.id.reviewLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        txtAverage = (TextView) view.findViewById(R.id.txtAverage);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);

        rtbReview = (RatingBar) view.findViewById(R.id.rtbReview);

        LayerDrawable stars = (LayerDrawable) rtbReview.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        rvReview = (RecyclerView) view.findViewById(R.id.rvReview);
        rvReview.setNestedScrollingEnabled(false);
        rvReview.setVisibility(View.GONE);

        btnReview = (Button) view.findViewById(R.id.btnReview);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            reviewLayout.setVisibility(View.VISIBLE);
            currentPage = 1;
            RequestReviews();
        } else {
            reviewLayout.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvReview, R.drawable.wifi_drawable);
            //Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        rvReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        rvReview.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestReviews();
                    } else {
                        Globals.ShowSnackBar(rvReview, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()) == null) {
                    isPause = true;
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    WriteReviewFragment objWriteReviewFragment = new WriteReviewFragment();
                    objWriteReviewFragment.show(getActivity().getSupportFragmentManager(), "");
                }
            }
        });
        return view;
    }

    @Override
    public void ReviewMasterResponse(ArrayList<ReviewMaster> alReviewMaster) {
        if (currentPage > 3) {
            progressDialog.dismiss();
        }
        SetRecyclerView(alReviewMaster);
    }

    @Override
    public void AverageReview(String average, String totalUser) {
        txtAverage.setText(average);
        txtTotal.setText(String.format(getActivity().getString(R.string.rfAverageReview), totalUser));
        rtbReview.setRating(Float.parseFloat(average));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()) != null) {
                WriteReviewFragment objWriteReviewFragment = new WriteReviewFragment();
                objWriteReviewFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        }
    }

    //region Private Methods
    private void RequestReviews() {
        if (currentPage > 3) {
            progressDialog.show(getFragmentManager(), "");
        }
        ReviewJSONParser objReviewJSONParser = new ReviewJSONParser();
        objReviewJSONParser.SelectAllReviewMasterPageWise(this, getActivity(), String.valueOf(currentPage), String.valueOf(objBusinessMaster.getBusinessMasterId()));
    }

    private void SetRecyclerView(ArrayList<ReviewMaster> alReviewMaster) {
        if (alReviewMaster == null) {
            if (currentPage == 1) {
                reviewLayout.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvReview, 0);
            }
        } else if (alReviewMaster.size() == 0) {
            if (currentPage == 1) {
                reviewLayout.setVisibility(View.GONE);
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvReview, 0);
            }
        } else {
            reviewLayout.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, false, null, rvReview, 0);
            if (currentPage > 1) {
                adapter.ReviewDataChanged(alReviewMaster);
                isDataAppend = true;
                return;
            } else if (alReviewMaster.size() < 10) {
                currentPage += 1;
            }
            adapter = new ReviewAdapter(getActivity(), alReviewMaster, this);
            rvReview.setAdapter(adapter);
            rvReview.setLayoutManager(linearLayoutManager);
            if (!isDataAppend) {
                SetAverageReview(alReviewMaster);
                isDataAppend = false;
            }
        }
    }

    private void SetAverageReview(ArrayList<ReviewMaster> alReviewMaster) {
        totalReview = 0;
        for (ReviewMaster reviewMaster : alReviewMaster) {
            totalReview = totalReview + reviewMaster.getStarRating();
        }
        totalReview = totalReview / alReviewMaster.size();
        txtAverage.setText(Globals.dfWithOnePrecision.format(totalReview));
        txtTotal.setText(String.format(getActivity().getString(R.string.rfAverageReview), alReviewMaster.size()));
        rtbReview.setRating((float) totalReview);
    }
    //endregion
}
