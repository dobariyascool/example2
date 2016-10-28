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
import android.widget.Toast;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.modal.FCMMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.arraybit.parser.FCMJSONParser;
import com.arraybit.parser.NotificationJSONParser;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
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
    public static String token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);

        if(Service.CheckNet(SplashScreenActivity.this)) {
            FCMTokenGenerate();
        }

        mainLayout = (DrawerLayout) findViewById(R.id.mainLayout);

        displayMetrics = getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_background_full);
//        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        mainLayout.setBackground(new BitmapDrawable(getResources(), originalBitmap));

        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivText = (ImageView) findViewById(R.id.ivText);

//        Picasso.with(SplashScreenActivity.this).load(R.drawable.left_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivLeft);
//        Picasso.with(SplashScreenActivity.this).load(R.drawable.right_design).resize((displayMetrics.widthPixels * 50) / 100, (displayMetrics.heightPixels * 20) / 100).into(ivRight);
//        Picasso.with(SplashScreenActivity.this).load(R.drawable.likeat_logo).resize((displayMetrics.widthPixels * 25) / 100, (displayMetrics.heightPixels * 8) / 100).into(ivLogo);
//        Picasso.with(SplashScreenActivity.this).load(R.drawable.welcome_text).resize((displayMetrics.widthPixels * 80) / 100, (displayMetrics.heightPixels * 7) / 100).into(ivText);

        SetBusinessMasterID();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AddNotificationRequest();
                objSharePreferenceManage = new SharePreferenceManage();
                String str = objSharePreferenceManage.GetPreference("LoginPreference", "IntegrationId", SplashScreenActivity.this);
                if (objSharePreferenceManage.GetPreference("LoginPreference", "IntegrationId", SplashScreenActivity.this) != null) {
                    String customerId = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", SplashScreenActivity.this);
                    if (customerId != null && !customerId.equals("")) {
                        if (Service.CheckNet(SplashScreenActivity.this)) {
                            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                            objCustomerJSONParser.SelectCustomerMaster(SplashScreenActivity.this, null, null, customerId, null, String.valueOf(Globals.linktoBusinessMasterId),null);
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
                    if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this) != null) {
                        String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this);
                        String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this);
                        String businessMasterId = objSharePreferenceManage.GetPreference("LoginPreference", "BusinessMasterId", SplashScreenActivity.this);
                        if ((!userName.isEmpty() && !userPassword.isEmpty()) && (businessMasterId != null && businessMasterId.equals(String.valueOf(Globals.linktoBusinessMasterId)))) {
                            if (Service.CheckNet(SplashScreenActivity.this)) {
                                CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                                objCustomerJSONParser.SelectCustomerMaster(SplashScreenActivity.this, userName, userPassword, null, null, String.valueOf(Globals.linktoBusinessMasterId),null);
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
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {

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
            if (objCustomerMaster.getGooglePlusUserId() != null && !objCustomerMaster.getGooglePlusUserId().equals("")) {
                objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objCustomerMaster.getGooglePlusUserId(), this);
            } else if (objCustomerMaster.getFacebookUserId() != null && !objCustomerMaster.getFacebookUserId().equals("")) {
                objSharePreferenceManage.CreatePreference("LoginPreference", "IntegrationId", objCustomerMaster.getFacebookUserId(), this);
            }
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

    private void FCMTokenGenerate() {

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            // [END subscribe_topics]

            token = FirebaseInstanceId.getInstance().getToken();

        }

    }

    private void AddNotificationRequest() {
        try {
            FCMJSONParser objFcmjsonParser= new FCMJSONParser();
            FCMMaster objFcmMaster= new FCMMaster();
            objFcmMaster.setFCMToken(token);
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", SplashScreenActivity.this) != null) {
                objFcmMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", SplashScreenActivity.this)));
            }
            objFcmjsonParser.InsertFCMMaster(objFcmMaster,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
