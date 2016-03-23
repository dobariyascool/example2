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
import com.arraybit.modal.RegisteredUserMaster;

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

public class RegisteredUserJSONParser {

    public String InsertRegisteredUserMaster = "InsertRegisteredUserMaster";
    public String UpdateRegisteredUserMaster = "UpdateRegisteredUserMaster";
    public String UpdateRegisteredUserMasterPassword = "UpdateRegisteredUserMasterPassword";
    public String SelectRegisteredUserMaster = "SelectRegisteredUserMaster";
    public String SelectAllRegisteredUserMaster = "SelectAllAreaMaster";
    public String SelectRegisteredUserMasterUserName = "SelectRegisteredUserMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    RegisteredUserRequestListener objRegisteredUserRequestListener;

    private RegisteredUserMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        RegisteredUserMaster objRegisteredUserMaster = null;
        try {
            if (jsonObject != null) {
                objRegisteredUserMaster = new RegisteredUserMaster();
                objRegisteredUserMaster.setRegisteredUserMasterId(jsonObject.getInt("RegisteredUserMasterId"));
                objRegisteredUserMaster.setEmail(jsonObject.getString("Email"));
                objRegisteredUserMaster.setPhone(jsonObject.getString("Phone"));
                objRegisteredUserMaster.setPassword(jsonObject.getString("Password"));
                objRegisteredUserMaster.setFirstName(jsonObject.getString("FirstName"));
                objRegisteredUserMaster.setLastName(jsonObject.getString("LastName"));
                objRegisteredUserMaster.setGender(jsonObject.getString("Gender"));
                //dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                //objRegisteredUserMaster.setBirthDate(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoAreaMasterId").equals("null")) {
                    objRegisteredUserMaster.setlinktoAreaMasterId((short) jsonObject.getInt("linktoAreaMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objRegisteredUserMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdCreatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                }
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                if(!jsonObject.getString("BirthDate").equals("null")){
                    dt = sdfDateFormat.parse(jsonObject.getString("BirthDate"));
                    objRegisteredUserMaster.setBirthDate(sdfControlDateFormat.format(dt));
                }
                objRegisteredUserMaster.setlinktoSourceMasterId((short) jsonObject.getInt("linktoSourceMasterId"));
                objRegisteredUserMaster.setComment(jsonObject.getString("Comment"));
                objRegisteredUserMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));

                /// Extra
                objRegisteredUserMaster.setArea(jsonObject.getString("Area"));
            }
            return objRegisteredUserMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    //region Class Methods

    private ArrayList<RegisteredUserMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<RegisteredUserMaster> lstRegisteredUserMaster = new ArrayList<>();
        RegisteredUserMaster objRegisteredUserMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objRegisteredUserMaster = new RegisteredUserMaster();
                objRegisteredUserMaster.setRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("RegisteredUserMasterId"));
                objRegisteredUserMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objRegisteredUserMaster.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                objRegisteredUserMaster.setPassword(jsonArray.getJSONObject(i).getString("Password"));
                objRegisteredUserMaster.setFirstName(jsonArray.getJSONObject(i).getString("FirstName"));
                objRegisteredUserMaster.setLastName(jsonArray.getJSONObject(i).getString("LastName"));
                objRegisteredUserMaster.setGender(jsonArray.getJSONObject(i).getString("Gender"));
                //dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("BirthDate"));
                //objRegisteredUserMaster.setBirthDate(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoAreaMasterId").equals("null")) {
                    objRegisteredUserMaster.setlinktoAreaMasterId((short) jsonArray.getJSONObject(i).getInt("linktoAreaMasterId"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objRegisteredUserMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdCreatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objRegisteredUserMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objRegisteredUserMaster.setlinktoSourceMasterId((short) jsonArray.getJSONObject(i).getInt("linktoSourceMasterId"));
                objRegisteredUserMaster.setComment(jsonArray.getJSONObject(i).getString("Comment"));
                objRegisteredUserMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));

                /// Extra
                objRegisteredUserMaster.setArea(jsonArray.getJSONObject(i).getString("Area"));
                lstRegisteredUserMaster.add(objRegisteredUserMaster);
            }
            return lstRegisteredUserMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertRegisteredUserMaster(final RegisteredUserMaster objRegisteredUserMaster, final Context context) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("Email").value(objRegisteredUserMaster.getEmail());
            stringer.key("Phone").value(objRegisteredUserMaster.getPhone());
            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
            stringer.key("FirstName").value(objRegisteredUserMaster.getFirstName());
            stringer.key("LastName").value(objRegisteredUserMaster.getLastName());
            stringer.key("Gender").value(objRegisteredUserMaster.getGender());
            if(objRegisteredUserMaster.getBirthDate()!=null) {
                stringer.key("BirthDate").value(objRegisteredUserMaster.getBirthDate());
            }
            stringer.key("linktoCityMasterId").value(objRegisteredUserMaster.getlinktoCityMasterId());
            stringer.key("linktoAreaMasterId").value(objRegisteredUserMaster.getlinktoAreaMasterId());
            stringer.key("linktoBusinessMasterId").value(objRegisteredUserMaster.getlinktoBusinessMasterId());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoSourceMasterId").value(objRegisteredUserMaster.getlinktoSourceMasterId());
            stringer.key("Comment").value(objRegisteredUserMaster.getComment());
            stringer.key("IsEnabled").value(objRegisteredUserMaster.getIsEnabled());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.InsertRegisteredUserMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject jsonResponse = jsonObject.getJSONObject(InsertRegisteredUserMaster + "Result");

                            if(jsonResponse!=null){
                                String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                                objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                                objRegisteredUserRequestListener.RegisteredUserResponse(errorCode,null);
                            }else{
                                objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                                objRegisteredUserRequestListener.RegisteredUserResponse("-1",null);
                            }
                        } catch (JSONException e) {
                            objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                            objRegisteredUserRequestListener.RegisteredUserResponse("-1",null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                        objRegisteredUserRequestListener.RegisteredUserResponse("-1",null);
                    }
                });
            queue.add(jsonObjectRequest);
        }
        catch (Exception ex) {
            objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
            objRegisteredUserRequestListener.RegisteredUserResponse("-1",null);
        }
    }

    //endregion

    //region Insert

//    public JSONStringer InsertRegisteredUserMaster(RegisteredUserMaster objRegisteredUserMaster) {
//        dt = new Date();
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("registeredUserMaster");
//            stringer.object();
//
//            stringer.key("Email").value(objRegisteredUserMaster.getEmail());
//            stringer.key("Phone").value(objRegisteredUserMaster.getPhone());
//            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
//            stringer.key("FirstName").value(objRegisteredUserMaster.getFirstName());
//            stringer.key("LastName").value(objRegisteredUserMaster.getLastName());
//            stringer.key("Gender").value(objRegisteredUserMaster.getGender());
//            if (objRegisteredUserMaster.getBirthDate() != null) {
//                stringer.key("BirthDate").value(objRegisteredUserMaster.getBirthDate());
//            }
//            stringer.key("linktoCityMasterId").value(objRegisteredUserMaster.getlinktoCityMasterId());
//            stringer.key("linktoAreaMasterId").value(objRegisteredUserMaster.getlinktoAreaMasterId());
//            stringer.key("linktoBusinessMasterId").value(objRegisteredUserMaster.getlinktoBusinessMasterId());
//            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoSourceMasterId").value(objRegisteredUserMaster.getlinktoSourceMasterId());
//            stringer.key("Comment").value(objRegisteredUserMaster.getComment());
//            stringer.key("IsEnabled").value(objRegisteredUserMaster.getIsEnabled());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
////            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertRegisteredUserMaster, stringer);
////            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertRegisteredUserMaster + "Result");
//            return stringer;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    //region Update
    public String UpdateRegisteredUserMaster(RegisteredUserMaster objRegisteredUserMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("RegisteredUserMasterId").value(objRegisteredUserMaster.getRegisteredUserMasterId());
            stringer.key("Phone").value(objRegisteredUserMaster.getPhone());
            stringer.key("FirstName").value(objRegisteredUserMaster.getFirstName());
            stringer.key("LastName").value(objRegisteredUserMaster.getLastName());
            stringer.key("Gender").value(objRegisteredUserMaster.getGender());
            if(objRegisteredUserMaster.getBirthDate()!=null) {
                stringer.key("BirthDate").value(objRegisteredUserMaster.getBirthDate());
            }
            stringer.key("linktoAreaMasterId").value(objRegisteredUserMaster.getlinktoAreaMasterId());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("Comment").value(objRegisteredUserMaster.getComment());
            stringer.key("linktoUserMasterIdUpdatedBy").value(objRegisteredUserMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateRegisteredUserMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateRegisteredUserMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    //endregion

    public String UpdateRegisteredUserMasterPassword(RegisteredUserMaster objRegisteredUserMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("registeredUserMaster");
            stringer.object();

            stringer.key("RegisteredUserMasterId").value(objRegisteredUserMaster.getRegisteredUserMasterId());
            stringer.key("Password").value(objRegisteredUserMaster.getPassword());
            stringer.key("OldPassword").value(objRegisteredUserMaster.getOldPassword());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdUpdatedBy").value(objRegisteredUserMaster.getlinktoUserMasterIdUpdatedBy());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateRegisteredUserMasterPassword, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateRegisteredUserMasterPassword + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public void SelectRegisteredUserMasterUserName(final Context context,String userName,String password) {
        try {
            String url  = Service.Url + this.SelectRegisteredUserMasterUserName + "/" + URLEncoder.encode(userName, "utf-8").replace(".", "2E") + "/" + URLEncoder.encode(password, "utf-8").replace(".", "2E") + "/" + null;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject != null) {
                        JSONObject jsonResponse = jsonObject.getJSONObject(SelectRegisteredUserMasterUserName + "Result");
                        if (jsonResponse != null) {
                            objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                            objRegisteredUserRequestListener.RegisteredUserResponse(null,SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    }
                }
                catch (Exception e) {
                    objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                    objRegisteredUserRequestListener.RegisteredUserResponse(null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
                objRegisteredUserRequestListener.RegisteredUserResponse(null,null);
            }
        });
        queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objRegisteredUserRequestListener = (RegisteredUserRequestListener)context;
            objRegisteredUserRequestListener.RegisteredUserResponse(null,null);
        }
    }
    //endregion

    //region Select

    public ArrayList<RegisteredUserMaster> SelectAllRegisteredUserMasterPageWise(JSONObject jsonResponse) {
        ArrayList<RegisteredUserMaster> lstRegisteredUserMaster = null;
        try {
            if (jsonResponse != null) {
                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllRegisteredUserMaster + "Result");
                if (jsonArray != null) {
                    lstRegisteredUserMaster = SetListPropertiesFromJSONArray(jsonArray);
                }
            }
            return lstRegisteredUserMaster;
        } catch (Exception ex) {
            return null;
        }
    }

//    public RegisteredUserMaster SelectRegisteredUserMasterUserName(JSONObject jsonResponse) {
//        try {
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectRegisteredUserMasterUserName + "Result");
//                if (jsonObject != null) {
//                    return SetClassPropertiesFromJSONObject(jsonObject);
//                }
//            }
//            return null;
//        } catch (Exception ex) {
//            return null;
//        }
//    }

    //endregion

    //region SelectAll

    public interface RegisteredUserRequestListener {
        void RegisteredUserResponse(String errorCode,RegisteredUserMaster objRegisteredUserMaster);
    }


    //endregion
}

