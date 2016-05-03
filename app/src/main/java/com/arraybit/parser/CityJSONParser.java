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
import com.arraybit.modal.CityMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityJSONParser {

    public String SelectAllCityMasterByState = "SelectAllCityMasterByState";
    CityRequestListener objCityRequestListener;

    //region Class Methods
    private CityMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CityMaster objCityMaster = null;
        try {
            if (jsonObject != null) {
                objCityMaster = new CityMaster();
                objCityMaster.setCityMasterId((short) jsonObject.getInt("CityMasterId"));
                objCityMaster.setCityName(jsonObject.getString("CityName"));
                objCityMaster.setCityCode(jsonObject.getString("CityCode"));
                objCityMaster.setlinktoStateMasterId((short) jsonObject.getInt("linktoStateMasterId"));
                objCityMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objCityMaster.setState(jsonObject.getString("State"));
            }
            return objCityMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<CityMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CityMaster> lstCityMaster = new ArrayList<>();
        CityMaster objCityMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCityMaster = new CityMaster();
                objCityMaster.setCityMasterId((short) jsonArray.getJSONObject(i).getInt("CityMasterId"));
                objCityMaster.setCityName(jsonArray.getJSONObject(i).getString("CityName"));
                objCityMaster.setCityCode(jsonArray.getJSONObject(i).getString("CityCode"));
                objCityMaster.setlinktoStateMasterId((short) jsonArray.getJSONObject(i).getInt("linktoStateMasterId"));
                objCityMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objCityMaster.setState(jsonArray.getJSONObject(i).getString("State"));
                lstCityMaster.add(objCityMaster);
            }
            return lstCityMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //region SelectAll
    public void SelectAllCityMasterByState(final Fragment targetFragment, final Context context, String linktoStateMasterId) {
        String url = Service.Url + this.SelectAllCityMasterByState + "/" + linktoStateMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                ArrayList<SpinnerItem> lstSpinnerItem = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllCityMasterByState + "Result");
                    if (jsonArray != null) {
                        lstSpinnerItem = new ArrayList<>();
                        SpinnerItem objSpinnerItem;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objSpinnerItem = new SpinnerItem();
                            objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("CityName"));
                            objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("CityMasterId"));
                            lstSpinnerItem.add(objSpinnerItem);
                        }

                    }
                    if(targetFragment==null){
                        objCityRequestListener = (CityRequestListener) context;
                        objCityRequestListener.CityResponse(lstSpinnerItem);
                    }else{
                        objCityRequestListener = (CityRequestListener) targetFragment;
                        objCityRequestListener.CityResponse(lstSpinnerItem);
                    }

                } catch (Exception e) {
                    if(targetFragment==null){
                        objCityRequestListener = (CityRequestListener) context;
                        objCityRequestListener.CityResponse(null);
                    }else{
                        objCityRequestListener = (CityRequestListener) targetFragment;
                        objCityRequestListener.CityResponse(null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(targetFragment==null){
                    objCityRequestListener = (CityRequestListener) context;
                    objCityRequestListener.CityResponse(null);
                }else{
                    objCityRequestListener = (CityRequestListener) targetFragment;
                    objCityRequestListener.CityResponse(null);
                }

            }
        });
        queue.add(jsonObjectRequest);
    }
    //endregion

    public interface CityRequestListener {
        void CityResponse(ArrayList<SpinnerItem> alCityMaster);
    }
//    public ArrayList<SpinnerItem> SelectAllCityMasterByState(JSONObject jsonResponse) {
//        ArrayList<SpinnerItem> lstSpinnerItem = null;
//        try {
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllCityMasterByState + "Result");
//                if (jsonArray != null) {
//                    lstSpinnerItem = new ArrayList<>();
//                    SpinnerItem objSpinnerItem;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        objSpinnerItem = new SpinnerItem();
//                        objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("CityName"));
//                        objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("CityMasterId"));
//                        lstSpinnerItem.add(objSpinnerItem);
//                    }
//                }
//            }
//            return lstSpinnerItem;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
    //endregion

}


