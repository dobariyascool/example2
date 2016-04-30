package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;
import com.rey.material.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class AboutUsActivity extends AppCompatActivity implements BusinessDescriptionJSONParser.BusinessDescriptionRequestListener {
    CardView cardPolicy, cardTerms;
    BusinessDescription objBusinessDescription = new BusinessDescription();
    WebView wvAbout;
    ProgressDialog progressDialog;

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        cardTerms = (CardView) findViewById(R.id.cardTerms);
        cardPolicy = (CardView) findViewById(R.id.cardPolicy);

        final TextView txtCardPolicy = (TextView) findViewById(R.id.txtCardPolicy);
        final TextView txtCardTerms = (TextView) findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) findViewById(R.id.txtVersionCode);

        RequestBusinessDescription();

        wvAbout = (WebView) findViewById(R.id.wvAbout);
        wvAbout.getSettings().setJavaScriptEnabled(true);
        wvAbout.getSettings().setDatabaseEnabled(true);
        wvAbout.getSettings().setDomStorageEnabled(true);
        wvAbout.getSettings().setAppCacheEnabled(true);
        wvAbout.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvAbout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        txtVersionCode.setText(getResources().getString(R.string.abVersionCode) + " " + BuildConfig.VERSION_CODE + "\n" + getResources().getString(R.string.abVersionName) + " " + BuildConfig.VERSION_NAME);

        txtCardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment(txtCardPolicy.getText().toString()), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_policy), R.id.aboutFragment);
            }
        });

        txtCardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment(txtCardTerms.getText().toString()), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_terms_of_service), R.id.aboutFragment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void BusinessDescriptionResponse(BusinessDescription objBusinessDescription) {
        progressDialog.dismiss();
        this.objBusinessDescription = objBusinessDescription;

        if (objBusinessDescription == null || objBusinessDescription.getDescription().equals("")) {
            wvAbout.setVisibility(View.GONE);
        } else {
            wvAbout.setVisibility(View.VISIBLE);
            String customHtml = objBusinessDescription.getDescription();
            wvAbout.loadData(customHtml, "text/html; charset=UTF-8", null);
        }
    }

    //region Private Methods
    private void RequestBusinessDescription() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
        objBusinessDescriptionJSONParser.SelectBusinessDescription(AboutUsActivity.this, null, String.valueOf(Globals.linktoBusinessMasterId), "About Us");
    }
    //endregion
}
