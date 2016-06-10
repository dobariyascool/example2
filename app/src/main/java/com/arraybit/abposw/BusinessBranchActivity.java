package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.arraybit.adapter.BusinessBranchAdapter;
import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class BusinessBranchActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener,BusinessBranchAdapter.BranchSelectorListener {

    AppCompatSpinner spOrderCity;
    EditText etBusinessGroupName;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isCityFilter;
    short businessGroupMasterId;
    ArrayList<BusinessMaster> alBusinessMaster;
    LinearLayout businessBranchLayout;
    RecyclerView rvBusinessBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_branch);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        businessBranchLayout = (LinearLayout) findViewById(R.id.businessBranchLayout);
        Globals.SetScaleImageBackground(BusinessBranchActivity.this,businessBranchLayout,null,null);

        businessGroupMasterId = getIntent().getShortExtra("linktoBusinessGroupMasterId", (short) 0);

        spOrderCity = (AppCompatSpinner) findViewById(R.id.spOrderCity);
        etBusinessGroupName = (EditText) findViewById(R.id.etBusinessGroupName);

        rvBusinessBranch = (RecyclerView) findViewById(R.id.rvBusinessBranch);

        if (Service.CheckNet(BusinessBranchActivity.this)) {
            RequestBusinessMaster(businessGroupMasterId, null);
        } else {
            Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
        }

        spOrderCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView spinnerText = (TextView) view.findViewById(R.id.txtSpinnerItem);
                if (spinnerText != null) {
                    spinnerText.setTextColor(ContextCompat.getColor(BusinessBranchActivity.this, R.color.white_blur));
                }
                if (Service.CheckNet(BusinessBranchActivity.this)) {
                    isCityFilter = true;
                    RequestBusinessMaster(alBusinessMaster.get(0).getLinktoBusinessGroupMasterId(), (String) parent.getAdapter().getItem(position));
                } else {
                    Globals.ShowSnackBar(businessBranchLayout, getResources().getString(R.string.MsgCheckConnection), BusinessBranchActivity.this, 1000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        this.alBusinessMaster = alBusinessMaster;
        if(isCityFilter){
            SetRecyclerView();
        }else{
            FillCity();
        }

    }

    @Override
    public void BranchSelectorOnClickListener(BusinessMaster objBusinessMaster) {
        Globals.linktoBusinessMasterId = objBusinessMaster.getBusinessMasterId();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.CreatePreference("BusinessPreference", "BusinessMasterId", String.valueOf(objBusinessMaster.getBusinessMasterId()), BusinessBranchActivity.this);
        setResult(RESULT_OK);
        finish();
    }

    private void RequestBusinessMaster(short linktoBusinessGroupMasterId, String city) {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        if (linktoBusinessGroupMasterId != 0) {
            objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(BusinessBranchActivity.this, String.valueOf(linktoBusinessGroupMasterId), city);
        }

    }

    private void FillCity() {
        ArrayList<SpinnerItem> alCity = new ArrayList<>();
        short cnt = 0;
        boolean isDuplicate = false;
        for (BusinessMaster objBusiness : alBusinessMaster) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            if (alCity.size() == 0) {
                objSpinnerItem.setText(objBusiness.getCity());
                objSpinnerItem.setValue(cnt);
                alCity.add(objSpinnerItem);
            } else {
                for (SpinnerItem objSpinner : alCity) {
                    if (objSpinner.getText().equals(objBusiness.getCity())) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    objSpinnerItem.setText(objBusiness.getCity());
                    objSpinnerItem.setValue(cnt);
                    alCity.add(objSpinnerItem);
                }
            }
            cnt++;
        }
        SpinnerAdapter cityAdapter = new SpinnerAdapter(BusinessBranchActivity.this, alCity, true);
        spOrderCity.setAdapter(cityAdapter);
        etBusinessGroupName.setText(alBusinessMaster.get(0).getBusinessGroup());
    }

    private void SetRecyclerView() {
        if (alBusinessMaster != null && alBusinessMaster.size() != 0) {
            BusinessBranchAdapter adapter = new BusinessBranchAdapter(BusinessBranchActivity.this, alBusinessMaster, this);
            rvBusinessBranch.setVisibility(View.VISIBLE);
            rvBusinessBranch.setAdapter(adapter);
            rvBusinessBranch.setLayoutManager(new LinearLayoutManager(BusinessBranchActivity.this));
        }
    }

}
