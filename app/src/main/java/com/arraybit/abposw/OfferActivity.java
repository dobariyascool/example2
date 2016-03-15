package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.adapter.OfferAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.OfferJSONParser;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class OfferActivity extends AppCompatActivity implements OfferJSONParser.OfferRequestListener {

    LinearLayout errorLayout;
    RecyclerView rvOffer;
    int currentPage = 1;
    OfferAdapter offerAdapter;
    LinearLayoutManager linearLayoutManager;
    com.arraybit.abposw.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        CoordinatorLayout offerLayout = (CoordinatorLayout) findViewById(R.id.offerLayout);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);

        rvOffer = (RecyclerView) findViewById(R.id.rvOffer);
        rvOffer.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(this);

        if (Service.CheckNet(this)) {
            RequestOfferMaster();
        } else {
            Globals.ShowSnackBar(offerLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        rvOffer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                offerAdapter.isItemAnimate = true;
            }
        });

        rvOffer.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                offerAdapter.isItemAnimate = true;
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(OfferActivity.this)) {
                        RequestOfferMaster();
                    } else {
                        Globals.ShowSnackBar(rvOffer, getResources().getString(R.string.MsgCheckConnection), OfferActivity.this, 1000);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alOfferMaster);
    }

    //region Private Methods

    private void RequestOfferMaster() {
        progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");

        OfferJSONParser objOfferJSONParser = new OfferJSONParser();
        objOfferJSONParser.SelectAllOfferMasterByFromDate(String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), OfferActivity.this);
    }

    private void SetRecyclerView(ArrayList<OfferMaster> lstOfferMaster) {
        if (lstOfferMaster == null) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvOffer);
            }
        } else if (lstOfferMaster.size() == 0) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvOffer);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvOffer);
            if (currentPage > 1) {
                offerAdapter.OffersDataChanged(lstOfferMaster);
                return;
            } else if (lstOfferMaster.size() < 10) {
                currentPage += 1;
            }
            offerAdapter = new OfferAdapter(OfferActivity.this, lstOfferMaster);
            rvOffer.setAdapter(offerAdapter);
            rvOffer.setLayoutManager(linearLayoutManager);
        }
    }

    //endregion
}
