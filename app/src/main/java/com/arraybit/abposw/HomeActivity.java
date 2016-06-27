package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BannerMaster;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.BannerMasterJSONParser;
import com.arraybit.parser.BusinessJSONParser;
import com.arraybit.parser.CustomerJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

@SuppressWarnings("ResourceType")
@SuppressLint("InflateParams")
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BannerMasterJSONParser.BannerRequestListener, View.OnClickListener, CustomerJSONParser.CustomerRequestListener, BusinessJSONParser.BusinessRequestListener {

    static final int duration = 5000;
    static BusinessMaster objBusinessMaster;
    final int requestCode = 123;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog = new ProgressDialog();
    ViewPager viewPager;
    CircleIndicator circlePageIndicator;
    CompoundButton cbName;
    TextView txtFullName;
    boolean isLogin;
    RelativeLayout relativeLayout;
    com.rey.material.widget.TextView txtCartNumber;
    boolean doubleBackToExitPressedOnce, isNetCheck;
    LinearLayout homeLayout, internetLayout, nameLayout;
    boolean stopSliding = false, isPause, isProgressShow;
    SlidePagerAdapter pagerAdapter;
    FloatingActionMenu famRoot;
    String shareData;
    ImageView imageView;
    SharePreferenceManage objSharePreferenceManager = new SharePreferenceManage();
    boolean isRefresh;
    private Runnable animateViewPager;
    private Handler handler;

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

        isNetCheck = getIntent().getBooleanExtra("IsNetCheck", false);

        famRoot = (FloatingActionMenu) findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Globals.SetHomePageBackground(HomeActivity.this, drawerLayout, null, null, null);

        internetLayout = (LinearLayout) findViewById(R.id.internetLayout);
        Button btnRetry = (Button) internetLayout.findViewById(R.id.btnRetry);
        homeLayout = (LinearLayout) findViewById(R.id.homeLayout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CircleIndicator) findViewById(R.id.circlePageIndicator);

        View headerView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.navigation_header, null);
        cbName = (CompoundButton) headerView.findViewById(R.id.cbName);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        nameLayout = (LinearLayout) headerView.findViewById(R.id.nameLayout);
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
        nameLayout.setOnClickListener(this);
        cbName.setOnClickListener(this);

        if (internetLayout.getVisibility() == View.GONE) {

            SaveCartDataInSharePreference();

            SetUserName();

            SetWishListFromSharePreference();

            SetBusinessMasterID();
        }

        if (Service.CheckNet(this)) {
            internetLayout.setVisibility(View.GONE);
            famRoot.setVisibility(View.VISIBLE);
            homeLayout.setVisibility(View.VISIBLE);
            isProgressShow = true;
            RequestBannerMaster();
            RequestBusinessMaster();
        } else {
            internetLayout.setVisibility(View.VISIBLE);
            famRoot.setVisibility(View.GONE);
            Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
            homeLayout.setVisibility(View.GONE);
        }

        FloatingActionButton fabYou = (FloatingActionButton) findViewById(R.id.fabYou);
        FloatingActionButton fabInst = (FloatingActionButton) findViewById(R.id.fabInst);
        FloatingActionButton fabPin = (FloatingActionButton) findViewById(R.id.fabPin);
        FloatingActionButton fabLinkedIn = (FloatingActionButton) findViewById(R.id.fabLinkedIn);
        FloatingActionButton fabTwitter = (FloatingActionButton) findViewById(R.id.fabTwitter);
        FloatingActionButton fabGoogle = (FloatingActionButton) findViewById(R.id.fabGoogle);
        FloatingActionButton fabFB = (FloatingActionButton) findViewById(R.id.fabFB);

        btnRetry.setOnClickListener(this);
        fabYou.setOnClickListener(this);
        fabInst.setOnClickListener(this);
        fabPin.setOnClickListener(this);
        fabLinkedIn.setOnClickListener(this);
        fabTwitter.setOnClickListener(this);
        fabGoogle.setOnClickListener(this);
        fabFB.setOnClickListener(this);

        famRoot.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    if (handler != null && !stopSliding) {
                        stopSliding = true;
                        handler.removeCallbacks(animateViewPager);
                    }
                } else {
                    if (handler != null) {
                        stopSliding = false;
                        handler.postDelayed(animateViewPager, duration);
                    }
                }
            }
        });

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
            startActivityForResult(intent, 0);
        } else if (item.getItemId() == R.id.hFeedback) {
            drawerLayout.closeDrawer(navigationView);
            Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
            startActivityForResult(intent, 0);
        } else if (item.getItemId() == R.id.hShare) {
            drawerLayout.closeDrawer(navigationView);
            if (objBusinessMaster != null) {
                if (objBusinessMaster.getCity() != null && !objBusinessMaster.getCity().equals("")) {
                    shareData = objBusinessMaster.getBusinessName() + " | " + objBusinessMaster.getCity() + " | " + objBusinessMaster.getPhone1();
                } else {
                    shareData = objBusinessMaster.getBusinessName() + " | " + objBusinessMaster.getPhone1();
                }
                if (objBusinessMaster.getEmail() != null && !objBusinessMaster.getEmail().equals("")) {
                    shareData = shareData + " | " + objBusinessMaster.getEmail();
                }
                if (objBusinessMaster.getWebsite() != null && !objBusinessMaster.getWebsite().equals("")) {
                    shareData = shareData + " | " + objBusinessMaster.getWebsite();
                }
            } else {
                shareData = "Share Data";
            }
            Intent shareIntent = ShareCompat.IntentBuilder.from(HomeActivity.this)
                    .setType("text/plain")
                    .setText(shareData)
                    .getIntent();
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        } else if (item.getItemId() == R.id.hRate) {
            drawerLayout.closeDrawer(navigationView);
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

            }
        } else if (item.getItemId() == R.id.hAboutUs) {
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

        MenuItem menuItem = menu.findItem(R.id.cart_layout);

        relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        final RelativeLayout cartLayout = (RelativeLayout) relativeLayout.findViewById(R.id.cartLayout);
        txtCartNumber = (com.rey.material.widget.TextView) relativeLayout.findViewById(R.id.txtCartNumber);

        SetCartNumber();

        cartLayout.setOnClickListener(this);

        return true;
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (viewPager.getCurrentItem() == size - 1) {
                        viewPager.setCurrentItem(0, true);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1,true);
                    }
                    handler.postDelayed(animateViewPager, duration);
                }
            }
        };
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (txtFullName.getVisibility() == View.VISIBLE) {
            menu.findItem(R.id.myAccount).setVisible(true);
            menu.findItem(R.id.logout).setVisible(true);
        } else {
            menu.findItem(R.id.myAccount).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            if (!isNetCheck) {
                Globals.ClearUserPreference(HomeActivity.this, HomeActivity.this);
            }
            SaveCartDataInSharePreference();
        }
        menu.findItem(R.id.cart_layout).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
            isPause = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isPause) {
            if (handler != null) {
                handler.postDelayed(animateViewPager, duration);
            }
            isPause = false;
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            Globals.ClearUserPreference(HomeActivity.this, HomeActivity.this);
            SaveCartDataInSharePreference();
            txtFullName.setVisibility(View.GONE);
            cbName.setText(getResources().getString(R.string.siSignIn));
            imageView.setImageResource(R.drawable.account_drawable);
            imageView.setPadding(0, 0, 0, 0);
        } else if (id == R.id.myAccount) {
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.myBookings) {
            if (txtFullName.getVisibility() == View.GONE) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.putExtra("Booking", "Booking");
                startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
                intent.putExtra("IsBookingFromMenu", true);
                startActivityForResult(intent, 0);
            }
        } else if (id == R.id.wishList) {
            Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
            startActivityForResult(intent, 0);
        } else if (id == R.id.myOrders) {
            if (txtFullName.getVisibility() == View.GONE) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.putExtra("Order", "Order");
                startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
                startActivityForResult(intent, 0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!famRoot.isOpened()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(HomeActivity.this, getResources().getString(R.string.HoBackMsg), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void BannerResponse(ArrayList<BannerMaster> alBannerMaster) {
        if (isProgressShow) {
            progressDialog.dismiss();
            isProgressShow = false;
        }
        SetSlider(alBannerMaster);
    }

    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        HomeActivity.objBusinessMaster = objBusinessMaster;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvDelivery) {
            Globals.linktoOrderTypeMasterId = (short) Globals.OrderType.HomeDelivery.getValue();
            if(objBusinessMaster!=null) {
                if (objBusinessMaster.getLinktoBusinessGroupMasterId() == 0) {
                    objSharePreferenceManager.RemovePreference("BusinessPreference", "BusinessMasterId", HomeActivity.this);
                    objSharePreferenceManager.ClearPreference("BusinessPreference", HomeActivity.this);
                    objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()), HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    if (objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this) != null && Globals.counter != 0) {
                        Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this));
                        objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()), HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                        startActivityForResult(intent, 0);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, BusinessBranchActivity.class);
                        intent.putExtra("linktoBusinessGroupMasterId", objBusinessMaster.getLinktoBusinessGroupMasterId());
                        startActivityForResult(intent, 111);
                    }
                }
            }

        } else if (v.getId() == R.id.cvTakeAway) {
            Globals.linktoOrderTypeMasterId = (short) Globals.OrderType.TakeAway.getValue();
            if(objBusinessMaster!=null) {
                if (objBusinessMaster.getLinktoBusinessGroupMasterId() == 0) {
                    objSharePreferenceManager.RemovePreference("BusinessPreference", "BusinessMasterId", HomeActivity.this);
                    objSharePreferenceManager.ClearPreference("BusinessPreference", HomeActivity.this);
                    objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()), HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    if (objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this) != null && Globals.counter != 0) {
                        Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this));
                        objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()), HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                        startActivityForResult(intent, 0);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, BusinessBranchActivity.class);
                        intent.putExtra("linktoBusinessGroupMasterId", objBusinessMaster.getLinktoBusinessGroupMasterId());
                        startActivityForResult(intent, 111);
                    }
                }
            }
        } else if (v.getId() == R.id.cvBookTable) {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.cvOffer) {
            Intent intent = new Intent(HomeActivity.this, OfferActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.nameLayout) {
            drawerLayout.closeDrawer(navigationView);
            if (cbName.getText().equals(getResources().getString(R.string.siSignIn))) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            } else {
                Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                startActivityForResult(intent, 0);
            }
        } else if (v.getId() == R.id.cbName) {
            drawerLayout.closeDrawer(navigationView);
            if (cbName.getText().equals(getResources().getString(R.string.siSignIn))) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            } else {
                Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                startActivityForResult(intent, 0);
            }
        } else if (v.getId() == R.id.cartLayout) {
            Intent intent = new Intent(HomeActivity.this, CartItemActivity.class);
            intent.putExtra("ActivityName", getResources().getString(R.string.title_home));
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.btnRetry) {
            if (Service.CheckNet(HomeActivity.this)) {
                CheckUserNamePassword();
            }
        } else if (v.getId() == R.id.fabFB) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://www.facebook.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabGoogle) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://plus.google.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabTwitter) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://twitter.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabLinkedIn) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://www.linkedin.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabInst) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://www.instagram.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabPin) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://in.pinterest.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (v.getId() == R.id.fabYou) {
            famRoot.close(true);
            Uri uri = Uri.parse("https://www.youtube.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    isLogin = data.getBooleanExtra("IsLogin", false);
                    if (data.getBooleanExtra("IsShowMessage", false)) {
                        Globals.ShowSnackBar(drawerLayout, getResources().getString(R.string.siLoginSuccessMsg), HomeActivity.this, 2000);
                    } else if (data.getBooleanExtra("ShowBookingMessage", false)) {
                        ShowSnackBarWithAction(getResources().getString(R.string.ybAddBookingSuccessMsg));
                    } else if (data.getBooleanExtra("IsOrderPlace", false)) {
                        ShowSnackBarWithAction(getResources().getString(R.string.MsgOrder));
                    }
                    if (data.getBooleanExtra("IsBranchChange", false)) {
                        if (Service.CheckNet(this)) {
                            internetLayout.setVisibility(View.GONE);
                            famRoot.setVisibility(View.VISIBLE);
                            homeLayout.setVisibility(View.VISIBLE);
                            isProgressShow = false;
                            isRefresh = true;
                            RequestBannerMaster();
                            RequestBusinessMaster();
                        } else {
                            internetLayout.setVisibility(View.VISIBLE);
                            famRoot.setVisibility(View.GONE);
                            Globals.SetErrorLayout(internetLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
                            homeLayout.setVisibility(View.GONE);
                        }
                    }
                }
                SetUserName();
                SaveCartDataInSharePreference();
                SetCartNumber();
                SetWishListFromSharePreference();
            } else if (requestCode == 123) {
                if (data != null) {
                    if (data.getBooleanExtra("IsRedirect", false)) {
                        if (data.getStringExtra("TargetActivity") != null && data.getStringExtra("TargetActivity").equals("Booking")) {
                            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
                            intent.putExtra("IsShowMessage", true);
                            intent.putExtra("IsBookingFromMenu", true);
                            startActivityForResult(intent, 0);
                        } else if (data.getStringExtra("TargetActivity") != null && data.getStringExtra("TargetActivity").equals("Order")) {
                            Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
                            intent.putExtra("IsShowMessage", true);
                            startActivityForResult(intent, 0);
                        }
                    }
                }
            } else if (requestCode == 111) {
                if (Globals.linktoOrderTypeMasterId == (short) Globals.OrderType.HomeDelivery.getValue()) {
                    objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.HomeDelivery.getValue()), HomeActivity.this);
                } else if (Globals.linktoOrderTypeMasterId == (short) Globals.OrderType.TakeAway.getValue()) {
                    objSharePreferenceManager.CreatePreference("OrderTypePreference", "OrderType", String.valueOf(Globals.OrderType.TakeAway.getValue()), HomeActivity.this);
                }
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                intent.putExtra("IsBranchChange", true);
                startActivityForResult(intent, 0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        if (objCustomerMaster == null) {
            intent.putExtra("IsLogin", false);
        } else {
            intent.putExtra("IsLogin", true);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //region Private Methods
    private void RequestBannerMaster() {
        if (isProgressShow) {
            progressDialog.show(getSupportFragmentManager(), "");
        }
        BannerMasterJSONParser objBannerMasterJSONParser = new BannerMasterJSONParser();
        objBannerMasterJSONParser.SelectAllBannerMaster(HomeActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void RequestBusinessMaster() {
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        objBusinessJSONParser.SelectBusinessMaster(HomeActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetSlider(ArrayList<BannerMaster> alBannerMaster) {
        if (alBannerMaster != null && alBannerMaster.size() != 0) {
            circlePageIndicator.setVisibility(View.VISIBLE);
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
            pagerAdapter.addAll(alBannerMaster);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            viewPager.setAnimation(AnimationUtils.makeInAnimation(HomeActivity.this, false));
            //circlePageIndicator.initViewPager(viewPager);
            circlePageIndicator.setViewPager(viewPager);
            runnable(alBannerMaster.size());

            //Re-run callback
            handler.postDelayed(animateViewPager, duration);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_CANCEL:
                            break;

                        case MotionEvent.ACTION_UP:
                            // calls when touch release on ViewPager
                            stopSliding = false;
                            handler.postDelayed(animateViewPager, 5000);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // calls when ViewPager touch
                            if (handler != null && !stopSliding) {
                                stopSliding = true;
                                handler.removeCallbacks(animateViewPager);
                            }
                            break;
                    }
                    return false;

                }
            });
        } else {
            if (handler != null && !stopSliding) {
                stopSliding = true;
                handler.removeCallbacks(animateViewPager);
            }
            viewPager.removeAllViews();
            viewPager.removeAllViewsInLayout();
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
            alBannerMaster = new ArrayList<>();
            pagerAdapter.addAll(alBannerMaster);
            viewPager.setAdapter(pagerAdapter);
            circlePageIndicator.setViewPager(viewPager);
            circlePageIndicator.setVisibility(View.INVISIBLE);
            stopSliding = false;
            handler = null;
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
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerProfileUrl", HomeActivity.this) != null) {
                String url = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerProfileUrl", HomeActivity.this);
                Glide.with(HomeActivity.this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                        imageView.setPadding(8, 8, 8, 8);
                    }
                });
            } else {
                imageView.setImageResource(R.drawable.account_drawable);
                imageView.setPadding(0, 0, 0, 0);
            }
            isLogin = false;
        }
    }

    private void ShowSnackBarWithAction(final String msg) {
        //getResources().getString(R.string.ybAddBookingSuccessMsg)
        Snackbar snackbar = Snackbar
                .make(drawerLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("View", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (msg.equals(getResources().getString(R.string.ybAddBookingSuccessMsg))) {
                            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
                            intent.putExtra("IsBookingFromMenu", true);
                            startActivityForResult(intent, 0);
                        } else {
                            Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
                            startActivityForResult(intent, 0);
                        }

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
        } else {
            txtCartNumber.setText("");
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
                    Globals.alOrderItemTran.addAll(new ArrayList<>(lstItemMaster));
                    Globals.counter = Globals.alOrderItemTran.size();
                    if (objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", HomeActivity.this) != null) {
                        RemarkDialogFragment.strRemark = objSharePreferenceManage.GetPreference("CartItemListPreference", "OrderRemark", HomeActivity.this);
                    }
                } else {
                    objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", HomeActivity.this);
                    objSharePreferenceManage.ClearPreference("CheckOutDataPreference", HomeActivity.this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetWishListFromSharePreference() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", HomeActivity.this) != null) {
            ItemAdapter.alWishItemMaster = new ArrayList<>();
            ArrayList<String> alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", HomeActivity.this);
            if (alString.size() > 0) {
                for (String itemMasterId : alString) {
                    ItemMaster objItemMaster = new ItemMaster();
                    objItemMaster.setItemMasterId(Integer.parseInt(itemMasterId));
                    objItemMaster.setIsChecked((short) 1);
                    ItemAdapter.alWishItemMaster.add(objItemMaster);
                }
            }
        } else {
            ItemAdapter.alWishItemMaster = new ArrayList<>();
        }
    }

    private void CheckUserNamePassword() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this) != null && objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", HomeActivity.this) != null) {
            String userName = objSharePreferenceManage.GetPreference("LoginPreference", "UserName", HomeActivity.this);
            String userPassword = objSharePreferenceManage.GetPreference("LoginPreference", "UserPassword", HomeActivity.this);
            if (!userName.isEmpty() && !userPassword.isEmpty()) {
                if (Service.CheckNet(HomeActivity.this)) {
                    CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
                    objCustomerJSONParser.SelectCustomerMaster(HomeActivity.this, userName, userPassword, null, null, String.valueOf(Globals.linktoBusinessMasterId));
                } else {
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    intent.putExtra("IsNetCheck", true);
                    startActivity(intent);
                }

            } else {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void SetBusinessMasterID() {
        if (objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this) != null && Globals.counter != 0) {
            Globals.linktoBusinessMasterId = Short.parseShort(objSharePreferenceManager.GetPreference("BusinessPreference", "BusinessMasterId", HomeActivity.this));
        }
    }
    //endregion

    //region Page Adapter
    public class SlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<BannerMaster> lstBannerMaster;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return SliderFragment.createInstance(lstBannerMaster.get(i));
        }

        @Override
        public int getCount() {
            return lstBannerMaster.size();
        }

        public void addAll(List<BannerMaster> lstBannerMaster) {
            this.lstBannerMaster = lstBannerMaster;
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    //endregion
}



