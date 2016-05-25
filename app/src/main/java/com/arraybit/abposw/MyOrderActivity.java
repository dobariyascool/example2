package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;

public class MyOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        FrameLayout orderFragmentLayout = (FrameLayout)findViewById(R.id.orderFragmentLayout);

        if(getIntent().getBooleanExtra("IsShowMessage", false)){
            Globals.ShowSnackBar(orderFragmentLayout, getResources().getString(R.string.siLoginSuccessMsg), MyOrderActivity.this, 2000);
        }

        Globals.ReplaceFragment(new YourOrderFragment(), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_your_order), R.id.orderFragmentLayout);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("IsLogin", true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
