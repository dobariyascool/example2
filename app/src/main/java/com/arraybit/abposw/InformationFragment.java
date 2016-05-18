package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.BusinessServiceAdapter;
import com.arraybit.adapter.WorkingHoursAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.BusinessServiceTran;
import com.arraybit.parser.BusinessHoursJSONParser;
import com.arraybit.parser.BusinessServiceJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class InformationFragment extends Fragment implements BusinessHoursJSONParser.BusinessHoursRequestListener,BusinessServiceJSONParser.BusinessServiceRequestListener{

    static ArrayList<BusinessHoursTran> lstBusinessHoursTran;
    static ArrayList<BusinessServiceTran> lstBusinessService;
    ProgressDialog progressDialog = new ProgressDialog();
    RecyclerView rvWorkingHours,rvService;
    TextView txtAddress, txtPhone1,txtPhone2, txtEmail, txtWebSite,txtFax;
    WorkingHoursAdapter adapter;
    BusinessServiceAdapter businessServiceAdapter;
    BusinessMaster objBusinessMaster;
    LinearLayout callLayout, emailLayout, siteLayout,faxLayout,emailDivider,webSiteDivider,faxDivider,informationFragment,headerServiceLayout;
    ImageView ivCall;

    public InformationFragment(BusinessMaster objBusinessMaster) {
        this.objBusinessMaster = objBusinessMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_information, container, false);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtPhone1 = (TextView) view.findViewById(R.id.txtPhone1);
        txtPhone2 = (TextView) view.findViewById(R.id.txtPhone2);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtWebSite = (TextView) view.findViewById(R.id.txtWebSite);
        txtFax = (TextView) view.findViewById(R.id.txtFax);

        ivCall = (ImageView)view.findViewById(R.id.ivCall);

        callLayout = (LinearLayout) view.findViewById(R.id.callLayout);
        emailLayout = (LinearLayout) view.findViewById(R.id.emailLayout);
        siteLayout = (LinearLayout) view.findViewById(R.id.siteLayout);
        faxLayout = (LinearLayout) view.findViewById(R.id.faxLayout);
        emailDivider = (LinearLayout) view.findViewById(R.id.emailDivider);
        webSiteDivider = (LinearLayout) view.findViewById(R.id.webSiteDivider);
        faxDivider = (LinearLayout) view.findViewById(R.id.faxDivider);
        informationFragment = (LinearLayout) view.findViewById(R.id.informationFragment);
        headerServiceLayout = (LinearLayout) view.findViewById(R.id.headerServiceLayout);

        rvWorkingHours = (RecyclerView) view.findViewById(R.id.rvWorkingHours);
        rvService = (RecyclerView) view.findViewById(R.id.rvService);
        rvService.setNestedScrollingEnabled(false);
        rvWorkingHours.setNestedScrollingEnabled(false);
        rvWorkingHours.setVisibility(View.GONE);
        rvService.setVisibility(View.GONE);

        if (lstBusinessHoursTran == null) {
            SetContactDetails();
            if (Service.CheckNet(getActivity())) {
                RequestBusinessServices();
            } else {
                Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else {
            SetContactDetails();
            SetServiceRecyclerView(lstBusinessService);
            SetWorkingHoursRecyclerView(lstBusinessHoursTran);
        }

        return view;
    }

    @Override
    public void BusinessHoursResponse(ArrayList<BusinessHoursTran> alBusinessHoursTran) {
        progressDialog.dismiss();
        lstBusinessHoursTran = alBusinessHoursTran;
        SetWorkingHoursRecyclerView(lstBusinessHoursTran);
    }


    @Override
    public void BusinessServiceResponse(ArrayList<BusinessServiceTran> alBusinessServiceTran) {
        progressDialog.dismiss();
        lstBusinessService = alBusinessServiceTran;
        SetServiceRecyclerView(lstBusinessService);
        if (Service.CheckNet(getActivity())) {
            RequestBusinessHours();
        } else {
            Globals.ShowSnackBar(informationFragment, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }
    }

    //region Private Methods
    private void RequestBusinessHours(){
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BusinessHoursJSONParser objBusinessHoursJSONParser = new BusinessHoursJSONParser();
        objBusinessHoursJSONParser.SelectAllBusinessHours(this, getActivity(), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void RequestBusinessServices(){
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BusinessServiceJSONParser objBusinessServiceJSONParser = new BusinessServiceJSONParser();
        objBusinessServiceJSONParser.SelectAllBusinessService(getActivity(), this,String.valueOf(Globals.linktoBusinessTypeMasterId),String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetWorkingHoursRecyclerView(ArrayList<BusinessHoursTran> lstBusinessHoursTran) {
        if (lstBusinessHoursTran == null) {
            rvWorkingHours.setVisibility(View.GONE);
        } else if (lstBusinessHoursTran.size() == 0) {
            rvWorkingHours.setVisibility(View.GONE);
        }else {
            rvWorkingHours.setVisibility(View.VISIBLE);
            adapter = new WorkingHoursAdapter(getActivity(), lstBusinessHoursTran);
            rvWorkingHours.setAdapter(adapter);
            rvWorkingHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetServiceRecyclerView(ArrayList<BusinessServiceTran> lstBusinessServiceTran) {
        if(lstBusinessServiceTran==null){
            rvService.setVisibility(View.GONE);
            headerServiceLayout.setVisibility(View.GONE);
        }else if(lstBusinessServiceTran.size()==0){
            rvService.setVisibility(View.GONE);
            headerServiceLayout.setVisibility(View.GONE);
        }else {
            rvService.setVisibility(View.VISIBLE);
            headerServiceLayout.setVisibility(View.VISIBLE);
            businessServiceAdapter = new BusinessServiceAdapter(getActivity(), lstBusinessServiceTran);
            rvService.setAdapter(businessServiceAdapter);
            rvService.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }
    }


    private void SetContactDetails() {
        if (objBusinessMaster.getAddress() == null) {
            txtAddress.setVisibility(View.GONE);
        } else {
            txtAddress.setVisibility(View.VISIBLE);
            txtAddress.setText(objBusinessMaster.getAddress());
        }
        if (objBusinessMaster.getPhone1().equals("") && objBusinessMaster.getPhone2().equals("")) {
            callLayout.setVisibility(View.GONE);
        } else if(!objBusinessMaster.getPhone2().equals("") && !objBusinessMaster.getPhone1().equals("")) {
            callLayout.setVisibility(View.VISIBLE);
            txtPhone2.setVisibility(View.VISIBLE);
            txtPhone1.setVisibility(View.VISIBLE);
            txtPhone2.setText(objBusinessMaster.getPhone2());
            txtPhone1.setText(objBusinessMaster.getPhone1());
        }else if(objBusinessMaster.getPhone2().equals("") && !objBusinessMaster.getPhone1().equals(""))  {
            callLayout.setVisibility(View.VISIBLE);
            txtPhone2.setVisibility(View.GONE);
            txtPhone1.setVisibility(View.VISIBLE);
            txtPhone1.setText(objBusinessMaster.getPhone1());
        }else if(objBusinessMaster.getPhone1().equals("") && !objBusinessMaster.getPhone2().equals("")) {
            callLayout.setVisibility(View.VISIBLE);
            txtPhone1.setVisibility(View.GONE);
            txtPhone2.setVisibility(View.VISIBLE);
            txtPhone2.setText(objBusinessMaster.getPhone2());
        }
        if (objBusinessMaster.getWebsite().equals("")) {
            siteLayout.setVisibility(View.GONE);
            webSiteDivider.setVisibility(View.GONE);
        } else {
            siteLayout.setVisibility(View.VISIBLE);
            webSiteDivider.setVisibility(View.VISIBLE);
            txtWebSite.setText(objBusinessMaster.getWebsite());
        }
        if (objBusinessMaster.getEmail().equals("")) {
            emailLayout.setVisibility(View.GONE);
            emailDivider.setVisibility(View.GONE);
        } else {
            emailLayout.setVisibility(View.VISIBLE);
            emailDivider.setVisibility(View.VISIBLE);
            txtEmail.setText(objBusinessMaster.getEmail());
        }
        if (objBusinessMaster.getFax().equals("")) {
            faxLayout.setVisibility(View.GONE);
            faxDivider.setVisibility(View.GONE);
        } else {
            faxLayout.setVisibility(View.VISIBLE);
            faxDivider.setVisibility(View.VISIBLE);
            txtFax.setText(objBusinessMaster.getFax());
        }
    }
    //endregion

}
