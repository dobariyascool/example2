package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.arraybit.adapter.ItemSuggestedAdapter;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class DetailActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener {

    ImageView ivItemImage;
    TextView tvItemName, tvItemRate, tvShortDescription;
    RecyclerView rvSuggestedItem;
    ItemMaster objItemMaster;
    ArrayList<ItemMaster> alItemMaster;
    ItemSuggestedAdapter itemSuggestedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        objItemMaster = getIntent().getParcelableExtra("ItemMaster");

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);

        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (objItemMaster.getItemName() != null) {
                app_bar.setTitle(objItemMaster.getItemName());
            } else {
                app_bar.setTitle(this.getResources().getString(R.string.title_detail));
            }
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(this.getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        //end

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemRate = (TextView) findViewById(R.id.tvItemRate);
        tvShortDescription = (TextView) findViewById(R.id.tvShortDescription);
        rvSuggestedItem = (RecyclerView) findViewById(R.id.rvSuggestedItem);

        if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItemImage.getContext()).load(R.drawable.default_image).into(ivItemImage);
        } else {
            Picasso.with(ivItemImage.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItemImage);
        }
        tvItemName.setText(objItemMaster.getItemName());
        tvShortDescription.setText(objItemMaster.getShortDescription());
        tvItemRate.setText(String.valueOf(objItemMaster.getRate()));

        RequestItem();
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }

    public void SetRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        rvSuggestedItem.setLayoutManager(layoutManager);
        itemSuggestedAdapter = new ItemSuggestedAdapter(this, alItemMaster);
        rvSuggestedItem.setAdapter(itemSuggestedAdapter);
    }

    //region Private Method
    private void RequestItem() {
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemSuggested(this, String.valueOf(objItemMaster.getItemMasterId()), String.valueOf(objItemMaster.getlinktoBusinessMasterId()));
    }
    //endregion
}