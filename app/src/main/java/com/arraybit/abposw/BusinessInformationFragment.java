package com.arraybit.abposw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.BusinessInfoAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessInfoAnswerMaster;
import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.arraybit.parser.BusinessInfoQuestionJSONParser;

import java.util.ArrayList;

public class BusinessInformationFragment extends Fragment implements BusinessInfoQuestionJSONParser.BusinessInfoMasterRequestListener {

    RecyclerView rvBusinessInfo;
    LinearLayoutManager linearLayoutManager;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    BusinessInfoAdapter adapter;
    ArrayList<BusinessInfoQuestionMaster> lstBusinessInfoQuestionMaster;
    BusinessInfoQuestionMaster objBusinessInfoQuestionMaster;
    BusinessInfoAnswerMaster objBusinessInfoAnswerMaster;

    public BusinessInformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_information, container, false);

        rvBusinessInfo = (RecyclerView) view.findViewById(R.id.rvBusinessInfo);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            RequestBusinessInfo();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        rvBusinessInfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    @Override
    public void BusinessInfoMasterResponse(ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alBusinessInfoQuestionMaster);
    }

    //region Private Methods
    private void RequestBusinessInfo() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        BusinessInfoQuestionJSONParser objBusinessInfoQuestionJSONParser = new BusinessInfoQuestionJSONParser();
        objBusinessInfoQuestionJSONParser.SelectAllBusinessInfoQuestionMaster(this, getActivity(), Globals.linktoBusinessTypeMasterId, Globals.linktoBusinessMasterId);
    }

    private void SetRecyclerView(ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster) {
        if (alBusinessInfoQuestionMaster == null) {

        } else if (alBusinessInfoQuestionMaster.size() == 0) {

        } else {
            rvBusinessInfo.setVisibility(View.VISIBLE);
            String que = "";
            ArrayList<BusinessInfoAnswerMaster> alBusinessInfoAnswerMaster = new ArrayList<>();
            lstBusinessInfoQuestionMaster = new ArrayList<>();
            for (BusinessInfoQuestionMaster i : alBusinessInfoQuestionMaster) {
                if (que.equals("")) {
                    que = i.getQuestion();
                    objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                    objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                    objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(i.getBusinessInfoQuestionMasterId());
                    objBusinessInfoQuestionMaster.setQuestion(i.getQuestion());
                    objBusinessInfoAnswerMaster.setAnswer(i.getAnswer());
                    objBusinessInfoAnswerMaster.setIsAnswer(i.getIsAnswer());
                    alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                } else {
                    if (que.equals(i.getQuestion())) {
                        objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                        objBusinessInfoAnswerMaster.setAnswer(i.getAnswer());
                        objBusinessInfoAnswerMaster.setIsAnswer(i.getIsAnswer());
                        alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                    } else {
                        objBusinessInfoQuestionMaster.setAlBusinessInfoAnswerMaster(alBusinessInfoAnswerMaster);
                        lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                        alBusinessInfoAnswerMaster = new ArrayList<>();
                        que = i.getQuestion();
                        objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                        objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                        objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(i.getBusinessInfoQuestionMasterId());
                        objBusinessInfoQuestionMaster.setQuestion(i.getQuestion());
                        objBusinessInfoAnswerMaster.setAnswer(i.getAnswer());
                        objBusinessInfoAnswerMaster.setIsAnswer(i.getIsAnswer());
                        alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                    }
                }
            }
            adapter = new BusinessInfoAdapter(getActivity(), lstBusinessInfoQuestionMaster);
            rvBusinessInfo.setAdapter(adapter);
            rvBusinessInfo.setLayoutManager(linearLayoutManager);
        }
    }
}
//endregion

