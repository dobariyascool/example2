package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;

import java.util.ArrayList;

public class ItemModifierRemarkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modifier_remark);

        Intent intent = getIntent();

        Globals.ReplaceFragment(new ItemModifierRemarkFragment((ItemMaster) intent.getParcelableExtra("ItemMaster")), getSupportFragmentManager(), null, R.id.itemModifierRemarkLayout);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
        ModifierSelectionFragmentDialog.alFinalCheckedModifier=new ArrayList<>();
    }
}
