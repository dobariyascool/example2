package com.arraybit.parser;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CustomerMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerJSONParser {


    public String InsertCustomerMaster = "InsertCustomerMaster";
    public String UpdateCustomerMasterPassword = "UpdateCustomerMasterPassword";
    public String UpdateCustomerMaster = "UpdateCustomerMaster";
    public String SelectCustomerMaster = "SelectCustomerMaster";
    public String UpdateCustomerMasterGCMToken = "UpdateCustomerMasterGCMToken";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    CustomerRequestListener objCustomerRequestListener;
    CustomerGCMListener objCustomerGCMListener;

    //region Class Method
    private CustomerMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CustomerMaster objCustomerMaster = null;
        try {
            if (jsonObject != null) {
                objCustomerMaster = new CustomerMaster();
                objCustomerMaster.setCustomerMasterId(jsonObject.getInt("CustomerMasterId"));
                objCustomerMaster.setShortName(jsonObject.getString("ShortName"));
                objCustomerMaster.setCustomerName(jsonObject.getString("CustomerName"));
                objCustomerMaster.setDescription(jsonObject.getString("Description"));
                objCustomerMaster.setContactPersonName(jsonObject.getString("ContactPersonName"));
                objCustomerMaster.setDesignation(jsonObject.getString("Designation"));
                objCustomerMaster.setPhone1(jsonObject.getString("Phone1"));
                objCustomerMaster.setIsPhone1DND(jsonObject.getBoolean("IsPhone1DND"));
                objCustomerMaster.setPhone2(jsonObject.getString("Phone2"));
                if (!jsonObject.getString("IsPhone2DND").equals("null")) {
                    objCustomerMaster.setIsPhone2DND(jsonObject.getBoolean("IsPhone2DND"));
                }
                objCustomerMaster.setEmail1(jsonObject.getString("Email1"));
                objCustomerMaster.setEmail2(jsonObject.getString("Email2"));
                objCustomerMaster.setFax(jsonObject.getString("Fax"));
                if(!jsonObject.getString("ImageName").equals("null") && !jsonObject.getString("ImageName").equals("")) {
                    objCustomerMaster.setImageName(jsonObject.getString("ImageName"));
                    objCustomerMaster.setXs_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                    objCustomerMaster.setSm_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                    objCustomerMaster.setMd_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                    objCustomerMaster.setLg_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                    objCustomerMaster.setXl_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                }
                if (!jsonObject.getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                    objCustomerMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonObject.getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonObject.getString("AnniversaryDate"));
                    objCustomerMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                objCustomerMaster.setCustomerType((short) jsonObject.getInt("CustomerType"));
                if (!jsonObject.getString("IsFavourite").equals("null")) {
                    objCustomerMaster.setIsFavourite(jsonObject.getBoolean("IsFavourite"));
                }
                if (!jsonObject.getString("IsCredit").equals("null")) {
                    objCustomerMaster.setIsCredit(jsonObject.getBoolean("IsCredit"));
                }
                objCustomerMaster.setOpeningBalance(jsonObject.getDouble("OpeningBalance"));
                objCustomerMaster.setCreditDays((short) jsonObject.getInt("CreditDays"));
                objCustomerMaster.setCreditBalance(jsonObject.getDouble("CreditBalance"));
                objCustomerMaster.setCreditLimit(jsonObject.getDouble("CreditLimit"));
                objCustomerMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objCustomerMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCustomerMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objCustomerMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objCustomerMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objCustomerMaster.setGender(jsonObject.getString("Gender"));
                objCustomerMaster.setPassword(jsonObject.getString("Password"));
                objCustomerMaster.setlinktoSourceMasterId((short) jsonObject.getInt("linktoSourceMasterId"));
                if(!jsonObject.getString("GooglePlusUserId").equals("null")) {
                    objCustomerMaster.setGooglePlusUserId(jsonObject.getString("GooglePlusUserId"));
                } if(!jsonObject.getString("FacebookUserId").equals("null")) {
                    objCustomerMaster.setFacebookUserId(jsonObject.getString("FacebookUserId"));
                } if(!jsonObject.getString("GCMToken").equals("null")) {
                    objCustomerMaster.setFacebookUserId(jsonObject.getString("GCMToken"));
                }
                objCustomerMaster.setAgeMinRange(jsonObject.getInt("AgeMinRange"));
                objCustomerMaster.setAgeMaxRange(jsonObject.getInt("AgeMaxRange"));
                objCustomerMaster.setIsVerified(jsonObject.getBoolean("IsVerified"));
                objCustomerMaster.setErrorCode(jsonObject.optString("ErrorCode"));
            }
            return objCustomerMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<CustomerMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CustomerMaster> lstCustomerMaster = new ArrayList<>();
        CustomerMaster objCustomerMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCustomerMaster = new CustomerMaster();
                objCustomerMaster.setCustomerMasterId(jsonArray.getJSONObject(i).getInt("CustomerMasterId"));
                objCustomerMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objCustomerMaster.setCustomerName(jsonArray.getJSONObject(i).getString("CustomerName"));
                objCustomerMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objCustomerMaster.setContactPersonName(jsonArray.getJSONObject(i).getString("ContactPersonName"));
                objCustomerMaster.setDesignation(jsonArray.getJSONObject(i).getString("Designation"));
                objCustomerMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                objCustomerMaster.setIsPhone1DND(jsonArray.getJSONObject(i).getBoolean("IsPhone1DND"));
                objCustomerMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                if (!jsonArray.getJSONObject(i).getString("IsPhone2DND").equals("null")) {
                    objCustomerMaster.setIsPhone2DND(jsonArray.getJSONObject(i).getBoolean("IsPhone2DND"));
                }
                objCustomerMaster.setEmail1(jsonArray.getJSONObject(i).getString("Email1"));
                objCustomerMaster.setEmail2(jsonArray.getJSONObject(i).getString("Email2"));
                objCustomerMaster.setFax(jsonArray.getJSONObject(i).getString("Fax"));
                objCustomerMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                if (!jsonArray.getJSONObject(i).getString("BirthDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                    objCustomerMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                if (!jsonArray.getJSONObject(i).getString("AnniversaryDate").equals("null")) {
                    dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("AnniversaryDate"));
                    objCustomerMaster.setAnniversaryDate(sdfControlDateFormat.format(dt));
                }
                objCustomerMaster.setCustomerType((short) jsonArray.getJSONObject(i).getInt("CustomerType"));
                if (!jsonArray.getJSONObject(i).getString("IsFavourite").equals("null")) {
                    objCustomerMaster.setIsFavourite(jsonArray.getJSONObject(i).getBoolean("IsFavourite"));
                }
                if (!jsonArray.getJSONObject(i).getString("IsCredit").equals("null")) {
                    objCustomerMaster.setIsCredit(jsonArray.getJSONObject(i).getBoolean("IsCredit"));
                }
                objCustomerMaster.setOpeningBalance(jsonArray.getJSONObject(i).getDouble("OpeningBalance"));
                objCustomerMaster.setCreditDays((short) jsonArray.getJSONObject(i).getInt("CreditDays"));
                objCustomerMaster.setCreditBalance(jsonArray.getJSONObject(i).getDouble("CreditBalance"));
                objCustomerMaster.setCreditLimit(jsonArray.getJSONObject(i).getDouble("CreditLimit"));
                objCustomerMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objCustomerMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCustomerMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objCustomerMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objCustomerMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objCustomerMaster.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                objCustomerMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objCustomerMaster.setlinktoSourceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoSourceMasterId"));
                if(!jsonArray.getJSONObject(i).getString("GooglePlusUserId").equals("null")) {
                    objCustomerMaster.setGooglePlusUserId(jsonArray.getJSONObject(i).getString("GooglePlusUserId"));
                } if(!jsonArray.getJSONObject(i).getString("FacebookUserId").equals("null")) {
                    objCustomerMaster.setFacebookUserId(jsonArray.getJSONObject(i).getString("FacebookUserId"));
                }if(!jsonArray.getJSONObject(i).getString("GCMToken").equals("null")) {
                    objCustomerMaster.setFacebookUserId(jsonArray.getJSONObject(i).getString("GCMToken"));
                }
                objCustomerMaster.setAgeMinRange(jsonArray.getJSONObject(i).getInt("AgeMinRange"));
                objCustomerMaster.setAgeMaxRange(jsonArray.getJSONObject(i).getInt("AgeMaxRange"));
                objCustomerMaster.setIsVerified(jsonArray.getJSONObject(i).getBoolean("IsVerified"));
                objCustomerMaster.setErrorCode(jsonArray.getJSONObject(i).optString("ErrorCode"));

                /// Extra
                lstCustomerMaster.add(objCustomerMaster);
            }
            return lstCustomerMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
    //endregion

    //region Insert
    public void InsertCustomerMaster(final CustomerMaster objCustomerMaster, final Context context) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerMaster");

            stringer.object();

            stringer.key("Email1").value(objCustomerMaster.getEmail1());
            stringer.key("Phone1").value(objCustomerMaster.getPhone1());
            stringer.key("Password").value(objCustomerMaster.getPassword());
            stringer.key("ShortName").value(objCustomerMaster.getCustomerName());
            stringer.key("CustomerName").value(objCustomerMaster.getCustomerName());
            stringer.key("Gender").value(objCustomerMaster.getGender());
            if (objCustomerMaster.getBirthDate() != null) {
                stringer.key("BirthDate").value(objCustomerMaster.getBirthDate());
            }
            if(objCustomerMaster.getLinktoCountryMasterId()!=0) {
                stringer.key("linktoCountryMasterId").value(objCustomerMaster.getLinktoCountryMasterId());
                stringer.key("linktoStateMasterId").value(objCustomerMaster.getLinktoStateMasterId());
                stringer.key("linktoCityMasterId").value(objCustomerMaster.getLinktoCityMasterId());
                stringer.key("linktoAreaMasterId").value(objCustomerMaster.getLinktoAreaMasterId());
            }
            stringer.key("linktoBusinessMasterId").value(objCustomerMaster.getlinktoBusinessMasterId());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoSourceMasterId").value(objCustomerMaster.getlinktoSourceMasterId());
            stringer.key("CustomerType").value(objCustomerMaster.getCustomerType());
            stringer.key("LastLoginDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("IsEnabled").value(objCustomerMaster.getIsEnabled());
            stringer.key("IsPrimary").value(true);
            stringer.key("ImageNamePhysicalNameBytes").value(objCustomerMaster.getImageNamePhysicalNameBytes());
            stringer.key("ImageName").value(objCustomerMaster.getImageName());
            if(objCustomerMaster.getGooglePlusUserId()!=null && !objCustomerMaster.getGooglePlusUserId().equals("")){
                stringer.key("GooglePlusUserId").value(objCustomerMaster.getGooglePlusUserId());
            }else if(objCustomerMaster.getFacebookUserId()!=null && !objCustomerMaster.getFacebookUserId().equals("")){
                stringer.key("FacebookUserId").value(objCustomerMaster.getFacebookUserId());
            }
            if(objCustomerMaster.getAgeMinRange() > 0){
                stringer.key("AgeMinRange").value(objCustomerMaster.getAgeMinRange());
            }
            if(objCustomerMaster.getAgeMaxRange() > 0){
                stringer.key("AgeMaxRange").value(objCustomerMaster.getAgeMinRange());
            }
            stringer.key("IsVerified").value(objCustomerMaster.getIsVerified());
            if(objCustomerMaster.getGCMToken()!=null && !objCustomerMaster.getGCMToken().equals("")){
                Log.e("JSON token:"," "+objCustomerMaster.getGCMToken());
                stringer.key("GCMToken").value(objCustomerMaster.getGCMToken());
            }
            stringer.endObject();
            stringer.endObject();

            String url = Service.Url + this.InsertCustomerMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertCustomerMaster + "Result");
                        if (jsonResponse != null) {
                            objCustomerRequestListener = (CustomerRequestListener) context;
                            objCustomerRequestListener.CustomerResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    } catch (JSONException e) {
                        objCustomerRequestListener = (CustomerRequestListener) context;
                        objCustomerRequestListener.CustomerResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerRequestListener = (CustomerRequestListener) context;
                    objCustomerRequestListener.CustomerResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objCustomerRequestListener = (CustomerRequestListener) context;
            objCustomerRequestListener.CustomerResponse("-1", null);
        }
    }
    //endregion

    //region Update
    public void UpdateCustomerMaster(CustomerMaster objCustomerMaster, final Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerMaster");
            stringer.object();

            stringer.key("CustomerMasterId").value(objCustomerMaster.getCustomerMasterId());
            stringer.key("Phone1").value(objCustomerMaster.getPhone1());
            stringer.key("CustomerName").value(objCustomerMaster.getCustomerName());
            stringer.key("Gender").value(objCustomerMaster.getGender());
            if (objCustomerMaster.getBirthDate() != null) {
                stringer.key("BirthDate").value(objCustomerMaster.getBirthDate());
            }
            if(objCustomerMaster.getImageNamePhysicalNameBytes()!=null) {
                stringer.key("ImageNamePhysicalNameBytes").value(objCustomerMaster.getImageNamePhysicalNameBytes());
            }
            stringer.key("ImageName").value(objCustomerMaster.getImageName());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            if(objCustomerMaster.getGooglePlusUserId()!=null && !objCustomerMaster.getGooglePlusUserId().equals("")){
                stringer.key("GooglePlusUserId").value(objCustomerMaster.getGooglePlusUserId());
            }else if(objCustomerMaster.getFacebookUserId()!=null && !objCustomerMaster.getFacebookUserId().equals("")){
                stringer.key("FacebookUserId").value(objCustomerMaster.getFacebookUserId());
            }
            //stringer.key("linktoUserMasterIdUpdatedBy").value(sdfDateTimeFormat.format(dt));
            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateCustomerMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateCustomerMaster + "Result");
                        if (jsonResponse != null) {
                            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                            objCustomerRequestListener.CustomerResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
                        }else {
                            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                            objCustomerRequestListener.CustomerResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                        objCustomerRequestListener.CustomerResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                    objCustomerRequestListener.CustomerResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
            objCustomerRequestListener.CustomerResponse("-1", null);
        }
    }

    public void UpdateCustomerMasterPassword(CustomerMaster objCustomerMaster, final Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerMaster");
            stringer.object();

            stringer.key("CustomerMasterId").value(objCustomerMaster.getCustomerMasterId());
            stringer.key("Password").value(objCustomerMaster.getPassword());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateCustomerMasterPassword;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateCustomerMasterPassword + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorNumber"));
                            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                            objCustomerRequestListener.CustomerResponse(errorCode, null);
                        } else {
                            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                            objCustomerRequestListener.CustomerResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                        objCustomerRequestListener.CustomerResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                    objCustomerRequestListener.CustomerResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);

        } catch (Exception ex) {
            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
            objCustomerRequestListener.CustomerResponse("-1", null);
        }
    }

    public void UpdateCustomerMasterGCM(final Context context, String token, String customerMasterId) {
        String url;
        try {
            if (!token.equals("")&& !customerMasterId.equals("0") && !customerMasterId.equals("")) {
                String token1=token.replace(":", "2E2").replace("-","3E3").replace("_","4E4");
                Log.e("token Encoded: "," "+token1);
                url = Service.Url + this.UpdateCustomerMasterGCMToken + "/" + customerMasterId + "/" + token1;
                Log.e("url"," "+url);
            } else {
                url = Service.Url + this.UpdateCustomerMasterGCMToken + "/" + 1 + "/" + null ;
                Log.e("url"," "+url);
            }
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e("jsonobject"," "+jsonObject.toString());
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(UpdateCustomerMasterGCMToken + "Result");
                            if (jsonResponse != null) {
                                    Log.e("json"," "+jsonResponse.toString());
                                objCustomerGCMListener = (CustomerGCMListener) context;
                                objCustomerGCMListener.CustomerGCMToken();
                            }
                        }
                        else
                        {
                            Log.e("jsonelse"," "+jsonObject.toString());
                        }
                    } catch (Exception e) {
                        objCustomerGCMListener = (CustomerGCMListener) context;
                        objCustomerGCMListener.CustomerGCMToken();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerGCMListener = (CustomerGCMListener) context;
                    objCustomerGCMListener.CustomerGCMToken();
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objCustomerGCMListener = (CustomerGCMListener) context;
            objCustomerGCMListener.CustomerGCMToken();
        }
    }
    //endregion

    //region Select
    public void SelectCustomerMaster(final Context context, String userName, String password, String customerMasterId, final Fragment targetFragment, String businessMasterId) {
        String url;
        try {
            if (userName != null && password != null) {
                url = Service.Url + this.SelectCustomerMaster + "/" + URLEncoder.encode(userName, "utf-8").replace(".", "2E") + "/" + URLEncoder.encode(password, "utf-8").replace(".", "2E") + "/" + customerMasterId + "/" + businessMasterId;
            } else {
                url = Service.Url + this.SelectCustomerMaster + "/" + null + "/" + null + "/" + customerMasterId + "/" + businessMasterId;
            }
            final RequestQueue queue = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectCustomerMaster + "Result");
                            if (jsonResponse != null) {
                                if (targetFragment == null) {
                                    objCustomerRequestListener = (CustomerRequestListener) context;
                                    objCustomerRequestListener.CustomerResponse(null, SetClassPropertiesFromJSONObject(jsonResponse));
                                } else {
                                    objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                                    objCustomerRequestListener.CustomerResponse(null, SetClassPropertiesFromJSONObject(jsonResponse));
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (targetFragment == null) {
                            objCustomerRequestListener = (CustomerRequestListener) context;
                            objCustomerRequestListener.CustomerResponse(null, null);
                        } else {
                            objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                            objCustomerRequestListener.CustomerResponse(null, null);
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (targetFragment == null) {
                        objCustomerRequestListener = (CustomerRequestListener) context;
                        objCustomerRequestListener.CustomerResponse(null, null);
                    } else {
                        objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                        objCustomerRequestListener.CustomerResponse(null, null);
                    }
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            if (targetFragment == null) {
                objCustomerRequestListener = (CustomerRequestListener) context;
                objCustomerRequestListener.CustomerResponse(null, null);
            } else {
                objCustomerRequestListener = (CustomerRequestListener) targetFragment;
                objCustomerRequestListener.CustomerResponse(null, null);
            }
        }

    }
    //endregion

    public interface CustomerRequestListener {
        void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster);
    }

    public interface CustomerGCMListener{
        void CustomerGCMToken();
    }
}

