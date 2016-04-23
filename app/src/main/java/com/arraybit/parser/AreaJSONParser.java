package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.AreaMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AreaJSONParser {

    public String SelectAllAreaMasterByCity = "SelectAllAreaMasterByCity";
    AreaRequestListener objAreaRequestListener;

    //region Class Methods
    private AreaMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        AreaMaster objAreaMaster = null;
        try {
            if (jsonObject != null) {
                objAreaMaster = new AreaMaster();
                objAreaMaster.setAreaMasterId((short) jsonObject.getInt("AreaMasterId"));
                objAreaMaster.setAreaName(jsonObject.getString("AreaName"));
                objAreaMaster.setZipCode(jsonObject.getString("ZipCode"));
                objAreaMaster.setlinktoCityMasterId((short) jsonObject.getInt("linktoCityMasterId"));
                objAreaMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objAreaMaster.setCity(jsonObject.getString("City"));
            }
            return objAreaMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<AreaMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<AreaMaster> lstAreaMaster = new ArrayList<>();
        AreaMaster objAreaMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objAreaMaster = new AreaMaster();
                objAreaMaster.setAreaMasterId((short) jsonArray.getJSONObject(i).getInt("AreaMasterId"));
                objAreaMaster.setAreaName(jsonArray.getJSONObject(i).getString("AreaName"));
                objAreaMaster.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                objAreaMaster.setlinktoCityMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCityMasterId"));
                objAreaMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objAreaMaster.setCity(jsonArray.getJSONObject(i).getString("City"));
                lstAreaMaster.add(objAreaMaster);
            }
            return lstAreaMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //region SelectAll
    public void SelectAllAreaMasterAreaByCity(final Fragment targetFragment, final Context context, String linktoCityMasterId) {
        String url = Service.Url + this.SelectAllAreaMasterByCity + "/" + linktoCityMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                ArrayList<SpinnerItem> lstSpinnerItem = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllAreaMasterByCity + "Result");
                    if (jsonArray != null) {
                        lstSpinnerItem = new ArrayList<>();
                        SpinnerItem objSpinnerItem;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objSpinnerItem = new SpinnerItem();
                            objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("AreaName"));
                            objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("AreaMasterId"));
                            lstSpinnerItem.add(objSpinnerItem);
                        }
                    }
                    objAreaRequestListener = (AreaRequestListener) targetFragment;
                    objAreaRequestListener.AreaResponse(lstSpinnerItem);
                } catch (Exception e) {
                    objAreaRequestListener = (AreaRequestListener) targetFragment;
                    objAreaRequestListener.AreaResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objAreaRequestListener = (AreaRequestListener) targetFragment;
                objAreaRequestListener.AreaResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }
    //endregion

    public interface AreaRequestListener {
        void AreaResponse(ArrayList<SpinnerItem> alCityMaster);
    }

//    public ArrayList<SpinnerItem> SelectAllAreaMasterAreaByCity(JSONObject jsonResponse) {
//        ArrayList<SpinnerItem> lstSpinnerItem = null;
//        try {
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllAreaMasterByCity + "Result");
//                if (jsonArray != null) {
//                    lstSpinnerItem = new ArrayList<>();
//                    SpinnerItem objSpinnerItem;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        objSpinnerItem = new SpinnerItem();
//                        objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("AreaName"));
//                        objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("AreaMasterId"));
//                        lstSpinnerItem.add(objSpinnerItem);
//                    }
//                }
//            }
//            return lstSpinnerItem;
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
    //endregion

}
