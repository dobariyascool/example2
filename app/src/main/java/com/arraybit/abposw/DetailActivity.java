package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.arraybit.modal.ItemMaster;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView ivItemImage;
    TextView tvItemName, tvItemRate, tvShortDescription;
    ItemMaster objItemMaster;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        objItemMaster = getIntent().getParcelableExtra("ItemMaster");

        //app_bar
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
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
        //end

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemRate = (TextView) findViewById(R.id.tvItemRate);
        tvShortDescription = (TextView) findViewById(R.id.tvShortDescription);

        if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItemImage.getContext()).load(R.drawable.default_image).into(ivItemImage);
        } else {
            Picasso.with(ivItemImage.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItemImage);
        }
        tvItemName.setText(objItemMaster.getItemName());
        tvShortDescription.setText(objItemMaster.getShortDescription());
        tvItemRate.setText(String.valueOf(objItemMaster.getRate()));
    }
}
