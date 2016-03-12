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
import com.arraybit.modal.BusinessHoursTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusinessHoursJSONParser {

    public String SelectAllBusinessHoursTran = "SelectAllBusinessHoursTranByBusinessMasterId";

    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    BusinessHoursRequestListener objBusinessHoursRequestListener;

    private BusinessHoursTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessHoursTran objBusinessHoursTran = null;
        try {
            if (jsonObject != null) {
                objBusinessHoursTran = new BusinessHoursTran();
                objBusinessHoursTran.setBusinessHoursTranId((short) jsonObject.getInt("BusinessHoursTranId"));
                objBusinessHoursTran.setDayOfWeek((short) jsonObject.getInt("DayOfWeek"));
                dt = sdfTimeFormat.parse(jsonObject.getString("OpeningTime"));
                objBusinessHoursTran.setOpeningTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("ClosingTime"));
                objBusinessHoursTran.setClosingTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("BreakStartTime"));
                objBusinessHoursTran.setBreakStartTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("BreakEndTime"));
                objBusinessHoursTran.setBreakEndTime(sdfControlTimeFormat.format(dt));
                objBusinessHoursTran.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessHoursTran.setBusiness(jsonObject.getString("Business"));
            }
            return objBusinessHoursTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BusinessHoursTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessHoursTran> lstBusinessHoursTran = new ArrayList<>();
        BusinessHoursTran objBusinessHoursTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessHoursTran = new BusinessHoursTran();
                objBusinessHoursTran.setBusinessHoursTranId((short) jsonArray.getJSONObject(i).getInt("BusinessHoursTranId"));
                objBusinessHoursTran.setDayOfWeek((short) jsonArray.getJSONObject(i).getInt("DayOfWeek"));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("OpeningTime"));
                objBusinessHoursTran.setOpeningTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ClosingTime"));
                objBusinessHoursTran.setClosingTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("BreakStartTime"));
                objBusinessHoursTran.setBreakStartTime(sdfControlTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("BreakEndTime"));
                objBusinessHoursTran.setBreakEndTime(sdfControlTimeFormat.format(dt));
                objBusinessHoursTran.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessHoursTran.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBusinessHoursTran.add(objBusinessHoursTran);
            }
            return lstBusinessHoursTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void SelectAllBusinessHours(final Fragment targetFragment,Context context, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllBusinessHoursTran + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllBusinessHoursTran + "Result");
                    if (jsonArray != null) {
                        ArrayList<BusinessHoursTran> alBusinessHoursTran = SetListPropertiesFromJSONArray(jsonArray);
                        objBusinessHoursRequestListener = (BusinessHoursRequestListener) targetFragment;
                        objBusinessHoursRequestListener.BusinessHoursResponse(alBusinessHoursTran);
                    }
                } catch (Exception e) {
                    objBusinessHoursRequestListener = (BusinessHoursRequestListener) targetFragment;
                    objBusinessHoursRequestListener.BusinessHoursResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBusinessHoursRequestListener = (BusinessHoursRequestListener) targetFragment;
                objBusinessHoursRequestListener.BusinessHoursResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public interface BusinessHoursRequestListener {
        void BusinessHoursResponse(ArrayList<BusinessHoursTran> alBusinessHoursTran);
    }
}
