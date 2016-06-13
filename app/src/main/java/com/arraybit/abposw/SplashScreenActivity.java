package com.arraybit.abposw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity implements CustomerJSONParser.CustomerRequestListener {

    SharePreferenceManage objSharePreferenceManage;
    DisplayMetrics displayMetrics;
    ImageView ivLeft, ivRight, ivLogo, ivText;
    DrawerLayout mainLayout;
    List<ItemMaster> lstItemMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);

        displayMetrics = getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_background);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        mainLayout.setBackground(new BitmapDrawable(getResources(), resizeBitmap));

        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivText = (ImageView) findViewById(R.id.ivText);

        Picasso.with(SplashScreenActivity.this).load(R.drawable.left_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivLeft);
        Picasso.with(SplashScreenActivity.this).load(R.drawable.right_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivRight);
        Picasso.with(SplashScreenActivity.this).load(R.drawable.likeat_logo).resize((displayMetrics.widthPixels * 25) / 100, (displayMetrics.heightPixels * 8) / 100).into(ivLogo);
        Picasso.with(SplashScreenActivity.this).load(R.drawable.welcome_text).resize((displayMetrics.widthPixels * 80) / 100, (displayMetrics.heightPixels * 7) / 100).into(ivText);

        SetBusinessMasterID();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this) != null) {
                    String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this);
                    String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this);
                    String businessMasterId = objSharePreferenceManage.GetPreference("LoginPreference","BusinessMasterId", SplashScreenActivity.this);
                    if ((!userName.isEmpty() && !userPassword.isEmpty()) && businessMasterId.equals(String.valueOf(Globals.linktoBusinessMasterId))) {
                        if (Service.CheckNet(SplashScreenActivity.this)) {
                            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                            objCustomerJSONParser.SelectCustomerMaster(SplashScreenActivity.this, userName, userPassword, null, null, String.valueOf(Globals.linktoBusinessMasterId));
                        } else {
                            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                            intent.putExtra("IsNetCheck", true);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        startActivity(intent);
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
        }, 1000);
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
        if (objCustomerMaster == null) {
            intent.putExtra("IsLogin", false);
        } else {
            intent.putExtra("IsLogin", true);
            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerMasterId", String.valueOf(objCustomerMaster.getCustomerMasterId()), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", objCustomerMaster.getEmail1(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", objCustomerMaster.getPassword(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", objCustomerMaster.getCustomerName(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objCustomerMaster.getXs_ImagePhysicalName(), this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), this);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void SetBusinessMasterID() {
        objSharePreferenceManage = new SharePreferenceManage();
        Gson gson = new Gson();
        String string = objSharePreferenceManage.GetPreference("CartItemListPreference", "CartItemList", SplashScreenActivity.this);
        if (string != null) {
            ItemMaster[] objItemMaster = gson.fromJson(string,
                    ItemMaster[].class);

            lstItemMaster = Arrays.asList(objItemMaster);
            Globals.alOrderItemTran.addAll(new ArrayList<>(lstItemMaster));
            Globals.counter = Globals.alOrderItemTran.size();
            if (objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", SplashScreenActivity.this) != null) {
                RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", SplashScreenActivity.this);
            }
        }
        if (objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", SplashScreenActivity.this) != null && Globals.counter != 0) {
            Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("BusinessPreference", "BusinessMasterId", SplashScreenActivity.this));
        } else {
            Globals.linktoBusinessMasterId = 1;
        }

    }
}
