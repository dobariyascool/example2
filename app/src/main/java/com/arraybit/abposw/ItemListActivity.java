package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;

public class ItemListActivity extends AppCompatActivity implements CategoryJSONParser.CategoryListener {

    Toolbar app_bar;
    LinearLayout itemListActivity, llItemListFragment;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        itemListActivity = (LinearLayout) findViewById(R.id.itemListActivity);
        Globals.SetScaleImageBackground(ItemListActivity.this, itemListActivity, null, null);
        llItemListFragment = (LinearLayout) findViewById(R.id.llItemListFragment);
        int categoryMasterId = getIntent().getIntExtra("CateoryMasterId", 0);
        isStart= getIntent().getBooleanExtra("isStart", false);
        RequestOfferMaster(categoryMasterId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (isStart) {
                Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                finish();
//                overridePendingTransition(0, R.anim.right_exit);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void CategoryResponse(CategoryMaster objCategoryMaster) {
        progressDialog.dismiss();
        if (objCategoryMaster != null) {
            app_bar.setTitle(objCategoryMaster.getCategoryName());
            Fragment fragment = new ItemListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ItemListFragment.ITEMS_COUNT_KEY, objCategoryMaster);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.llItemListFragment, fragment, "ItemListFragment");
            fragmentTransaction.addToBackStack("ItemListFragment");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            finish();
//                overridePendingTransition(0, R.anim.right_exit);
        }
    }

    private void RequestOfferMaster(int categoryMasterId) {
        progressDialog.show(getSupportFragmentManager(), "");
        CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();
        if (categoryMasterId != 0) {
            objCategoryJSONParser.SelectCategoryMaster(ItemListActivity.this, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(categoryMasterId));
        }
    }


}
