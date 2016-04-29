package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;

public class CartItemActivity extends AppCompatActivity {

    String activityName;
    FrameLayout fragmentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        fragmentLayout = (FrameLayout)findViewById(R.id.fragmentLayout);

        Intent intent = getIntent();
        if(intent.getStringExtra("ActivityName")!=null){
            activityName = intent.getStringExtra("ActivityName");
        }
        Globals.ReplaceFragment(new CartItemFragment(activityName), getSupportFragmentManager(), null, R.id.fragmentLayout);
    }

    @Override
    public void onBackPressed() {
        LinearLayout errorLayout = (LinearLayout)fragmentLayout.findViewById(R.id.errorLayout);
        if(errorLayout.isShown()){
            CheckOutActivity.objCheckOut = null;
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ShowMessage", false);
        returnIntent.putExtra("IsLogin", true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
