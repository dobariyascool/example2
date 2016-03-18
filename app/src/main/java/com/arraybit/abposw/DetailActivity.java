package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arraybit.adapter.ItemSuggestedAdapter;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class DetailActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener, ItemSuggestedAdapter.ImageViewClickListener {

    ImageView ivItemImage;
    TextView tvItemRate, tvShortDescription;
    RecyclerView rvSuggestedItem;
    Toolbar app_bar;
    ItemMaster objItemMaster;
    ArrayList<ItemMaster> alItemMaster;
    ItemSuggestedAdapter itemSuggestedAdapter;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        objItemMaster = getIntent().getParcelableExtra("ItemMaster");
        rvSuggestedItem = (RecyclerView) findViewById(R.id.rvSuggestedItem);
        rvSuggestedItem.setVisibility(View.INVISIBLE);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemRate = (TextView) findViewById(R.id.tvItemRate);
        tvShortDescription = (TextView) findViewById(R.id.tvShortDescription);

        GetItemDetail(objItemMaster);

        RequestItem();
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        progressDialog.dismiss();
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }

    public void SetRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        rvSuggestedItem.setVisibility(View.VISIBLE);
        rvSuggestedItem.setLayoutManager(layoutManager);
        itemSuggestedAdapter = new ItemSuggestedAdapter(this, alItemMaster, this);
        rvSuggestedItem.setAdapter(itemSuggestedAdapter);
    }

    @Override
    public void ImageOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        this.objItemMaster = objItemMaster;
        GetItemDetail(objItemMaster);
        RequestItem();
    }

    //region Private Method
    private void RequestItem() {
        progressDialog.show(DetailActivity.this.getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemSuggested(this, String.valueOf(objItemMaster.getItemMasterId()));
    }

    private void GetItemDetail(ItemMaster objItemMaster) {
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (objItemMaster.getItemName() != null) {
                getSupportActionBar().setTitle(objItemMaster.getItemName());
            } else {
                getSupportActionBar().setTitle(this.getResources().getString(R.string.title_detail));
            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(this.getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItemImage.getContext()).load(R.drawable.default_image).into(ivItemImage);
        } else {
            Picasso.with(ivItemImage.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItemImage);
        }
        tvShortDescription.setText(objItemMaster.getShortDescription());
        tvItemRate.setText(String.valueOf(objItemMaster.getRate()));
    }
    //endregion
}