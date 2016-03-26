package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.BusinessGalleryJSONParser;
import com.liangfeizc.slidepageindicator.CirclePageIndicator;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BusinessGalleryJSONParser.BusinessGalleryRequestListener,View.OnClickListener{

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RegisteredUserMaster objRegisteredUserMaster;
    ProgressDialog progressDialog = new ProgressDialog();
    ViewPager viewPager;
    CirclePageIndicator circlePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.app_logo);
            getSupportActionBar().setTitle("Home");
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        objRegisteredUserMaster = getIntent().getParcelableExtra("RegisteredUserMaster");

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);

        View headerView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.navigation_header, null);
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        //ivLogo.setVisibility(View.GONE);
        TextView txtLetter = (TextView) headerView.findViewById(R.id.txtLetter);
        txtLetter.setText(objRegisteredUserMaster.getFirstName().substring(0, 1));
        TextView txtName = (TextView) headerView.findViewById(R.id.txtName);
        txtName.setText(objRegisteredUserMaster.getEmail());

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.addHeaderView(headerView);

        Globals.SetNavigationDrawer(actionBarDrawerToggle, HomeActivity.this, drawerLayout, app_bar);

        CardView cvDelivery = (CardView)findViewById(R.id.cvDelivery);
        CardView cvTakeAway = (CardView)findViewById(R.id.cvTakeAway);
        CardView cvBookTable = (CardView)findViewById(R.id.cvBookTable);
        CardView cvOffer = (CardView)findViewById(R.id.cvOffer);

        cvDelivery.setOnClickListener(this);
        cvTakeAway.setOnClickListener(this);
        cvBookTable.setOnClickListener(this);
        cvOffer.setOnClickListener(this);

        if (Service.CheckNet(this)) {
            RequestBusinessGallery();
        } else {
            Globals.ShowSnackBar(drawerLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.hHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, HotelProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hOffers) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hNotification) {
            drawerLayout.closeDrawer(navigationView);
        } else if (item.getItemId() == R.id.hExit) {
            System.exit(0);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //MenuItem menuItem = menu.findItem(R.id.cart_layout);

        //homeLinearLayoput = (LinearLayout) MenuItemCompat.getActionView(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            Globals.Logout(HomeActivity.this,HomeActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void BusinessGalleryResponse(ArrayList<BusinessGalleryTran> alBusinessGalleryTran) {
        progressDialog.dismiss();
        SetSlider(alBusinessGalleryTran);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cvDelivery){
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.cvTakeAway){
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.cvBookTable){

        }else if(v.getId()==R.id.cvOffer){
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(intent);
        }
    }

    //region Private Methods
    private void RequestBusinessGallery() {
        progressDialog.show(getSupportFragmentManager(), "");

        BusinessGalleryJSONParser objBusinessGalleryJSONParser = new BusinessGalleryJSONParser();
        objBusinessGalleryJSONParser.SelectAllBusinessGalleryTran(HomeActivity.this, String.valueOf(1), String.valueOf(Globals.linktoBusinessMasterId));
    }
    //endregion

    private void SetSlider(ArrayList<BusinessGalleryTran> alBusinessGalleryTran){

        SlidePagerAdapter pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addAll(alBusinessGalleryTran);
        viewPager.setAdapter(pagerAdapter);
        circlePageIndicator.setViewPager(viewPager);
    }

    //region Page Adapter
    public class SlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<BusinessGalleryTran> lstAdvertise;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return SliderFragment.createInstance(lstAdvertise.get(i));
        }

        @Override
        public int getCount() {
            return lstAdvertise.size();
        }

        public void addAll(List<BusinessGalleryTran> lstAdvertise) {
            this.lstAdvertise = lstAdvertise;
        }
    }
    //endregion
}
