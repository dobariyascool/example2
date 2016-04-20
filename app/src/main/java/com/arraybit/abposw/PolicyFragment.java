package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessDescription;
import com.arraybit.parser.BusinessDescriptionJSONParser;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class PolicyFragment extends Fragment implements BusinessDescriptionJSONParser.BusinessDescriptionRequestListener {

    String keyword;
    BusinessDescription objBusinessDescription;
    WebView wvPolicy;
    ProgressDialog progressDialog = new ProgressDialog();

    public PolicyFragment(String keyword) {
        this.keyword = keyword;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        if (keyword == null) {
            app_bar.setTitle(keyword);
        } else {
            app_bar.setTitle(keyword);
        }
        setHasOptionsMenu(true);

        RequestKeyword();

        wvPolicy = (WebView) view.findViewById(R.id.wvPolicy);
        wvPolicy.getSettings().setDatabaseEnabled(true);
        wvPolicy.getSettings().setDomStorageEnabled(true);
        wvPolicy.getSettings().setAppCacheEnabled(true);
        wvPolicy.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BusinessDescriptionResponse(BusinessDescription objBusinessDescription) {
        progressDialog.dismiss();
        this.objBusinessDescription = objBusinessDescription;

        if (objBusinessDescription == null || objBusinessDescription.getDescription().equals("")) {
            wvPolicy.setVisibility(View.GONE);
        } else {
            wvPolicy.setVisibility(View.VISIBLE);
            String customHtml = objBusinessDescription.getDescription();
            wvPolicy.loadData(customHtml, "text/html; charset=UTF-8", null);
        }
    }

    // region Private Method
    private void RequestKeyword() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");
        BusinessDescriptionJSONParser objBusinessDescriptionJSONParser = new BusinessDescriptionJSONParser();
        objBusinessDescriptionJSONParser.SelectBusinessDescription(getActivity(), this, String.valueOf(Globals.linktoBusinessMasterId), keyword);
    }
    //endregion

}
