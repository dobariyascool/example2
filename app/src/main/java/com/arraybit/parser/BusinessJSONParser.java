package com.arraybit.parser;

import android.content.Context;
import android.net.ParseException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.ContactUsMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusinessJSONParser {

    public String SelectBusinessMasterByBusinessMasterId = "SelectBusinessMasterByBusinessMasterId";
    public String SelectAllBusinessMasterByBusinessGroup = "SelectAllBusinessMasterByBusinessGroup";
    public String SelectAllBusinessMasterBusinessName = "SelectAllBusinessMasterBusinessName";
    public String InsertContactUsMaster = "InsertContactUsMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    BusinessRequestListener objBusinessRequestListener;

    private BusinessMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessMaster objBusinessMaster = null;
        try {
            if (jsonObject != null) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short) jsonObject.getInt("BusinessMasterId"));
                objBusinessMaster.setLinktoBusinessGroupMasterId((short) jsonObject.getInt("linktoBusinessGroupMasterId"));
                objBusinessMaster.setBusinessName(jsonObject.getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonObject.getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonObject.getString("Address"));
                objBusinessMaster.setPhone1(jsonObject.getString("Phone1"));
                objBusinessMaster.setPhone2(jsonObject.getString("Phone2"));
                objBusinessMaster.setEmail(jsonObject.getString("Email"));
                objBusinessMaster.setFax(jsonObject.getString("Fax"));
                objBusinessMaster.setWebsite(jsonObject.getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short) jsonObject.getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short) jsonObject.getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonObject.getString("City"));
                objBusinessMaster.setZipCode(jsonObject.getString("ZipCode"));
                objBusinessMaster.setXSImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objBusinessMaster.setSMImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objBusinessMaster.setLGImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objBusinessMaster.setMDImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objBusinessMaster.setXLImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                objBusinessMaster.setExtraText(jsonObject.getString("ExtraText"));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short) jsonObject.getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonObject.getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonObject.getString("Country"));
                objBusinessMaster.setState(jsonObject.getString("State"));
                objBusinessMaster.setBusinessType(jsonObject.getString("BusinessType"));
                objBusinessMaster.setBusinessGroup(jsonObject.getString("BusinessGroup"));
            }
            return objBusinessMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BusinessMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessMaster> lstBusinessMaster = new ArrayList<>();
        BusinessMaster objBusinessMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessMaster = new BusinessMaster();
                objBusinessMaster.setBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("BusinessMasterId"));
                objBusinessMaster.setLinktoBusinessGroupMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessGroupMasterId"));
                objBusinessMaster.setBusinessName(jsonArray.getJSONObject(i).getString("BusinessName"));
                objBusinessMaster.setBusinessShortName(jsonArray.getJSONObject(i).getString("BusinessShortName"));
                objBusinessMaster.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                objBusinessMaster.setPhone1(jsonArray.getJSONObject(i).getString("Phone1"));
                objBusinessMaster.setPhone2(jsonArray.getJSONObject(i).getString("Phone2"));
                objBusinessMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objBusinessMaster.setFax(jsonArray.getJSONObject(i).getString("Fax"));
                objBusinessMaster.setWebsite(jsonArray.getJSONObject(i).getString("Website"));
                objBusinessMaster.setlinktoCountryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCountryMasterId"));
                objBusinessMaster.setlinktoStateMasterId((short) jsonArray.getJSONObject(i).getInt("linktoStateMasterId"));
                objBusinessMaster.setCity(jsonArray.getJSONObject(i).getString("City"));
                objBusinessMaster.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                objBusinessMaster.setXSImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objBusinessMaster.setSMImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objBusinessMaster.setLGImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objBusinessMaster.setMDImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objBusinessMaster.setXLImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
                objBusinessMaster.setExtraText(jsonArray.getJSONObject(i).getString("ExtraText"));
                objBusinessMaster.setlinktoBusinessTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessTypeMasterId"));
                objBusinessMaster.setUniqueId(jsonArray.getJSONObject(i).getString("UniqueId"));
                objBusinessMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objBusinessMaster.setCountry(jsonArray.getJSONObject(i).getString("Country"));
                objBusinessMaster.setState(jsonArray.getJSONObject(i).getString("State"));
                objBusinessMaster.setBusinessType(jsonArray.getJSONObject(i).getString("BusinessType"));
                objBusinessMaster.setBusinessGroup(jsonArray.getJSONObject(i).getString("BusinessGroup"));
                lstBusinessMaster.add(objBusinessMaster);
            }
            return lstBusinessMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void SelectBusinessMaster(final Context context, String linktoBusinessMasterId) {
        try {
            String url = Service.Url + this.SelectBusinessMasterByBusinessMasterId + "/" + linktoBusinessMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            JSONObject jsonResponse = jsonObject.getJSONObject(SelectBusinessMasterByBusinessMasterId + "Result");
                            if (jsonResponse != null) {
                                objBusinessRequestListener = (BusinessRequestListener) context;
                                objBusinessRequestListener.BusinessResponse(null, SetClassPropertiesFromJSONObject(jsonResponse), null);
                            }
                        }
                    } catch (Exception e) {
                        objBusinessRequestListener = (BusinessRequestListener) context;
                        objBusinessRequestListener.BusinessResponse(null, null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objBusinessRequestListener = (BusinessRequestListener) context;
                    objBusinessRequestListener.BusinessResponse(null, null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objBusinessRequestListener = (BusinessRequestListener) context;
            objBusinessRequestListener.BusinessResponse(null, null, null);
        }
    }

    public void SelectAllBusinessMasterByBusinessGroup(final Context context, String linktoBusinessGroupMasterId, String city) {
        try {
            String url;
            if(city==null){
                url = Service.Url + this.SelectAllBusinessMasterByBusinessGroup + "/" + linktoBusinessGroupMasterId + "/"+null;
            }else{
                url = Service.Url + this.SelectAllBusinessMasterByBusinessGroup + "/" + linktoBusinessGroupMasterId + "/"+ URLEncoder.encode(city,"UTF-8");
            }

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObject.getJSONArray(SelectAllBusinessMasterByBusinessGroup + "Result");
                        if (jsonArray != null) {
                            ArrayList<BusinessMaster> alBusinessMaster = SetListPropertiesFromJSONArray(jsonArray);
                            objBusinessRequestListener = (BusinessRequestListener) context;
                            objBusinessRequestListener.BusinessResponse(null, null, alBusinessMaster);

                        }
                    } catch (Exception e) {
                        objBusinessRequestListener = (BusinessRequestListener) context;
                        objBusinessRequestListener.BusinessResponse(null, null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objBusinessRequestListener = (BusinessRequestListener) context;
                    objBusinessRequestListener.BusinessResponse(null, null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objBusinessRequestListener = (BusinessRequestListener) context;
            objBusinessRequestListener.BusinessResponse(null, null, null);
        }
    }

    //region Insert ContactUs Activity
    public void InsertContactUsMaster(final Context context, ContactUsMaster objContactUsMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("contactUsMaster");

            stringer.object();

            stringer.key("Name").value(objContactUsMaster.getName());
            stringer.key("Email").value(objContactUsMaster.getEmail());
            stringer.key("Modile").value(objContactUsMaster.getMobile());
            stringer.key("Message").value(objContactUsMaster.getMessage());

            stringer.endObject();
            stringer.endObject();

            String url = Service.Url + this.InsertContactUsMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertContactUsMaster + "Result");
                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorNumber"));
                            objBusinessRequestListener = (BusinessRequestListener) context;
                            objBusinessRequestListener.BusinessResponse(errorCode, null, null);
                        } else {
                            objBusinessRequestListener = (BusinessRequestListener) context;
                            objBusinessRequestListener.BusinessResponse("-1", null, null);
                        }
                    } catch (JSONException e) {
                        objBusinessRequestListener = (BusinessRequestListener) context;
                        objBusinessRequestListener.BusinessResponse("-1", null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objBusinessRequestListener = (BusinessRequestListener) context;
                    objBusinessRequestListener.BusinessResponse("-1", null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objBusinessRequestListener = (BusinessRequestListener) context;
            objBusinessRequestListener.BusinessResponse("-1", null, null);
        }
    }
    //endregion

    public interface BusinessRequestListener {
        void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster);
    }

}
