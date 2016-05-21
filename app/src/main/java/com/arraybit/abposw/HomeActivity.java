package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.BusinessGalleryJSONParser;
import com.google.gson.Gson;
import com.liangfeizc.slidepageindicator.CirclePageIndicator;
import com.rey.material.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResourceType")
@SuppressLint("InflateParams")
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BusinessGalleryJSONParser.BusinessGalleryRequestListener, View.OnClickListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog = new ProgressDialog();
    ViewPager viewPager;
    CirclePageIndicator circlePageIndicator;
    CompoundButton cbName;
    TextView txtFullName;
    boolean isLogin;
    RelativeLayout relativeLayout;
    com.rey.material.widget.TextView txtCartNumber;
    boolean doubleBackToExitPressedOnce;


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
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetHomePageBackground(HomeActivity.this,drawerLayout,null,null,null);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);

        View headerView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.navigation_header, null);
        ImageView ivLogo = (ImageView) headerView.findViewById(R.id.ivLogo);
        cbName = (CompoundButton) headerView.findViewById(R.id.cbName);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.addHeaderView(headerView);

        Globals.SetNavigationDrawer(actionBarDrawerToggle, HomeActivity.this, drawerLayout, app_bar);

        CardView cvDelivery = (CardView) findViewById(R.id.cvDelivery);
        CardView cvTakeAway = (CardView) findViewById(R.id.cvTakeAway);
        CardView cvBookTable = (CardView) findViewById(R.id.cvBookTable);
        CardView cvOffer = (CardView) findViewById(R.id.cvOffer);

        cvDelivery.setOnClickListener(this);
        cvTakeAway.setOnClickListener(this);
        cvBookTable.setOnClickListener(this);
        cvOffer.setOnClickListener(this);
        cbName.setOnClickListener(this);

        if (Service.CheckNet(this)) {
            RequestBusinessGallery();
        } else {
            Globals.ShowSnackBar(drawerLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }

        SaveCartDataInSharePreference();

        SetUserName();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.hHotelProfile) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, HotelProfileActivity.class);
            startActivityForResult(intent, 0);
        } else if (item.getItemId() == R.id.hOffers) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hNotification) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hFeedback) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hShare) {
            drawerLayout.closeDrawer(navigationView);
            Uri imageUri = Uri.parse("android.resource://com.arraybit.abposw/drawable/" + R.mipmap.app_logo);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, imageUri);
            i.putExtra(Intent.EXTRA_TEXT, "This is the very good app");
            Intent chooser = Intent.createChooser(i, "Tell a Friend");
            startActivity(chooser);
        } else if (item.getItemId() == R.id.hRate) {
            drawerLayout.closeDrawer(navigationView);
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

            }
        }
        else if (item.getItemId() == R.id.hAboutUs) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.hContactUs) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(intent);
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

        MenuItem menuItem = menu.findItem(R.id.cart_layout);

        relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        final ImageView ivCart = (ImageView) relativeLayout.findViewById(R.id.ivCart);
        final RelativeLayout cartLayout = (RelativeLayout) relativeLayout.findViewById(R.id.cartLayout);
        txtCartNumber = (com.rey.material.widget.TextView) relativeLayout.findViewById(R.id.txtCartNumber);

        SetCartNumber();

        cartLayout.setOnClickListener(this);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this) != null && isLogin) {
            menu.findItem(R.id.myAccount).setVisible(true);
            menu.findItem(R.id.logout).setVisible(true);
        } else {
            menu.findItem(R.id.myAccount).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            Globals.ClearUserPreference(HomeActivity.this, HomeActivity.this);
            SaveCartDataInSharePreference();
        }
        menu.findItem(R.id.cart_layout).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            Globals.ClearUserPreference(HomeActivity.this, HomeActivity.this);
            SaveCartDataInSharePreference();
            SetUserName();
        } else if (id == R.id.myAccount) {
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.myBookings) {

            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            intent.putExtra("IsBookingFromMenu", true);
            startActivityForResult(intent, 0);
        } else if (id == R.id.wishList) {
            Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.myOrders) {
            Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
            startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Globals.ShowSnackBar(drawerLayout,getResources().getString(R.string.HoBackMsg),HomeActivity.this,1000);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void BusinessGalleryResponse(ArrayList<BusinessGalleryTran> alBusinessGalleryTran) {
        progressDialog.dismiss();
        SetSlider(alBusinessGalleryTran);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvDelivery) {
            Globals.linktoOrderTypeMasterId = (short) Globals.OrderType.HomeDelivery.getValue();
            SharePreferenceManage objSharePreferenceManager = new SharePreferenceManage();
            objSharePreferenceManager.CreatePreference("OrderTypePreference","OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()),HomeActivity.this);
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.cvTakeAway) {
            Globals.linktoOrderTypeMasterId = (short) Globals.OrderType.TakeAway.getValue();
            SharePreferenceManage objSharePreferenceManager = new SharePreferenceManage();
            objSharePreferenceManager.CreatePreference("OrderTypePreference","OrderType", String.valueOf(Globals.OrderType.TakeAway.getValue()),HomeActivity.this);
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.cvBookTable) {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.cvOffer) {
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.cbName) {
            drawerLayout.closeDrawer(navigationView);
            if (cbName.getText().equals(getResources().getString(R.string.siSignIn))) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        } else if (v.getId() == R.id.cartLayout) {
            Intent intent = new Intent(HomeActivity.this, CartItemActivity.class);
            intent.putExtra("ActivityName", getResources().getString(R.string.title_home));
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    isLogin = data.getBooleanExtra("IsLogin", false);
                    if (data.getBooleanExtra("IsShowMessage", false)) {
                        Globals.ShowSnackBar(drawerLayout, getResources().getString(R.string.siLoginSucessMsg), HomeActivity.this, 2000);
                    } else if (data.getBooleanExtra("ShowBookingMessage", false)) {
                        ShowSnackBarWithAction();
                    }
                }
                SetUserName();
                SaveCartDataInSharePreference();
                SetCartNumber();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //region Private Methods
    private void RequestBusinessGallery() {
        progressDialog.show(getSupportFragmentManager(), "");

        BusinessGalleryJSONParser objBusinessGalleryJSONParser = new BusinessGalleryJSONParser();
        objBusinessGalleryJSONParser.SelectAllBusinessGalleryTran(HomeActivity.this, String.valueOf(1), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetSlider(ArrayList<BusinessGalleryTran> alBusinessGalleryTran) {
        if (alBusinessGalleryTran != null && alBusinessGalleryTran.size() != 0) {
            SlidePagerAdapter pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
            pagerAdapter.addAll(alBusinessGalleryTran);
            viewPager.setAdapter(pagerAdapter);
            circlePageIndicator.setViewPager(viewPager);
        }
    }

    private void SetUserName() {
        Intent intent = getIntent();
        if (!isLogin) {
            isLogin = intent.getBooleanExtra("IsLogin", false);
        }
        if (isLogin) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this) != null) {
                cbName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this));
            } else {
                cbName.setText(getResources().getString(R.string.siSignIn));
            }
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", HomeActivity.this) != null) {
                txtFullName.setVisibility(View.VISIBLE);
                txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", HomeActivity.this));
            } else {
                txtFullName.setVisibility(View.GONE);
            }
        }
    }

    private void ShowSnackBarWithAction() {
        Snackbar snackbar = Snackbar
                .make(drawerLayout, getResources().getString(R.string.ybAddBookingSuccessMsg), Snackbar.LENGTH_LONG)
                .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Globals.ReplaceFragment(new YourBookingFragment(), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_your_booking), R.id.drawerLayout);
                    }
                })
                .setDuration(5000);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackBarActionColor));
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        TextView txt = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey));
        snackbar.show();
    }

    private void SetCartNumber() {
        if (Globals.counter > 0) {
            txtCartNumber.setText(String.valueOf(Globals.counter));
            txtCartNumber.setSoundEffectsEnabled(true);
            txtCartNumber.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.cart_number));
//            txtCartNumber.setAnimation(AnimationUtils.loadAnimation(MenuActivity.this, R.anim.fab_scale_up));
//            if (isShowMsg && itemName!=null) {
//                Globals.ShowSnackBar(menuActivity, String.format(getResources().getString(R.string.MsgCartItem), itemName), HomeActivity.this, 3000);
//            }
        } else {
            txtCartNumber.setBackgroundColor(ContextCompat.getColor(HomeActivity.this, android.R.color.transparent));
        }
    }

    private void SaveCartDataInSharePreference() {
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        List<ItemMaster> lstItemMaster;
        try {
            if (Globals.alOrderItemTran.size() == 0) {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = objSharePreferenceManage.GetPreference("CartItemListPreference", "CartItemList", HomeActivity.this);
                if (string != null) {
                    ItemMaster[] objItemMaster = gson.fromJson(string,
                            ItemMaster[].class);

                    lstItemMaster = Arrays.asList(objItemMaster);
                    Globals.alOrderItemTran.addAll(new ArrayList<ItemMaster>(lstItemMaster));
                    Globals.counter = Globals.alOrderItemTran.size();
                    if(objSharePreferenceManage.GetPreference("CartItemListPreference","OrderRemark",HomeActivity.this)!=null){
                        RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference","OrderRemark",HomeActivity.this);
                    }
                } else {
                    objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", HomeActivity.this);
                    objSharePreferenceManage.ClearPreference("CheckOutDataPreference", HomeActivity.this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            objSharePreferenceManage = null;
            lstItemMaster = null;
        }
    }


    //endregion

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
