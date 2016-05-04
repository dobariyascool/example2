package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.adapter.ItemSuggestedAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class DetailActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener, ItemSuggestedAdapter.ImageViewClickListener, View.OnClickListener {

    ImageView ivItemImage;
    TextView txtItemRate, txtShortDescription, txtHeader, txtItemDineOnly;
    RecyclerView rvSuggestedItem;
    Toolbar app_bar;
    Button btnCancel, btnAdd,btnDisable;
    ItemMaster objItemMaster;
    ArrayList<ItemMaster> alItemMaster;
    ItemSuggestedAdapter itemSuggestedAdapter;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayout itemSuggestionLayout, dividerLayout;
    FrameLayout detailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        objItemMaster = getIntent().getParcelableExtra("ItemMaster");

        detailLayout = (FrameLayout) findViewById(R.id.detailLayout);
        itemSuggestionLayout = (LinearLayout) findViewById(R.id.itemSuggestionLayout);
        dividerLayout = (LinearLayout) findViewById(R.id.dividerLayout);

        rvSuggestedItem = (RecyclerView) findViewById(R.id.rvSuggestedItem);

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);

        txtItemRate = (TextView) findViewById(R.id.txtItemRate);
        txtShortDescription = (TextView) findViewById(R.id.txtShortDescription);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtItemDineOnly = (TextView) findViewById(R.id.txtItemDineOnly);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDisable = (Button) findViewById(R.id.btnDisable);

        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        SetVisibility(false);

        if (objItemMaster != null) {
            GetItemDetail(objItemMaster);
            if (Service.CheckNet(this)) {
                RequestItem();
            } else {
                Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            onBackPressed();
        } else if (v.getId() == R.id.btnAdd) {
            if (objItemMaster.getLinktoItemMasterIdModifiers().equals("") && objItemMaster.getLinktoOptionMasterIds().equals("")) {
                AddQtyRemarkDialogFragment objAddQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(objItemMaster);
                objAddQtyRemarkDialogFragment.show(this.getSupportFragmentManager(), "");
            } else {
                Globals.ReplaceFragment(new ItemModifierRemarkFragment(objItemMaster), getSupportFragmentManager(), getResources().getString(R.string.title_item_modifier_remark), R.id.detailLayout);
            }
        }
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster, boolean isFilter) {
        progressDialog.dismiss();
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }


    @Override
    public void ImageOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        this.objItemMaster = objItemMaster;
        GetItemDetail(objItemMaster);
        RequestItem();
    }

    //region Private Method
    private void RequestItem() {
        progressDialog.show(getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemSuggested(this, String.valueOf(objItemMaster.getItemMasterId()), String.valueOf(Globals.linktoBusinessMasterId));
    }

    @SuppressLint("SetTextI18n")
    private void GetItemDetail(ItemMaster objItemMaster) {
        if (app_bar != null) {
            if (objItemMaster.getItemName() != null) {
                getSupportActionBar().setTitle(objItemMaster.getItemName());
            } else {
                getSupportActionBar().setTitle(this.getResources().getString(R.string.title_detail));
            }
        }

        if (objItemMaster.getIsDineInOnly()) {
            btnDisable.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else {
            btnDisable.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }

        if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItemImage.getContext()).load(R.drawable.default_image).into(ivItemImage);
        } else {
            Picasso.with(ivItemImage.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItemImage);
        }
        if (objItemMaster.getShortDescription().equals("")) {
            txtShortDescription.setVisibility(View.GONE);
        } else {
            txtShortDescription.setVisibility(View.VISIBLE);
            txtShortDescription.setText(objItemMaster.getShortDescription());
        }
        txtShortDescription.setText(objItemMaster.getShortDescription());
        txtItemRate.setText(getResources().getString(R.string.cifRupee) + " " + objItemMaster.getRate());
    }

    private void SetRecyclerView() {
        if (alItemMaster == null || alItemMaster.size() == 0) {
            SetVisibility(false);
        } else {
            SetVisibility(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setAutoMeasureEnabled(true);
            rvSuggestedItem.setLayoutManager(layoutManager);
            itemSuggestedAdapter = new ItemSuggestedAdapter(this, alItemMaster, this);
            rvSuggestedItem.setAdapter(itemSuggestedAdapter);
        }
    }

    private void SetVisibility(boolean isShow) {
        if (isShow) {
            txtHeader.setVisibility(View.VISIBLE);
            dividerLayout.setVisibility(View.VISIBLE);
            itemSuggestionLayout.setVisibility(View.VISIBLE);
            rvSuggestedItem.setVisibility(View.VISIBLE);
        } else {
            txtHeader.setVisibility(View.GONE);
            dividerLayout.setVisibility(View.GONE);
            itemSuggestionLayout.setVisibility(View.INVISIBLE);
            rvSuggestedItem.setVisibility(View.GONE);
        }
    }
    //endregion
}