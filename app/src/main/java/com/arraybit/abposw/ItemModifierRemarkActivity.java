package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;

public class ItemModifierRemarkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modifier_remark);

        //hide keyboard focus when activity open
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent = getIntent();
        Globals.ReplaceFragment(new ItemModifierRemarkFragment((ItemMaster) intent.getParcelableExtra("ItemMaster")), getSupportFragmentManager(), getResources().getString(R.string.title_item_modifier_remark), R.id.itemModifierRemarkLayout);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
    }
}
