package com.arraybit.abposw;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.parser.BusinessJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class HotelProfileActivity extends AppCompatActivity implements BusinessJSONParser.BusinessRequestListener {

    ViewPager viewPager;
    ImageView ivLogo, ivBackground;
    TabLayout tabLayout;
    BusinessMaster objBusinessMaster;
    PageAdapter pageAdapter;
    CoordinatorLayout hotelProfileFragment;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_hotel_profile);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        hotelProfileFragment = (CoordinatorLayout) findViewById(R.id.hotelProfileFragment);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);

        pageAdapter = new PageAdapter(getSupportFragmentManager());

        Picasso.with(HotelProfileActivity.this).load(R.drawable.hotel_profile_background).fit().centerCrop().into(ivBackground);

        if (Service.CheckNet(this)) {
            RequestBusinessMaster();
        } else {
            Globals.ShowSnackBar(hotelProfileFragment, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        this.objBusinessMaster = objBusinessMaster;
        SetTabLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ClearData();
            setResult(Activity.RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            ClearData();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            ClearData();
            super.onBackPressed();
        }
    }

    //region Private Methods
    private void RequestBusinessMaster() {
        progressDialog.show(HotelProfileActivity.this.getSupportFragmentManager(), "");

        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectBusinessMaster(HotelProfileActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetTabLayout() {
        if (objBusinessMaster == null) {
            Globals.ShowSnackBar(hotelProfileFragment, getResources().getString(R.string.MsgSelectFail), HotelProfileActivity.this, 1000);
        } else {

            Glide.with(HotelProfileActivity.this).load(objBusinessMaster.getXSImagePhysicalName()).asBitmap().override(150, 150).centerCrop().into(new BitmapImageViewTarget(ivLogo) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivLogo.setImageDrawable(circularBitmapDrawable);
                }
            });
            //Picasso.with(HotelProfileActivity.this).load(objBusinessMaster.getXSImagePhysicalName()).resize(120,120).into(ivLogo);

            pageAdapter.AddFragment(new InformationFragment(objBusinessMaster), "Information");
            pageAdapter.AddFragment(new GalleryFragment(), "Gallery");
            pageAdapter.AddFragment(new ReviewFragment(objBusinessMaster), "Review");
            pageAdapter.AddFragment(new BusinessInformationFragment(), "BusinessInformation");

            viewPager.setAdapter(pageAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void ClearData() {
        InformationFragment.lstBusinessHoursTran = null;
        InformationFragment.lstBusinessService = null;
    }
    //endregion

    //region Page Adapter
    static class PageAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
    //endregion
}
