package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.RegisteredUserMaster;
import com.rey.material.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RegisteredUserMaster objRegisteredUserMaster;
    LinearLayout homeLinearLayoput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        homeLinearLayoput = (LinearLayout) findViewById(R.id.homeLinearLayoput);
        objRegisteredUserMaster = getIntent().getParcelableExtra("RegisteredUserMaster");

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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.hHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Intent i = new Intent(HomeActivity.this, HotelProfileActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.hOffers) {
            drawerLayout.closeDrawer(navigationView);
            Intent i = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.hNotification) {
            drawerLayout.closeDrawer(navigationView);
            Intent i = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.hExit) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserName", "", this);
            objSharePreferenceManage.CreatePreference("LoginPreference", "UserPassword", "", this);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        //MenuItem menuItem = menu.findItem(R.id.cart_layout);

        //homeLinearLayoput = (LinearLayout) MenuItemCompat.getActionView(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("LoginPreference", "RegisteredUserMasterId", this);
            objSharePreferenceManage.RemovePreference("LoginPreference", "UserName", this);
            objSharePreferenceManage.RemovePreference("LoginPreference", "UserPassword", this);
            objSharePreferenceManage.ClearPreference("LoginPreference", this);
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
