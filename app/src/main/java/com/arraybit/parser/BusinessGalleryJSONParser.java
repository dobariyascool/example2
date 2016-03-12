package com.arraybit.parser;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BusinessGalleryJSONParser {

    public String SelectAllBusinessGalleryTran = "SelectAllBusinessGalleryTranPageWise";
    BusinessGalleryRequestListener objBusinessGalleryRequestListener;

    private BusinessGalleryTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessGalleryTran objBusinessGalleryTran = null;
        try {
            if (jsonObject != null) {
                objBusinessGalleryTran = new BusinessGalleryTran();
                objBusinessGalleryTran.setBusinessGalleryTranId(jsonObject.getInt("BusinessGalleryTranId"));
                objBusinessGalleryTran.setImageTitle(jsonObject.getString("ImageTitle"));
                objBusinessGalleryTran.setXSImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objBusinessGalleryTran.setSMImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objBusinessGalleryTran.setMDImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objBusinessGalleryTran.setLGImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objBusinessGalleryTran.setXLImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                objBusinessGalleryTran.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objBusinessGalleryTran.setSortOrder((short) jsonObject.getInt("SortOrder"));
                }

                /// Extra
                objBusinessGalleryTran.setBusiness(jsonObject.getString("Business"));
            }
            return objBusinessGalleryTran;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<BusinessGalleryTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessGalleryTran> lstBusinessGalleryTran = new ArrayList<>();
        BusinessGalleryTran objBusinessGalleryTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessGalleryTran = new BusinessGalleryTran();
                objBusinessGalleryTran.setBusinessGalleryTranId(jsonArray.getJSONObject(i).getInt("BusinessGalleryTranId"));
                objBusinessGalleryTran.setImageTitle(jsonArray.getJSONObject(i).getString("ImageTitle"));
                objBusinessGalleryTran.setXSImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objBusinessGalleryTran.setSMImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objBusinessGalleryTran.setMDImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objBusinessGalleryTran.setLGImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objBusinessGalleryTran.setXLImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
                objBusinessGalleryTran.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objBusinessGalleryTran.setSortOrder((short) jsonArray.getJSONObject(i).getInt("SortOrder"));
                }

                /// Extra
                objBusinessGalleryTran.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBusinessGalleryTran.add(objBusinessGalleryTran);
            }
            return lstBusinessGalleryTran;
        } catch (JSONException e) {
            return null;
        }
    }

    public void SelectAllBusinessGalleryTran(final Fragment targetFragment,Context context, String currentPage, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllBusinessGalleryTran + "/" +currentPage+"/"+ linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllBusinessGalleryTran + "Result");
                    if (jsonArray != null) {
                        ArrayList<BusinessGalleryTran> alBusinessGalleryTran = SetListPropertiesFromJSONArray(jsonArray);
                        objBusinessGalleryRequestListener = (BusinessGalleryRequestListener) targetFragment;
                        objBusinessGalleryRequestListener.BusinessGalleryResponse(alBusinessGalleryTran);
                    }
                } catch (Exception e) {
                    objBusinessGalleryRequestListener = (BusinessGalleryRequestListener) targetFragment;
                    objBusinessGalleryRequestListener.BusinessGalleryResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBusinessGalleryRequestListener = (BusinessGalleryRequestListener) targetFragment;
                objBusinessGalleryRequestListener.BusinessGalleryResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }


    public interface BusinessGalleryRequestListener {
        void BusinessGalleryResponse(ArrayList<BusinessGalleryTran> alBusinessGalleryTran);
    }

}
