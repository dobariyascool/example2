package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arraybit.adapter.NotificationAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.modal.NotificationTran;
import com.arraybit.parser.NotificationJSONParser;
import com.arraybit.parser.NotificationTranJSONParser;
import com.rey.material.widget.CompoundButton;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class NotificationActivity extends AppCompatActivity implements NotificationJSONParser.NotificationRequestListener, NotificationAdapter.OnClickListener, NotificationTranJSONParser.NotificationInsertListener {

    RelativeLayout loginLayout;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    LinearLayout notificationLayout, errorLayout;
    boolean isLogin, isStart = false;
    ProgressDialog progressDialog = new ProgressDialog();
    int CurrentPage = 1, position;
    NotificationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvNotification;
    NotificationMaster objNotificationMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        isStart = getIntent().getBooleanExtra("isStart", false);

        rvNotification = (RecyclerView) findViewById(R.id.rvNotification);
        notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        linearLayoutManager = new LinearLayoutManager(this);

        Globals.SetScaleImageBackground(NotificationActivity.this, notificationLayout, null, null);

        CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);

        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
        if (Service.CheckNet(this)) {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", NotificationActivity.this) != null) {
                loginLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
//                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
                RequestOfferMaster();
            } else {
                loginLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
//                loginLayout.setVisibility(View.GONE);
//                errorLayout.setVisibility(View.VISIBLE);
//                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
            }
        } else {
            loginLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
        }

        cbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        rvNotification.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page > CurrentPage) {
                    CurrentPage = current_page;
                    if (Service.CheckNet(NotificationActivity.this)) {
                        RequestOfferMaster();
                    } else {
                        Toast.makeText(NotificationActivity.this, getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    if (data.getBooleanExtra("IsShowMessage", false)) {
                        Globals.ShowSnackBar(notificationLayout, getResources().getString(R.string.siLoginSuccessMsg), NotificationActivity.this, 2000);
                        isLogin = true;
                        if (Service.CheckNet(this)) {
                            if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", NotificationActivity.this) != null) {
                                loginLayout.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.GONE);
//                                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
                                RequestOfferMaster();
                            } else {
                                loginLayout.setVisibility(View.VISIBLE);
                                errorLayout.setVisibility(View.GONE);
                            }
                        } else {
                            loginLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (isStart) {
                Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                Intent intent = new Intent();
                intent.putExtra("IsLogin", isLogin);
                setResult(RESULT_OK);
                finish();
//                overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void NotificationResponse(ArrayList<NotificationMaster> alNotificationMasters, NotificationMaster objNotificationMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alNotificationMasters);
    }

    @Override
    public void OnCardClick(NotificationMaster objNotificationMaster, int position) {
//        adapter.NotificationDataRead(position);
        this.objNotificationMaster = objNotificationMaster;
        this.position = position;
        if (objNotificationMaster.getIsRead() == 0) {
            if (Service.CheckNet(this)) {
                InsertNotification();
            } else {
                Globals.ShowSnackBar(errorLayout, getResources().getString(R.string.MsgCheckConnection), NotificationActivity.this, 1000);
            }
        } else
        {
            RedirectOnActivity();
        }
    }

    @Override
    public void NotificationResponse(String errorCode) {
        progressDialog.dismiss();
        if (errorCode.equals("0")) {
            adapter.NotificationDataRead(position);
            RedirectOnActivity();
        }
    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            Intent intent = new Intent();
            intent.putExtra("IsLogin", isLogin);
            setResult(RESULT_OK);
            super.onBackPressed();
        }
    }

    private void RequestOfferMaster() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        NotificationJSONParser objNotificationJSONParser = new NotificationJSONParser();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this) != null
                && !objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this).equals("")) {
            objNotificationJSONParser.SelectAllNotificationMasterPageWise(String.valueOf(CurrentPage), this, objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this));
        } else {
            objNotificationJSONParser.SelectAllNotificationMasterPageWise(String.valueOf(CurrentPage), this, String.valueOf(0));
        }
    }

    private void InsertNotification() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        progressDialog.show(getSupportFragmentManager(), "");
        NotificationTran objNotificationTran = new NotificationTran();
        objNotificationTran.setlinktoNotificationMasterId(objNotificationMaster.getNotificationMasterId());
        NotificationTranJSONParser objNotificationTranJSONParser = new NotificationTranJSONParser();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this) != null
                && !objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this).equals("")) {
            objNotificationTran.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", NotificationActivity.this)));
        }
        objNotificationTranJSONParser.InsertNotificationTran(objNotificationTran, NotificationActivity.this);
    }

    private void SetRecyclerView(ArrayList<NotificationMaster> lstNotificationMasters) {
        if (lstNotificationMasters == null) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvNotification, 0);
            }
        } else if (lstNotificationMasters.size() == 0) {
            if (CurrentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), rvNotification, 0);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvNotification, 0);
            if (CurrentPage > 1) {
                adapter.NotificationDataChanged(lstNotificationMasters);
                return;
            } else if (lstNotificationMasters.size() < 10) {
                CurrentPage += 1;
            }
            adapter = new NotificationAdapter(NotificationActivity.this, lstNotificationMasters, this);
            rvNotification.setAdapter(adapter);
            rvNotification.setLayoutManager(linearLayoutManager);
        }
    }

    private void RedirectOnActivity()
    {
        if (objNotificationMaster != null) {
            if (objNotificationMaster.getType() == Globals.BannerType.Offer.getValue() && objNotificationMaster.getID() > 0) {
                Intent intent = new Intent(NotificationActivity.this, OfferDetailActivity.class);
                intent.putExtra("OfferMasterId", objNotificationMaster.getID());
                startActivity(intent);
            } else if (objNotificationMaster.getType() == Globals.BannerType.Item.getValue() && objNotificationMaster.getID() > 0) {
                Intent intent = new Intent(NotificationActivity.this, DetailActivity.class);
                intent.putExtra("ItemMasterId", objNotificationMaster.getID());
                intent.putExtra("isBannerClick", true);
                startActivity(intent);
            } else if (objNotificationMaster.getType() == Globals.BannerType.Category.getValue() && objNotificationMaster.getID() > 0) {
                Intent intent = new Intent(NotificationActivity.this, ItemListActivity.class);
                intent.putExtra("CateoryMasterId", objNotificationMaster.getID());
                intent.putExtra("isBannerClick", true);
                startActivity(intent);
            } else {

            }
        }
    }
}
