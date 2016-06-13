package com.arraybit.abposw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.BusinessInfoAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.arraybit.parser.BusinessInfoQuestionJSONParser;

import java.util.ArrayList;

public class BusinessInformationFragment extends Fragment implements BusinessInfoQuestionJSONParser.BusinessInfoMasterRequestListener {

    RecyclerView rvBusinessInfo;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog = new ProgressDialog();
    BusinessInfoAdapter adapter;
    LinearLayout errorLayout;

    public BusinessInformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_information, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        rvBusinessInfo = (RecyclerView) view.findViewById(R.id.rvBusinessInfo);
        rvBusinessInfo.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            RequestBusinessInfo();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvBusinessInfo, R.drawable.wifi_drawable);
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
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvBusinessInfo, 0);
        } else if (alBusinessInfoQuestionMaster.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvBusinessInfo, 0);
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvBusinessInfo, 0);
            adapter = new BusinessInfoAdapter(getActivity(), alBusinessInfoQuestionMaster);
            rvBusinessInfo.setVisibility(View.VISIBLE);
            rvBusinessInfo.setAdapter(adapter);
            rvBusinessInfo.setLayoutManager(linearLayoutManager);
        }
    }

    //endregion
}

