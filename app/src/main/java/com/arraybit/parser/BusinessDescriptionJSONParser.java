package com.arraybit.parser;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessDescription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for BusinessDescription
/// </summary>
public class BusinessDescriptionJSONParser {
    public String SelectBusinessDescription = "SelectBusinessDescription";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    BusinessDescriptionRequestListener objBusinessDescriptionRequestListener;

    private BusinessDescription SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessDescription objBusinessDescription = null;
        try {
            if (jsonObject != null) {
                objBusinessDescription = new BusinessDescription();
                objBusinessDescription.setBusinessDescriptionId((short) jsonObject.getInt("BusinessDescriptionId"));
                objBusinessDescription.setKeyword(jsonObject.getString("Keyword"));
                objBusinessDescription.setDescription(jsonObject.getString("Description"));
                objBusinessDescription.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objBusinessDescription.setIsDefault(jsonObject.getBoolean("IsDefault"));
                /// Extra
                //objBusinessDescription.setBusiness(jsonObject.getString("Business"));
            }
            return objBusinessDescription;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessDescription> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessDescription> lstBusinessDescription = new ArrayList<>();
        BusinessDescription objBusinessDescription;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessDescription = new BusinessDescription();
                objBusinessDescription.setBusinessDescriptionId((short) jsonArray.getJSONObject(i).getInt("BusinessDescriptionId"));
                objBusinessDescription.setKeyword(jsonArray.getJSONObject(i).getString("Keyword"));
                objBusinessDescription.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objBusinessDescription.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objBusinessDescription.setIsDefault(jsonArray.getJSONObject(i).getBoolean("IsDefault"));
                objBusinessDescription.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                if (!jsonArray.getJSONObject(i).getString("CreateDateTime").equals("null")) {
                    objBusinessDescription.setUpdateDateTime(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBusinessDescription.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                if (!jsonArray.getJSONObject(i).getString("UpdateDateTime").equals("null")) {
                    objBusinessDescription.setUpdateDateTime(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                }
                /// Extra
                //objBusinessDescription.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBusinessDescription.add(objBusinessDescription);
            }
            return lstBusinessDescription;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void SelectBusinessDescription(final Context context, String businessMasterId, String keyword) {
        String url;
        try {

            url = Service.Url + this.SelectBusinessDescription + "/" + businessMasterId + "/" + URLEncoder.encode(keyword, "utf-8");

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectBusinessDescription + "Result");
                            if (jsonResponse != null) {
                                objBusinessDescriptionRequestListener = (BusinessDescriptionRequestListener) context;
                                objBusinessDescriptionRequestListener.BusinessDescriptionResponse(SetClassPropertiesFromJSONObject(jsonResponse));
                            }
                        }
                    } catch (Exception e) {
                        objBusinessDescriptionRequestListener = (BusinessDescriptionRequestListener) context;
                        objBusinessDescriptionRequestListener.BusinessDescriptionResponse(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objBusinessDescriptionRequestListener = (BusinessDescriptionRequestListener) context;
                    objBusinessDescriptionRequestListener.BusinessDescriptionResponse(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objBusinessDescriptionRequestListener = (BusinessDescriptionRequestListener) context;
            objBusinessDescriptionRequestListener.BusinessDescriptionResponse(null);
        }

    }

    public interface BusinessDescriptionRequestListener {
        void BusinessDescriptionResponse(BusinessDescription objBusinessDescription);
    }

}
