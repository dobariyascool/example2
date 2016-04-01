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
    String que;
    int cnt;

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
        if (alBusinessInfoQuestionMaster != null && alBusinessInfoQuestionMaster.size() != 0) {
            rvBusinessInfo.setVisibility(View.VISIBLE);
            ArrayList<BusinessInfoAnswerMaster> alBusinessInfoAnswerMaster = new ArrayList<>();
            lstBusinessInfoQuestionMaster = new ArrayList<>();
            que = "";
            cnt = 0;
            boolean isAnswer = false;
            for (BusinessInfoQuestionMaster objQuestionMaster : alBusinessInfoQuestionMaster) {
                if (que.equals("")) {
                    que = objQuestionMaster.getQuestion();
                    objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                    objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                    objBusinessInfoQuestionMaster = objQuestionMaster;
                    objBusinessInfoAnswerMaster.setAnswer(objQuestionMaster.getAnswer());
                    objBusinessInfoAnswerMaster.setIsAnswer(objQuestionMaster.getIsAnswer());
                    if(objQuestionMaster.getIsAnswer() && objQuestionMaster.getQuestionType()!= Globals.QuestionType.Input.getValue()){
                        isAnswer = true;
                    }else if(objQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue()){
                        isAnswer = true;
                    }
                    alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                    if (cnt == alBusinessInfoQuestionMaster.size() - 1) {
                        if(isAnswer) {
                            isAnswer = false;
                            objBusinessInfoQuestionMaster.setAlBusinessInfoAnswerMaster(alBusinessInfoAnswerMaster);
                            lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                        }
                    }
                } else {
                    if (que.equals(objQuestionMaster.getQuestion())) {
                        objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                        objBusinessInfoAnswerMaster.setAnswer(objQuestionMaster.getAnswer());
                        objBusinessInfoAnswerMaster.setIsAnswer(objQuestionMaster.getIsAnswer());
                        if(objQuestionMaster.getIsAnswer()){
                            isAnswer = true;
                        }else if(objQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue()){
                            isAnswer = true;
                        }
                        alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                        if (cnt == alBusinessInfoQuestionMaster.size() - 1) {
                            if(isAnswer) {
                                isAnswer = false;
                                objBusinessInfoQuestionMaster.setAlBusinessInfoAnswerMaster(alBusinessInfoAnswerMaster);
                                lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                            }
                        }
                    } else {
                        if(isAnswer) {
                            isAnswer = false;
                            objBusinessInfoQuestionMaster.setAlBusinessInfoAnswerMaster(alBusinessInfoAnswerMaster);
                            lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                        }
                        alBusinessInfoAnswerMaster = new ArrayList<>();
                        que = objQuestionMaster.getQuestion();
                        objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                        objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
                        objBusinessInfoQuestionMaster = objQuestionMaster;
                        objBusinessInfoAnswerMaster.setAnswer(objQuestionMaster.getAnswer());
                        objBusinessInfoAnswerMaster.setIsAnswer(objQuestionMaster.getIsAnswer());
                        if(objQuestionMaster.getIsAnswer()){
                            isAnswer = true;
                        }else if(objQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue()){
                            isAnswer = true;
                        }
                        alBusinessInfoAnswerMaster.add(objBusinessInfoAnswerMaster);
                        if (cnt == alBusinessInfoQuestionMaster.size() - 1) {
                            if(isAnswer){
                                isAnswer = false;
                                objBusinessInfoQuestionMaster.setAlBusinessInfoAnswerMaster(alBusinessInfoAnswerMaster);
                                lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                            }
                        }
                    }
                }
                cnt++;
            }
            adapter = new BusinessInfoAdapter(getActivity(), lstBusinessInfoQuestionMaster);
            rvBusinessInfo.setAdapter(adapter);
            rvBusinessInfo.setLayoutManager(linearLayoutManager);
        }
    }
}
//endregion

