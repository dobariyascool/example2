package com.arraybit.abposw;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arraybit.global.Globals;

public class MyOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Globals.ReplaceFragment(new YourOrderFragment(), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_your_order), R.id.orderFragmentLayout);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
