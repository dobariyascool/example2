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
import com.arraybit.parser.CustomerJSONParser;
import com.squareup.picasso.Picasso;

public class SplashScreenActivity extends AppCompatActivity implements CustomerJSONParser.CustomerRequestListener {

    SharePreferenceManage objSharePreferenceManage;
    DisplayMetrics displayMetrics;
    ImageView ivLeft, ivRight, ivLogo, ivText;
    DrawerLayout mainLayout;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this) != null) {
                    String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", SplashScreenActivity.this);
                    String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", SplashScreenActivity.this);
                    if (!userName.isEmpty() && !userPassword.isEmpty()) {
                        if (Service.CheckNet(SplashScreenActivity.this)) {
                            CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                            objCustomerJSONParser.SelectCustomerMaster(SplashScreenActivity.this, userName, userPassword, null, null, String.valueOf(Globals.linktoBusinessMasterId));
                        }else {
                            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                            intent.putExtra("IsNetCheck",true);
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
    public void CustomerResponse(String errorCode,CustomerMaster objCustomerMaster) {
        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
        if (objCustomerMaster == null) {
            intent.putExtra("IsLogin", false);
        } else {
            intent.putExtra("IsLogin", true);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
