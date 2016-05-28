package com.arraybit.parser;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BannerMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BannerMasterJSONParser {

    public String SelectAllBannerMaster = "SelectAllBannerMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    BannerRequestListener objBannerRequestListener;

    private BannerMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BannerMaster objBannerMaster = null;
        try {
            if (jsonObject != null) {
                objBannerMaster = new BannerMaster();
                objBannerMaster.setBannerMasterId(jsonObject.getInt("BannerMasterId"));
                objBannerMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objBannerMaster.setBannerTitle(jsonObject.getString("BannerTitle"));
                objBannerMaster.setBannerDescription(jsonObject.getString("BannerDescription"));
                objBannerMaster.setImageName(jsonObject.getString("mdImageName"));
                objBannerMaster.setLGImageName(jsonObject.getString("lgImageName"));
                if (!jsonObject.getString("Type").equals("null")) {
                    objBannerMaster.setType((short) jsonObject.getInt("Type"));
                }
                if (!jsonObject.getString("ID").equals("null")) {
                    objBannerMaster.setID(jsonObject.getInt("ID"));
                }
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objBannerMaster.setSortOrder((short) jsonObject.getInt("SortOrder"));
                }
                objBannerMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objBannerMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objBannerMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBannerMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objBannerMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objBannerMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BannerMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BannerMaster> lstBannerMaster = new ArrayList<>();
        BannerMaster objBannerMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBannerMaster = new BannerMaster();
                objBannerMaster.setBannerMasterId(jsonArray.getJSONObject(i).getInt("BannerMasterId"));
                objBannerMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objBannerMaster.setBannerTitle(jsonArray.getJSONObject(i).getString("BannerTitle"));
                objBannerMaster.setBannerDescription(jsonArray.getJSONObject(i).getString("BannerDescription"));
                objBannerMaster.setImageName(jsonArray.getJSONObject(i).getString("mdImageName"));
                objBannerMaster.setLGImageName(jsonArray.getJSONObject(i).getString("lgImageName"));
                if (!jsonArray.getJSONObject(i).getString("Type").equals("null")) {
                    objBannerMaster.setType((short)jsonArray.getJSONObject(i).getInt("Type"));
                }
                if (!jsonArray.getJSONObject(i).getString("ID").equals("null")) {
                    objBannerMaster.setID(jsonArray.getJSONObject(i).getInt("ID"));
                }
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objBannerMaster.setSortOrder((short)jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objBannerMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objBannerMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objBannerMaster.setlinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBannerMaster.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objBannerMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBannerMaster.add(objBannerMaster);
            }
            return lstBannerMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public void SelectAllBannerMaster(final Context context,String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllBannerMaster + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllBannerMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<BannerMaster> alBannerMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objBannerRequestListener = (BannerRequestListener) context;
                        objBannerRequestListener.BannerResponse(alBannerMaster);
                    }
                } catch (Exception e) {
                    objBannerRequestListener = (BannerRequestListener) context;
                    objBannerRequestListener.BannerResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBannerRequestListener = (BannerRequestListener) context;
                objBannerRequestListener.BannerResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }


    public interface BannerRequestListener {
        void BannerResponse(ArrayList<BannerMaster> alBannerMaster);
    }

}
