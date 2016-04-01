package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;

public class SplashScreenActivity extends AppCompatActivity implements CustomerJSONParser.CustomerRequestListener {

    SharePreferenceManage objSharePreferenceManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", this) != null) {
            String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", this);
            String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", this);
            if (!userName.isEmpty() && !userPassword.isEmpty()) {
                CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                objCustomerJSONParser.SelectCustomerMaster(SplashScreenActivity.this, userName, userPassword);
            } else {
                Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        Intent i = new Intent(SplashScreenActivity.this, RegistrationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
