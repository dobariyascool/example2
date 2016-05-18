package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessServiceTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessServiceJSONParser {

    public String SelectAllBusinessServiceTran = "SelectAllBusinessService";
    BusinessServiceRequestListener objBusinessServiceRequestListener;

    private BusinessServiceTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessServiceTran objBusinessServiceTran = null;
        try {
            if (jsonObject != null) {
                objBusinessServiceTran = new BusinessServiceTran();
                objBusinessServiceTran.setBusinessServiceTran((short) jsonObject.getInt("BusinessServiceTranId"));
                objBusinessServiceTran.setlinktoServiceMasterId((short) jsonObject.getInt("linktoServiceMasterId"));
                objBusinessServiceTran.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessServiceTran.setService(jsonObject.getString("ServiceName"));
                objBusinessServiceTran.setImageName(jsonObject.getString("ImageName"));
                objBusinessServiceTran.setXSImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
            }
            return objBusinessServiceTran;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessServiceTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessServiceTran> lstBusinessServiceTran = new ArrayList<>();
        BusinessServiceTran objBusinessServiceTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessServiceTran = new BusinessServiceTran();
                objBusinessServiceTran.setBusinessServiceTran((short) jsonArray.getJSONObject(i).getInt("BusinessServiceTranId"));
                objBusinessServiceTran.setlinktoServiceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoServiceMasterId"));
                objBusinessServiceTran.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objBusinessServiceTran.setService(jsonArray.getJSONObject(i).getString("ServiceName"));
                objBusinessServiceTran.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objBusinessServiceTran.setXSImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objBusinessServiceTran.setIsSelected(jsonArray.getJSONObject(i).getBoolean("IsSelected"));
                lstBusinessServiceTran.add(objBusinessServiceTran);
            }
            return lstBusinessServiceTran;
        } catch (JSONException e) {
            return null;
        }
    }

    public void SelectAllBusinessService(final Context context, final Fragment targetFragment, String linktoBusinessTypeMasterId, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllBusinessServiceTran + "/" + linktoBusinessTypeMasterId + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllBusinessServiceTran + "Result");
                    if (jsonArray != null) {
                        ArrayList<BusinessServiceTran> alBusinessServiceTran = SetListPropertiesFromJSONArray(jsonArray);
                        objBusinessServiceRequestListener = (BusinessServiceRequestListener) targetFragment;
                        objBusinessServiceRequestListener.BusinessServiceResponse(alBusinessServiceTran);
                    }
                } catch (Exception e) {
                    objBusinessServiceRequestListener = (BusinessServiceRequestListener) targetFragment;
                    objBusinessServiceRequestListener.BusinessServiceResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBusinessServiceRequestListener = (BusinessServiceRequestListener) targetFragment;
                objBusinessServiceRequestListener.BusinessServiceResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface BusinessServiceRequestListener {
        void BusinessServiceResponse(ArrayList<BusinessServiceTran> alBusinessServiceTran);
    }
}

