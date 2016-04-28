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
import com.arraybit.modal.OfferMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfferJSONParser {

    public String SelectOfferMaster = "SelectOfferMaster";
    public String SelectAllOfferMasterByFromDate = "SelectAllOfferMasterPageWise";
    public String SelectOfferCodeVerification = "SelectOfferMasterOfferCodeVerification";

    public SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    public Date dt = null;
    public OfferRequestListener objOfferRequestListener;
    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.TimeFormat, Locale.US);
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    //region Class Methods
    private OfferMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OfferMaster objOfferMaster = null;
        try {
            if (jsonObject != null) {
                objOfferMaster = new OfferMaster();
                objOfferMaster.setOfferMasterId(jsonObject.getInt("OfferMasterId"));
                objOfferMaster.setlinktoOfferTypeMasterId((short) jsonObject.getInt("linktoOfferTypeMasterId"));
                objOfferMaster.setOfferTitle(jsonObject.getString("OfferTitle"));
                objOfferMaster.setOfferContent(jsonObject.getString("OfferContent"));
                if (!jsonObject.getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonObject.getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonObject.getString("ToTime"));
                    objOfferMaster.setToTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonObject.getString("MinimumBillAmount").equals("null")) {
                    objOfferMaster.setMinimumBillAmount(jsonObject.getDouble("MinimumBillAmount"));
                }
                objOfferMaster.setDiscount(jsonObject.getDouble("Discount"));
                objOfferMaster.setIsDiscountPercentage(jsonObject.getBoolean("IsDiscountPercentage"));
                if (!jsonObject.getString("RedeemCount").equals("null")) {
                    objOfferMaster.setRedeemCount(jsonObject.getInt("RedeemCount"));
                }
                objOfferMaster.setOfferCode(jsonObject.getString("OfferCode"));
                objOfferMaster.setImagePhysicalName(jsonObject.getString("ImagePhysicalName"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objOfferMaster.setTermsAndConditions(jsonObject.getString("TermsAndConditions"));
                objOfferMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objOfferMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objOfferMaster.setIsForCustomers(jsonObject.getBoolean("IsForCustomers"));
                if (!jsonObject.getString("BuyItemCount").equals("null")) {
                    objOfferMaster.setBuyItemCount(jsonObject.getInt("BuyItemCount"));
                }
                if (!jsonObject.getString("GetItemCount").equals("null")) {
                    objOfferMaster.setGetItemCount(jsonObject.getInt("GetItemCount"));
                }
            }
            return objOfferMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OfferMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OfferMaster> lstOfferMaster = new ArrayList<>();
        OfferMaster objOfferMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOfferMaster = new OfferMaster();
                objOfferMaster.setOfferMasterId(jsonArray.getJSONObject(i).getInt("OfferMasterId"));
                objOfferMaster.setlinktoOfferTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOfferTypeMasterId"));
                objOfferMaster.setOfferTitle(jsonArray.getJSONObject(i).getString("OfferTitle"));
                objOfferMaster.setOfferContent(jsonArray.getJSONObject(i).getString("OfferContent"));
                if (!jsonArray.getJSONObject(i).getString("FromDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("FromDate"));
                    objOfferMaster.setFromDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("ToDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("ToDate"));
                    objOfferMaster.setToDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("FromTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("FromTime"));
                    objOfferMaster.setFromTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("ToTime").equals("null")) {
                    dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ToTime"));
                    objOfferMaster.setToTime(sdfControlTimeFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("MinimumBillAmount").equals("null")) {
                    objOfferMaster.setMinimumBillAmount(jsonArray.getJSONObject(i).getDouble("MinimumBillAmount"));
                }
                objOfferMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOfferMaster.setIsDiscountPercentage(jsonArray.getJSONObject(i).getBoolean("IsDiscountPercentage"));
                if (!jsonArray.getJSONObject(i).getString("RedeemCount").equals("null")) {
                    objOfferMaster.setRedeemCount(jsonArray.getJSONObject(i).getInt("RedeemCount"));
                }
                objOfferMaster.setOfferCode(jsonArray.getJSONObject(i).getString("OfferCode"));
                objOfferMaster.setImagePhysicalName(jsonArray.getJSONObject(i).getString("ImagePhysicalName"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOfferMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objOfferMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objOfferMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objOfferMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objOfferMaster.setTermsAndConditions(jsonArray.getJSONObject(i).getString("TermsAndConditions"));
                objOfferMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objOfferMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objOfferMaster.setIsForCustomers(jsonArray.getJSONObject(i).getBoolean("IsForCustomers"));
                if (!jsonArray.getJSONObject(i).getString("BuyItemCount").equals("null")) {
                    objOfferMaster.setBuyItemCount(jsonArray.getJSONObject(i).getInt("BuyItemCount"));
                }
                if (!jsonArray.getJSONObject(i).getString("GetItemCount").equals("null")) {
                    objOfferMaster.setGetItemCount(jsonArray.getJSONObject(i).getInt("GetItemCount"));
                }
                lstOfferMaster.add(objOfferMaster);
            }
            return lstOfferMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //region SelectAll
    public void SelectAllOfferMasterByFromDate(String currentPage, String linktoBusinessMasterId, final Context context) {
        String url = Service.Url + this.SelectAllOfferMasterByFromDate + "/" + currentPage + "/" + linktoBusinessMasterId + "/" +
                sdfControlDateFormat.format(new Date()) + "/" + Globals.GetCurrentTime();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllOfferMasterByFromDate + "Result");
                    if (jsonArray != null) {
                        ArrayList<OfferMaster> offerMasters = SetListPropertiesFromJSONArray(jsonArray);
                        objOfferRequestListener = (OfferRequestListener) context;
                        objOfferRequestListener.OfferResponse(offerMasters, null);
                    }
                } catch (Exception e) {
                    objOfferRequestListener = (OfferRequestListener) context;
                    objOfferRequestListener.OfferResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objOfferRequestListener = (OfferRequestListener) context;
                objOfferRequestListener.OfferResponse(null, null);
            }

        });
        queue.add(jsonObjectRequest);
    }
    //endregion

    public void SelectOfferCodeVerification(final Context context,OfferMaster objOfferMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("objOfferMaster");
            stringer.object();

            stringer.key("OfferCode").value(objOfferMaster.getOfferCode());
            stringer.key("MinimumBillAmount").value(objOfferMaster.getMinimumBillAmount());
            stringer.key("linktoBusinessMasterId").value(objOfferMaster.getlinktoBusinessMasterId());
            stringer.key("linktoCustomerMasterId").value(objOfferMaster.getLinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterIds").value(objOfferMaster.getlinktoOrderTypeMasterId());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.SelectOfferCodeVerification;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    JSONObject jObject = null;
                    try {
                        jObject = jsonObject.getJSONObject(SelectOfferCodeVerification + "Result");
                        if (jObject != null) {
                            OfferMaster offerMaster= SetClassPropertiesFromJSONObject(jObject);
                            objOfferRequestListener = (OfferRequestListener) context;
                            objOfferRequestListener.OfferResponse(null, offerMaster);
                        }
                    } catch (Exception e) {
                        objOfferRequestListener = (OfferRequestListener) context;
                        objOfferRequestListener.OfferResponse(null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objOfferRequestListener = (OfferRequestListener) context;
                    objOfferRequestListener.OfferResponse(null, null);
                }

            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objOfferRequestListener = (OfferRequestListener) context;
            objOfferRequestListener.OfferResponse(null, null);
        }
    }


    public interface OfferRequestListener {
        void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster);
    }
//    public ArrayList<OfferMaster> SelectAllOfferMasterByFromDate(JSONObject jsonResponse) {
//        ArrayList<OfferMaster> lstOfferMaster = null;
//        try {
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllOfferMasterByFromDate + "Result");
//                if (jsonArray != null) {
//                    lstOfferMaster = SetListPropertiesFromJSONArray(jsonArray);
//                }
//            }
//            return lstOfferMaster;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
    //endregion

}

