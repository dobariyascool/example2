package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.ReviewAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ReviewMaster;
import com.arraybit.parser.ReviewJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ReviewFragment extends Fragment implements ReviewJSONParser.ReviewMasterRequestListener, ReviewAdapter.WriteReviewListener {

    static ArrayList<ReviewMaster> lstReviewMaster;
    RecyclerView rvReview;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    LinearLayoutManager linearLayoutManager;
    BusinessMaster objBusinessMaster;
    ReviewAdapter adapter;
    int currentPage = 1;

    public ReviewFragment(BusinessMaster objBusinessMaster) {
        this.objBusinessMaster = objBusinessMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        rvReview = (RecyclerView) view.findViewById(R.id.rvReview);
        rvReview.setNestedScrollingEnabled(false);
        rvReview.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            currentPage = 1;
            RequestReviews();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
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
        return view;
    }

    @Override
    public void ReviewMasterResponse(ArrayList<ReviewMaster> alReviewMaster) {
        if(currentPage > 1) {
            progressDialog.dismiss();
        }
        lstReviewMaster = alReviewMaster;
        SetRecyclerView(lstReviewMaster);
    }

    @Override
    public void AddReview() {
        WriteReviewFragment objWriteReviewFragment = new WriteReviewFragment();
        objWriteReviewFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    //region Private Methods
    private void RequestReviews() {
        if(currentPage > 1) {
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }
        ReviewJSONParser objReviewJSONParser = new ReviewJSONParser();
        objReviewJSONParser.SelectAllReviewMasterPageWise(this, getActivity(), String.valueOf(currentPage), String.valueOf(objBusinessMaster.getBusinessMasterId()));
    }

    private void SetRecyclerView(ArrayList<ReviewMaster> alReviewMaster) {
        if (alReviewMaster == null) {
            if (currentPage == 1) {
                //txtMsg.setVisibility(View.GONE);
                //txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
            }
        } else if (alReviewMaster.size() == 0) {
            if (currentPage == 1) {
                //txtMsg.setVisibility(View.GONE);
                //txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
            }
        } else {
            rvReview.setVisibility(View.VISIBLE);
            //txtMsg.setVisibility(View.GONE);
            if (currentPage > 1) {
                adapter.ReviewDataChanged(alReviewMaster);
                return;
            } else if (alReviewMaster.size() < 10) {
                currentPage += 1;
            }
            adapter = new ReviewAdapter(getActivity(), alReviewMaster, this);
            rvReview.setAdapter(adapter);
            rvReview.setLayoutManager(linearLayoutManager);
        }
    }
    //endregion
}
