package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class AboutUsActivity extends AppCompatActivity implements BusinessDescriptionJSONParser.BusinessDescriptionRequestListener {
    CardView cardPolicy,cardTerms;
    LinearLayout versionLayout;
    ArrayList<BusinessDescription> alBusinessDescription;

    @SuppressLint("SetTextI18n")
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

        TextView txtCardPolicy = (TextView) findViewById(R.id.txtCardPolicy);
        TextView txtCardTerms = (TextView) findViewById(R.id.txtCardTerms);
        TextView txtVersionCode = (TextView) findViewById(R.id.txtVersionCode);

//        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 19) {
//            versionLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//            txtCardPolicy.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//            txtCardTerms.setBackground(ContextCompat.getDrawable(this, R.drawable.card_view_with_border));
//        }

        txtVersionCode.setText(getResources().getString(R.string.abVersionCode) + " " + BuildConfig.VERSION_CODE + "\n" + getResources().getString(R.string.abVersionName) + " " + BuildConfig.VERSION_NAME);

        txtCardPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment((short)1),getSupportFragmentManager(),getResources().getString(R.string.title_fragment_policy),R.id.aboutFragment);
            }
        });

        txtCardTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.ReplaceFragment(new PolicyFragment((short) 1), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_policy), R.id.aboutFragment);
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
    public void BusinessDescriptionResponse(ArrayList<BusinessDescription> alBusinessDescription) {
        this.alBusinessDescription = alBusinessDescription;
    }

    //region Private Methods
    private void RequestBusinessInfo() {
        BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
        objBusinessDescriptionJSONParser.SelectAllBusinessDescription(AboutUsActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

}
