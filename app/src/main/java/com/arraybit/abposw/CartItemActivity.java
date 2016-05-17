package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.google.gson.Gson;

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
//        LinearLayout errorLayout = (LinearLayout)fragmentLayout.findViewById(R.id.errorLayout);
//        if(errorLayout.isShown()){
//            CheckOutActivity.objCheckOut = null;
//        }
        SaveCartDataInSharePreference();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ShowMessage", false);
        returnIntent.putExtra("IsLogin", true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        if(activityName !=null && activityName.equals(getResources().getString(R.string.title_home))){
            CheckOutActivity.isBackPressed = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    if(data.getBooleanExtra("IsShowMessage", false)){
                        Globals.ShowSnackBar(fragmentLayout, getResources().getString(R.string.siLoginSucessMsg), CartItemActivity.this, 2000);
                    }else{
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("IsActivityFinish", true);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SaveCartDataInSharePreference(){
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        try {
            if (Globals.alOrderItemTran.size() == 0) {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", null, CartItemActivity.this);
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = gson.toJson(Globals.alOrderItemTran);
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", string, CartItemActivity.this);
                objSharePreferenceManage.CreatePreference("CartItemListPreference","OrderRemark",RemarkDialogFragment.strRemark,CartItemActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
