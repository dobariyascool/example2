package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.TaxMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaxJSONParser
{
    public String SelectAllTaxMaster = "SelectAllTaxMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    TaxMasterRequestListener objTaxMasterRequestListener;

    private TaxMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        TaxMaster objTaxMaster = null;
        try {
            if (jsonObject != null) {
                objTaxMaster = new TaxMaster();
                objTaxMaster.setTaxMasterId((short) jsonObject.getInt("TaxMasterId"));
                objTaxMaster.setTaxName(jsonObject.getString("TaxName"));
                objTaxMaster.setTaxCaption(jsonObject.getString("TaxCaption"));
                objTaxMaster.setTaxIndex((short) jsonObject.getInt("TaxIndex"));
                objTaxMaster.setTaxRate(jsonObject.getDouble("TaxRate"));
                objTaxMaster.setIsPercentage(jsonObject.getBoolean("IsPercentage"));
                objTaxMaster.setlinktoBusinessMasterId((short)jsonObject.getInt("linktoBusinessMasterId"));
                objTaxMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreatedDateTime"));
                objTaxMaster.setCreatedDateTime(sdfControlDateFormat.format(dt));
                objTaxMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTaxMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objTaxMaster.setDefaultTaxRate(jsonObject.getDouble("DefaultTaxRate"));
                objTaxMaster.setlinktoItemMasterId(jsonObject.getInt("linktoItemMasterId"));

            }
            return objTaxMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<TaxMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<TaxMaster> lstTaxMaster = new ArrayList<>();
        TaxMaster objTaxMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objTaxMaster = new TaxMaster();
                objTaxMaster.setTaxMasterId((short) jsonArray.getJSONObject(i).getInt("TaxMasterId"));
                objTaxMaster.setTaxName(jsonArray.getJSONObject(i).getString("TaxName"));
                objTaxMaster.setTaxCaption(jsonArray.getJSONObject(i).getString("TaxCaption"));
                objTaxMaster.setTaxIndex((short) jsonArray.getJSONObject(i).getInt("TaxIndex"));
                objTaxMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));
                objTaxMaster.setIsPercentage(jsonArray.getJSONObject(i).getBoolean("IsPercentage"));
                objTaxMaster.setlinktoBusinessMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objTaxMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreatedDateTime"));
                objTaxMaster.setCreatedDateTime(sdfControlDateFormat.format(dt));
                objTaxMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objTaxMaster.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objTaxMaster.setDefaultTaxRate(jsonArray.getJSONObject(i).getDouble("DefaultTaxRate"));
                objTaxMaster.setlinktoItemMasterId(jsonArray.getJSONObject(i).getInt("linktoItemMasterId"));
                lstTaxMaster.add(objTaxMaster);
            }
            return lstTaxMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //region SelectAll
    public void SelectAllTaxMaster(String linktoBusinessMasterId, final Context context,final Fragment targetFragment) {
        String url = Service.Url + this.SelectAllTaxMaster + "/" + linktoBusinessMasterId ;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllTaxMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<TaxMaster> taxMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objTaxMasterRequestListener = (TaxMasterRequestListener)targetFragment;
                        objTaxMasterRequestListener.TaxMasterResponse(taxMaster);
                    }
                } catch (Exception e) {
                    objTaxMasterRequestListener = (TaxMasterRequestListener)targetFragment;
                    objTaxMasterRequestListener.TaxMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objTaxMasterRequestListener = (TaxMasterRequestListener)targetFragment;
                objTaxMasterRequestListener.TaxMasterResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public interface TaxMasterRequestListener {
        void TaxMasterResponse(ArrayList<TaxMaster> alTaxMaster);
    }
    //endregion
}
