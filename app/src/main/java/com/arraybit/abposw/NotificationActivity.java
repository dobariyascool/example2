package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.rey.material.widget.CompoundButton;

@SuppressWarnings("ConstantConditions")
public class NotificationActivity extends AppCompatActivity {

    RelativeLayout loginLayout;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    LinearLayout notificationLayout, errorLayout;
    boolean isLogin;

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

        notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);

        CompoundButton cbSignIn = (CompoundButton) findViewById(R.id.cbSignIn);

        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
        if (Service.CheckNet(this)) {
            if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", NotificationActivity.this) != null) {
                loginLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
            } else {
//                loginLayout.setVisibility(View.VISIBLE);
//                errorLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
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
                                errorLayout.setVisibility(View.VISIBLE);
                                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNotification), null, 0);
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
            Intent intent = new Intent();
            intent.putExtra("IsLogin",isLogin);
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("IsLogin",isLogin);
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
