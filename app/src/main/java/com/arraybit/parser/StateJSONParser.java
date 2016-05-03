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
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.StateMaster;

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
/// JSONParser for StateMaster
/// </summary>
public class StateJSONParser {
    public String InsertStateMaster = "InsertStateMaster";
    public String UpdateStateMaster = "UpdateStateMaster";
    public String SelectAllStateMaster = "SelectAllStateMasterStateNameByCountry";
    //public String SelectAllStateMaster = "SelectAllStateMasterPageWise";
    public String SelectAllStateMasterStateName = "SelectAllStateMasterStateName";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    StateRequestListener objStateRequestListener;

    private StateMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        StateMaster objStateMaster = null;
        try {
            if (jsonObject != null) {
                objStateMaster = new StateMaster();
                objStateMaster.setStateMasterId((short) jsonObject.getInt("StateMasterId"));
                objStateMaster.setStateName(jsonObject.getString("StateName"));
                objStateMaster.setStateCode(jsonObject.getString("StateCode"));
                objStateMaster.setlinktoCountryMasterId((short) jsonObject.getInt("linktoCountryMasterId"));
                objStateMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objStateMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objStateMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objStateMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objStateMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objStateMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objStateMaster.setCountry(jsonObject.getString("Country"));
                objStateMaster.setUserCreatedBy(jsonObject.getString("UserCreatedBy"));
                objStateMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
            }
            return objStateMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<StateMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<StateMaster> lstStateMaster = new ArrayList<>();
        StateMaster objStateMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objStateMaster = new StateMaster();
                objStateMaster.setStateMasterId((short) jsonArray.getJSONObject(i).getInt("StateMasterId"));
                objStateMaster.setStateName(jsonArray.getJSONObject(i).getString("StateName"));
                objStateMaster.setStateCode(jsonArray.getJSONObject(i).getString("StateCode"));
                objStateMaster.setlinktoCountryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCountryMasterId"));
                objStateMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objStateMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objStateMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objStateMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objStateMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objStateMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objStateMaster.setCountry(jsonArray.getJSONObject(i).getString("Country"));
                objStateMaster.setUserCreatedBy(jsonArray.getJSONObject(i).getString("UserCreatedBy"));
                objStateMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
                lstStateMaster.add(objStateMaster);
            }
            return lstStateMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public String InsertStateMaster(StateMaster objStateMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("stateMaster");
            stringer.object();

            stringer.key("StateName").value(objStateMaster.getStateName());
            stringer.key("StateCode").value(objStateMaster.getStateCode());
            stringer.key("linktoCountryMasterId").value(objStateMaster.getlinktoCountryMasterId());
            stringer.key("IsEnabled").value(objStateMaster.getIsEnabled());
            stringer.key("IsDeleted").value(objStateMaster.getIsDeleted());
            dt = sdfControlDateFormat.parse(objStateMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objStateMaster.getlinktoUserMasterIdCreatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertStateMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertStateMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateStateMaster(StateMaster objStateMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("stateMaster");
            stringer.object();

            stringer.key("StateMasterId").value(objStateMaster.getStateMasterId());
            stringer.key("StateName").value(objStateMaster.getStateName());
            stringer.key("StateCode").value(objStateMaster.getStateCode());
            stringer.key("linktoCountryMasterId").value(objStateMaster.getlinktoCountryMasterId());
            stringer.key("IsEnabled").value(objStateMaster.getIsEnabled());
            stringer.key("IsDeleted").value(objStateMaster.getIsDeleted());
            dt = sdfControlDateFormat.parse(objStateMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objStateMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateStateMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateStateMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public void SelectAllStateMaster(final Fragment targetFragment, final Context context, String countryMasterId) {
        String url = Service.Url + this.SelectAllStateMaster + "/" + countryMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                ArrayList<SpinnerItem> lstSpinnerItem = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllStateMaster + "Result");
                    if (jsonArray != null) {
                        lstSpinnerItem = new ArrayList<>();
                        SpinnerItem objSpinnerItem;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objSpinnerItem = new SpinnerItem();
                            objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("StateName"));
                            objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("StateMasterId"));
                            lstSpinnerItem.add(objSpinnerItem);
                        }

                    }
                    if(targetFragment==null) {
                        objStateRequestListener = (StateRequestListener) context;
                        objStateRequestListener.StateResponse(lstSpinnerItem);
                    }else{
                        objStateRequestListener = (StateRequestListener) targetFragment;
                        objStateRequestListener.StateResponse(lstSpinnerItem);
                    }
                } catch (Exception e) {
                    if(targetFragment==null) {
                        objStateRequestListener = (StateRequestListener) context;
                        objStateRequestListener.StateResponse(null);
                    }else{
                        objStateRequestListener = (StateRequestListener) targetFragment;
                        objStateRequestListener.StateResponse(null);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(targetFragment==null) {
                    objStateRequestListener = (StateRequestListener) context;
                    objStateRequestListener.StateResponse(null);
                }else{
                    objStateRequestListener = (StateRequestListener) targetFragment;
                    objStateRequestListener.StateResponse(null);
                }
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface StateRequestListener {
        void StateResponse(ArrayList<SpinnerItem> alStateMaster);
    }
}

