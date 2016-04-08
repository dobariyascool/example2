package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.adapter.BusinessDescriptionAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class AboutUsActivity extends AppCompatActivity implements BusinessDescriptionJSONParser.BusinessDescriptionRequestListener {
    RecyclerView rvBusinessDescription;
    ArrayList<BusinessDescription> lstBusinessDescription;
    LinearLayout errorLayout;
    BusinessDescription objBusinessDescription;
    BusinessDescriptionAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        rvBusinessDescription = (RecyclerView) findViewById(R.id.rvBusinessDescription);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        CoordinatorLayout AboutUsLayout = (CoordinatorLayout) findViewById(R.id.offerLayout);
        linearLayoutManager = new LinearLayoutManager(this);

        if (Service.CheckNet(this)) {
            RequestBusinessInfo();
        } else {
            Globals.ShowSnackBar(AboutUsLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void BusinessDescriptionResponse(ArrayList<BusinessDescription> alBusinessDescription) {
        SetRecyclerView(lstBusinessDescription);
    }

    //region Private Methods
    private void RequestBusinessInfo() {
        BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
        objBusinessDescriptionJSONParser.SelectAllBusinessDescription(AboutUsActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetRecyclerView(ArrayList<BusinessDescription> lstBusinessDescription) {
        if (lstBusinessDescription == null) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvBusinessDescription);
        } else if (lstBusinessDescription.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvBusinessDescription);
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvBusinessDescription);
            adapter.notifyDataChanged(lstBusinessDescription);
            adapter = new BusinessDescriptionAdapter(AboutUsActivity.this, lstBusinessDescription);
            rvBusinessDescription.setAdapter(adapter);
            rvBusinessDescription.setLayoutManager(linearLayoutManager);
            return;

        }
    }

}
