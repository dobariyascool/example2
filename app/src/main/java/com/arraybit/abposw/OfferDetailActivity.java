package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.OfferItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.OfferMaster;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class OfferDetailActivity extends AppCompatActivity implements OfferJSONParser.OfferRequestListener {

    ProgressDialog progressDialog = new ProgressDialog();
    OfferMaster objOfferMaster;
    NestedScrollView scrollView;
    LinearLayout errorLayout, termsConditionLayout, discountLayout;
    ImageView ivOfferImage, ivTimings;
    int offerMasterId;
    TextView txtOfferTitle, txtOfferContent, txtFromDate, txtValidDays, txtMinBillAmt, txtOfferCustomer, txtOfferCode, txtOfferCondition, txtOfferDiscount, txtBuyGetItem, txtValidFor;
    WebView wvCondition;
    ImageButton ibVisible;
    int isShow = 0;
    RecyclerView rvSelectedItem, rvBuyItem, rvGetItem;
    StringBuilder sbDays, sbOrderType;
    CardView cvCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);
        termsConditionLayout = (LinearLayout) findViewById(R.id.termsConditionLayout);
        discountLayout = (LinearLayout) findViewById(R.id.discountLayout);

        ivOfferImage = (ImageView) findViewById(R.id.ivOfferImage);
        ivTimings = (ImageView) findViewById(R.id.ivTimings);
        ibVisible = (ImageButton) findViewById(R.id.ibVisible);

        offerMasterId = getIntent().getIntExtra("OfferMasterId", 0);

        cvCondition = (CardView) findViewById(R.id.cvCondition);

        rvSelectedItem = (RecyclerView) findViewById(R.id.rvSelectedItem);
        rvBuyItem = (RecyclerView) findViewById(R.id.rvBuyItem);
        rvGetItem = (RecyclerView) findViewById(R.id.rvGetItem);

        txtOfferTitle = (TextView) findViewById(R.id.txtOfferTitle);
        txtOfferContent = (TextView) findViewById(R.id.txtOfferContent);
        txtFromDate = (TextView) findViewById(R.id.txtFromDate);
        txtValidDays = (TextView) findViewById(R.id.txtValidDays);
        txtMinBillAmt = (TextView) findViewById(R.id.txtMinBillAmt);
        txtOfferCustomer = (TextView) findViewById(R.id.txtOfferCustomer);
        txtOfferCode = (TextView) findViewById(R.id.txtOfferCode);
        txtOfferCondition = (TextView) findViewById(R.id.txtOfferCondition);
        txtOfferDiscount = (TextView) findViewById(R.id.txtOfferDiscount);
        txtBuyGetItem = (TextView) findViewById(R.id.txtBuyGetItem);
        txtValidFor = (TextView) findViewById(R.id.txtValidFor);
        //txtOnlineApp = (TextView) findViewById(R.id.txtOnlineApp);

        wvCondition = (WebView) findViewById(R.id.wvCondition);
        wvCondition.getSettings().setDatabaseEnabled(true);
        wvCondition.getSettings().setDomStorageEnabled(true);
        wvCondition.getSettings().setAppCacheEnabled(true);
        wvCondition.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvCondition.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (Service.CheckNet(this)) {
            scrollView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            RequestOfferMaster();
        } else {
            scrollView.setVisibility(View.GONE);
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), null, R.drawable.wifi_drawable);
        }

        termsConditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow == 0) {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(OfferDetailActivity.this, R.drawable.collapse_drawable));
                    cvCondition.setVisibility(View.VISIBLE);
                    wvCondition.loadData(objOfferMaster.getTermsAndConditions(), "text/html", "UTF-8");
//                    wvCondition.setBackgroundColor(ContextCompat.getColor(OfferDetailActivity.this, R.color.offWhiteDark));
                    isShow = 1;
                } else {
                    ibVisible.setImageDrawable(ContextCompat.getDrawable(OfferDetailActivity.this, R.drawable.expand_drawable));
                    cvCondition.setVisibility(View.GONE);
                    isShow = 0;
                }
            }
        });

    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster) {
        progressDialog.dismiss();
        this.objOfferMaster = objOfferMaster;
        SetOfferDetail();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //region Description
    private void RequestOfferMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        OfferJSONParser objOfferJSONParser = new OfferJSONParser();
        if (offerMasterId != 0) {
            objOfferJSONParser.SelectOfferMaster(OfferDetailActivity.this, offerMasterId);
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetOfferDetail() {
        if (objOfferMaster != null) {
            if (objOfferMaster.getMd_ImagePhysicalName() != null && !objOfferMaster.getMd_ImagePhysicalName().equals("")) {
                Picasso.with(OfferDetailActivity.this).load(objOfferMaster.getMd_ImagePhysicalName()).into(ivOfferImage);
            }
            txtOfferTitle.setText(objOfferMaster.getOfferTitle());
            if (objOfferMaster.getOfferContent() != null && !objOfferMaster.getOfferContent().equals("")) {
                txtOfferContent.setVisibility(View.VISIBLE);
                txtOfferContent.setText(objOfferMaster.getOfferContent());
            } else {
                txtOfferContent.setVisibility(View.GONE);
            }

            if (objOfferMaster.getFromDate() != null && !objOfferMaster.getFromDate().equals("") && objOfferMaster.getToDate() != null && !objOfferMaster.getToDate().equals("")) {
                txtFromDate.setVisibility(View.VISIBLE);
                if (objOfferMaster.getFromTime() != null && !objOfferMaster.getFromTime().equals("") && objOfferMaster.getToTime() != null && !objOfferMaster.getToTime().equals("")) {
                    try {
                        Date fromTime = new SimpleDateFormat(Globals.TimeFormat, Locale.US).parse(objOfferMaster.getFromTime());
                        Date toTime = new SimpleDateFormat(Globals.TimeFormat, Locale.US).parse(objOfferMaster.getToTime());
                        txtFromDate.setText(objOfferMaster.getFromDate() + " To " + objOfferMaster.getToDate() + "  " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(fromTime) + " To " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(toTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    txtFromDate.setText(objOfferMaster.getFromDate() + " To " + objOfferMaster.getToDate());
                }
            } else {
                ivTimings.setVisibility(View.GONE);
                txtFromDate.setVisibility(View.GONE);
            }

            if (objOfferMaster.getValidDays() == null || objOfferMaster.getValidDays().equals("")) {
                txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidFor), "All Days"));
            } else {
                String[] strArray = objOfferMaster.getValidDays().split(",");
                sbDays = new StringBuilder();
                if (strArray.length < 7) {
                    for (String strDay : strArray) {
                        if (strDay.equals(strArray[strArray.length - 1])) {
                            sbDays.append(strDay);
                        } else {
                            sbDays.append(strDay).append(", ");
                        }
                    }
                    txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidOn), sbDays.toString()));
                } else {
                    txtValidDays.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaValidFor), "All Days"));
                }

            }
            if (objOfferMaster.getMinimumBillAmount() != 0) {
                txtMinBillAmt.setVisibility(View.VISIBLE);
                txtMinBillAmt.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaMinAmt), Globals.dfWithPrecision.format(objOfferMaster.getMinimumBillAmount())));
            } else {
                txtMinBillAmt.setVisibility(View.GONE);
            }
            if (objOfferMaster.getIsForCustomers()) {
                txtOfferCustomer.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferFor), "Customer"));
            } else {
                txtOfferCustomer.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferFor), "Register User"));
            }
            if (objOfferMaster.getOfferCode() != null && !objOfferMaster.getOfferCode().equals("")) {
                txtOfferCode.setVisibility(View.VISIBLE);
                txtOfferCode.setText(objOfferMaster.getOfferCode());
            } else {
                txtOfferCode.setVisibility(View.GONE);
            }
            if (objOfferMaster.getTermsAndConditions() != null && !objOfferMaster.getTermsAndConditions().equals("")) {
                cvCondition.setVisibility(View.GONE);
                termsConditionLayout.setVisibility(View.VISIBLE);
            } else {
                cvCondition.setVisibility(View.GONE);
                termsConditionLayout.setVisibility(View.GONE);
            }
            if (objOfferMaster.getDiscount() != 0) {
                discountLayout.setVisibility(View.VISIBLE);
                if (objOfferMaster.getIsDiscountPercentage()) {
                    String strOfferDiscount = String.valueOf(objOfferMaster.getDiscount()).substring(0, String.valueOf(objOfferMaster.getDiscount()).lastIndexOf("."));
                    txtOfferDiscount.setText(String.format(getResources().getString(R.string.odaOfferDiscount), strOfferDiscount));
                } else {
                    txtOfferDiscount.setText(String.format(getResources().getString(R.string.odaOfferDiscountWithRupee), Globals.dfWithPrecision.format(objOfferMaster.getDiscount())));
                }

            } else {
                discountLayout.setVisibility(View.GONE);
            }
            if (objOfferMaster.getValidItems() != null && !objOfferMaster.getValidItems().equals("")) {
                txtBuyGetItem.setVisibility(View.VISIBLE);
                txtBuyGetItem.setText("Selected Items");
                ArrayList<String> alString = new ArrayList<>(Arrays.asList(objOfferMaster.getValidItems().split(",")));
                OfferItemAdapter offerItemAdapter = new OfferItemAdapter(OfferDetailActivity.this, alString, "");
                rvSelectedItem.setVisibility(View.VISIBLE);
                rvSelectedItem.setAdapter(offerItemAdapter);
                rvSelectedItem.setLayoutManager(new LinearLayoutManager(OfferDetailActivity.this));
            } else {
                rvSelectedItem.setVisibility(View.GONE);
                if (objOfferMaster.getBuyItemCount() == 0 && objOfferMaster.getGetItemCount() == 0) {
                    txtBuyGetItem.setVisibility(View.GONE);
                } else {
                    txtBuyGetItem.setVisibility(View.VISIBLE);
                    if (objOfferMaster.getBuyItemCount() > 1 && objOfferMaster.getGetItemCount() > 1) {
                        txtBuyGetItem.setText("Buy " + objOfferMaster.getBuyItemCount() + " items Get " + objOfferMaster.getGetItemCount() + " items");
                    } else {
                        txtBuyGetItem.setText("Buy " + objOfferMaster.getBuyItemCount() + " item Get " + objOfferMaster.getGetItemCount() + " item");
                    }

                    if (objOfferMaster.getValidBuyItems() != null && !objOfferMaster.getValidBuyItems().equals("")) {
                        ArrayList<String> alBuyItem = new ArrayList<>(Arrays.asList(objOfferMaster.getValidBuyItems().split(",")));
                        OfferItemAdapter offerBuyItemAdapter = new OfferItemAdapter(OfferDetailActivity.this, alBuyItem, "Buy Items");
                        rvBuyItem.setVisibility(View.VISIBLE);
                        rvBuyItem.setAdapter(offerBuyItemAdapter);
                        rvBuyItem.setLayoutManager(new LinearLayoutManager(OfferDetailActivity.this));
                    }
                    if (objOfferMaster.getValidGetItems() != null && !objOfferMaster.getValidGetItems().equals("")) {
                        ArrayList<String> alGetItem = new ArrayList<>(Arrays.asList(objOfferMaster.getValidGetItems().split(",")));
                        OfferItemAdapter offerGetItemAdapter = new OfferItemAdapter(OfferDetailActivity.this, alGetItem, "Get Items");
                        rvGetItem.setVisibility(View.VISIBLE);
                        rvGetItem.setAdapter(offerGetItemAdapter);
                        rvGetItem.setLayoutManager(new LinearLayoutManager(OfferDetailActivity.this));
                    }
                }
            }

            if (objOfferMaster.getLinktoOrderTypeMasterIds() == null || objOfferMaster.getLinktoOrderTypeMasterIds().equals("")) {
                txtValidFor.setVisibility(View.GONE);
            } else {
                txtValidFor.setVisibility(View.VISIBLE);
                String[] strOrderType = objOfferMaster.getLinktoOrderTypeMasterIds().split(",");
                sbOrderType = new StringBuilder();
                for (String orderType : strOrderType) {
                    if (orderType.equals(strOrderType[strOrderType.length - 1])) {
                        if (orderType.equals(String.valueOf(Globals.OrderType.DineIn.getValue()))) {
                            sbOrderType.append("Dine In");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.TakeAway.getValue()))) {
                            sbOrderType.append("Take Away");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.HomeDelivery.getValue()))) {
                            sbOrderType.append("Home Delivery");
                        }
                    } else {
                        if (orderType.equals(String.valueOf(Globals.OrderType.DineIn.getValue()))) {
                            sbOrderType.append("Dine In").append(", ");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.TakeAway.getValue()))) {
                            sbOrderType.append("Take Away").append(", ");
                        } else if (orderType.equals(String.valueOf(Globals.OrderType.HomeDelivery.getValue()))) {
                            sbOrderType.append("Home Delivery").append(", ");
                        }
                    }
                }
                if ((objOfferMaster.getIsForApp()) && (objOfferMaster.getIsOnline())) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValid) + ") ");
                } else if (objOfferMaster.getIsOnline()) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValidOnline) + ") ");
                } else if (objOfferMaster.getIsForApp()) {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()) + " (" + getResources().getString(R.string.odaOfferValidApp) + ") ");
                } else {
                    txtValidFor.setText(getResources().getString(R.string.odaDiamond) + " " + String.format(getResources().getString(R.string.odaOfferOnlyFor), sbOrderType.toString()));
                }
            }
        }
    }
    //endregion
}
