package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.RegisteredUserMaster;
import com.arraybit.parser.RegisteredUserJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.RadioButton;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RadioButton chk2;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = (Button)findViewById(R.id.btn);
        chk2 = (RadioButton)findViewById(R.id.chk2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQtyRemarkDialogFragment addQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(null);
                addQtyRemarkDialogFragment.show(getSupportFragmentManager(),"");
            }
        });

       // GetResponse();

//        WebView webview = (WebView)findViewById(R.id.webview);
//        String str="<ul>\n" +
//                "\t<li>Valid only for first time user of Box8</li>\n" +
//                "\t<li>\n" +
//                "\t<p>&nbsp;Offer cannot be availed by any user who has ever ordered from Box8 either through our own channel (website/app/call center) or any other third party vendor like foodpanda, zomato etc.</p>\n" +
//                "\t</li>\n" +
//                "\t<li>\n" +
//                "\t<p>Offer not valid using cash on delivery payment option</p>\n" +
//                "\t</li>\n" +
//                "\t<li>\n" +
//                "\t<p>Not valid on drinks, desserts, sides, rotis &amp; rice</p>\n" +
//                "\t</li>\n" +
//                "\t<li>\n" +
//                "\t<p>Applicable only on our website/app and cannot be availed through the call center 6. Min order of Rs. 300 is exclusive of taxes and before discount</p>\n" +
//                "\n" +
//                "\t<p>&nbsp;</p>\n" +
//                "\t</li>\n" +
//                "</ul>\n";
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setDatabaseEnabled(true);
//        webview.getSettings().setDomStorageEnabled(true);
//        webview.getSettings().setAppCacheEnabled(true);
//        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        webview.loadData(str, "text/html; charset=UTF-8", null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SetUI(){
        chk2.setChecked(true);
    }


    private void GetResponse(){
        final com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
        progressDialog.show(getSupportFragmentManager(),"");
        final RegisteredUserJSONParser objRegisteredUserJSONParser=new RegisteredUserJSONParser();
        String url = Service.Url + objRegisteredUserJSONParser.SelectAllRegisteredUserMaster;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET,url,new JSONObject(),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ArrayList<RegisteredUserMaster> arrayList = objRegisteredUserJSONParser.SelectAllRegisteredUserMasterPageWise(jsonObject);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
        queue.add(jsonObjectRequest);
    }

}
