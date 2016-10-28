package com.arraybit.parser;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.NotificationMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for NotificationMaster
/// </summary>
public class NotificationJSONParser {
    public String SelectAllNotificationMaster = "SelectAllNotificationMasterPageWise";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    NotificationRequestListener objNotificationRequestListener;

    private NotificationMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        NotificationMaster objNotificationMaster = null;
        try {
            if (jsonObject != null) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonObject.getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonObject.getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonObject.getString("NotificationText"));
                if (jsonObject.getString("NotificationImageName") != null && !jsonObject.getString("NotificationImageName").equals("") && !jsonObject.getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonObject.getString("NotificationImageName"));
                }
                if (!jsonObject.getString("Type").equals("null")) {
                    objNotificationMaster.setType((short)jsonObject.getInt("Type"));
                }
                if (!jsonObject.getString("ID").equals("null")) {
                    objNotificationMaster.setID(jsonObject.getInt("ID"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("NotificationDateTime"));
                objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
            }
            return objNotificationMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<NotificationMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<NotificationMaster> lstNotificationMaster = new ArrayList<>();
        NotificationMaster objNotificationMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objNotificationMaster = new NotificationMaster();
                objNotificationMaster.setNotificationMasterId(jsonArray.getJSONObject(i).getInt("NotificationMasterId"));
                objNotificationMaster.setNotificationTitle(jsonArray.getJSONObject(i).getString("NotificationTitle"));
                objNotificationMaster.setNotificationText(jsonArray.getJSONObject(i).getString("NotificationText"));
                if (jsonArray.getJSONObject(i).getString("NotificationImageName") != null && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("") && !jsonArray.getJSONObject(i).getString("NotificationImageName").equals("null")) {
                    objNotificationMaster.setNotificationImageName(jsonArray.getJSONObject(i).getString("NotificationImageName"));
                }
                if (!jsonArray.getJSONObject(i).getString("Type").equals("null")) {
                    objNotificationMaster.setType((short)jsonArray.getJSONObject(i).getInt("Type"));
                }
                if (!jsonArray.getJSONObject(i).getString("ID").equals("null")) {
                    objNotificationMaster.setID(jsonArray.getJSONObject(i).getInt("ID"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("NotificationDateTime"));
                objNotificationMaster.setNotificationDateTime(sdfControlDateFormat.format(dt));
                objNotificationMaster.setIsRead((short) jsonArray.getJSONObject(i).getInt("IsRead"));
                lstNotificationMaster.add(objNotificationMaster);
            }
            return lstNotificationMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void SelectAllNotificationMasterPageWise(String currentPage, final Context context, String customerMasterId) {
        String url = Service.Url + this.SelectAllNotificationMaster + "/" + currentPage + "/" + Globals.linktoBusinessMasterId+"/"+customerMasterId;
        Log.e("url", " " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("json", " " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray(SelectAllNotificationMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<NotificationMaster> notificationMasters = SetListPropertiesFromJSONArray(jsonArray);
                        objNotificationRequestListener = (NotificationRequestListener) context;
                        objNotificationRequestListener.NotificationResponse(notificationMasters, null);
                    }
                } catch (Exception e) {
                    objNotificationRequestListener = (NotificationRequestListener) context;
                    objNotificationRequestListener.NotificationResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objNotificationRequestListener = (NotificationRequestListener) context;
                objNotificationRequestListener.NotificationResponse(null, null);
            }

        });
        queue.add(jsonObjectRequest);

    }

    //region interface
    public interface NotificationRequestListener {
        void NotificationResponse(ArrayList<NotificationMaster> alNotificationMasters, NotificationMaster objNotificationMaster);
    }
    //endregion
}
