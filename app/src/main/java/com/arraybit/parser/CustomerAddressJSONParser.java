package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CustomerAddressTran;

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
/// JSONParser for CustomerAddressTran
/// </summary>
public class CustomerAddressJSONParser {
    public String InsertCustomerAddressTran = "InsertCustomerAddressTran";
    public String UpdateCustomerAddressTran = "UpdateCustomerAddressTran";
    public String DeleteCustomerAddressTran = "DeleteCustomerAddressTran";
    public String SelectCustomerAddressTran = "SelectCustomerAddressTranByMasterId";
    public String SelectAllCustomerAddressTran = "SelectAllCustomerAddressTran";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    CustomerAddressRequestListener objCustomerAddressRequestListener;

    private CustomerAddressTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CustomerAddressTran objCustomerAddressTran = null;
        try {
            if (jsonObject != null) {
                objCustomerAddressTran = new CustomerAddressTran();
                objCustomerAddressTran.setCustomerAddressTranId(jsonObject.getInt("CustomerAddressTranId"));
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                objCustomerAddressTran.setCustomerName(jsonObject.getString("CustomerName"));
                if (!jsonObject.getString("linktoRegisteredUserMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoRegisteredUserMasterId(jsonObject.getInt("linktoRegisteredUserMasterId"));
                }
                objCustomerAddressTran.setAddress(jsonObject.getString("Address"));
                if (!jsonObject.getString("AddressType").equals("null")) {
                    objCustomerAddressTran.setAddressType((short) jsonObject.getInt("AddressType"));
                }
                if (!jsonObject.getString("linktoCountryMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCountryMasterId((short) jsonObject.getInt("linktoCountryMasterId"));
                }
                if (!jsonObject.getString("linktoStateMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoStateMasterId((short) jsonObject.getInt("linktoStateMasterId"));
                }
                if (!jsonObject.getString("linktoCityMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCityMasterId((short) jsonObject.getInt("linktoCityMasterId"));
                }
                if (!jsonObject.getString("linktoAreaMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoAreaMasterId((short) jsonObject.getInt("linktoAreaMasterId"));
                }
                objCustomerAddressTran.setZipCode(jsonObject.getString("ZipCode"));
                if (!jsonObject.getString("Phone").equals("null")) {
                    objCustomerAddressTran.setMobileNum(jsonObject.getString("Phone"));
                }
                objCustomerAddressTran.setIsPrimary(jsonObject.getBoolean("IsPrimary"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objCustomerAddressTran.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerAddressTran.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                objCustomerAddressTran.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objCustomerAddressTran.setCountry(jsonObject.getString("Country"));
                objCustomerAddressTran.setState(jsonObject.getString("State"));
                objCustomerAddressTran.setCity(jsonObject.getString("City"));
                objCustomerAddressTran.setArea(jsonObject.getString("Area"));
                objCustomerAddressTran.setUserCreatedBy(jsonObject.getString("linktoUserMasterIdCreatedBy"));
            }
            return objCustomerAddressTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<CustomerAddressTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CustomerAddressTran> lstCustomerAddressTran = new ArrayList<>();
        CustomerAddressTran objCustomerAddressTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCustomerAddressTran = new CustomerAddressTran();
                objCustomerAddressTran.setCustomerAddressTranId(jsonArray.getJSONObject(i).getInt("CustomerAddressTranId"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objCustomerAddressTran.setCustomerName(jsonArray.getJSONObject(i).getString("CustomerName"));
                if (!jsonArray.getJSONObject(i).getString("linktoRegisteredUserMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("linktoRegisteredUserMasterId"));
                }
                objCustomerAddressTran.setAddress(jsonArray.getJSONObject(i).getString("Address"));
                if (!jsonArray.getJSONObject(i).getString("AddressType").equals("null")) {
                    objCustomerAddressTran.setAddressType((short) jsonArray.getJSONObject(i).getInt("AddressType"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCountryMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCountryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCountryMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoStateMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoStateMasterId((short) jsonArray.getJSONObject(i).getInt("linktoStateMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCityMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoCityMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCityMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoAreaMasterId").equals("null")) {
                    objCustomerAddressTran.setlinktoAreaMasterId((short) jsonArray.getJSONObject(i).getInt("linktoAreaMasterId"));
                }
                objCustomerAddressTran.setZipCode(jsonArray.getJSONObject(i).getString("ZipCode"));
                if (!jsonArray.getJSONObject(i).getString("Phone").equals("null")) {
                    objCustomerAddressTran.setMobileNum(jsonArray.getJSONObject(i).getString("Phone"));
                }
                objCustomerAddressTran.setIsPrimary(jsonArray.getJSONObject(i).getBoolean("IsPrimary"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objCustomerAddressTran.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCustomerAddressTran.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                objCustomerAddressTran.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objCustomerAddressTran.setCountry(jsonArray.getJSONObject(i).getString("Country"));
                objCustomerAddressTran.setState(jsonArray.getJSONObject(i).getString("State"));
                objCustomerAddressTran.setCity(jsonArray.getJSONObject(i).getString("City"));
                objCustomerAddressTran.setArea(jsonArray.getJSONObject(i).getString("Area"));
                objCustomerAddressTran.setUserCreatedBy(jsonArray.getJSONObject(i).getString("linktoUserMasterIdCreatedBy"));
                lstCustomerAddressTran.add(objCustomerAddressTran);
            }
            return lstCustomerAddressTran;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertCustomerAddressTran(final Context context, final Fragment targetFragment, CustomerAddressTran objCustomerAddressTran) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerAddressTran");
            stringer.object();

            stringer.key("linktoCustomerMasterId").value(objCustomerAddressTran.getlinktoCustomerMasterId());
            stringer.key("CustomerName").value(objCustomerAddressTran.getCustomerName());
            stringer.key("Address").value(objCustomerAddressTran.getAddress());
            stringer.key("AddressType").value(objCustomerAddressTran.getAddressType());
            stringer.key("linktoCountryMasterId").value(objCustomerAddressTran.getlinktoCountryMasterId());
            stringer.key("linktoStateMasterId").value(objCustomerAddressTran.getlinktoStateMasterId());
            stringer.key("linktoCityMasterId").value(objCustomerAddressTran.getlinktoCityMasterId());
            stringer.key("linktoAreaMasterId").value(objCustomerAddressTran.getlinktoAreaMasterId());
            stringer.key("ZipCode").value(objCustomerAddressTran.getZipCode());
            stringer.key("Phone").value(objCustomerAddressTran.getMobileNum());
            stringer.key("IsPrimary").value(objCustomerAddressTran.getIsPrimary());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objCustomerAddressTran.getlinktoUserMasterIdCreatedBy());
            stringer.key("IsDeleted").value(objCustomerAddressTran.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.InsertCustomerAddressTran;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertCustomerAddressTran + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse(errorCode, null, null);
                        } else {
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                        }
                    } catch (JSONException e) {
                        objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                        objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                    objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
            objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
        }
    }

    public void UpdateCustomerAddressTran(final Context context, final Fragment targetFragment, CustomerAddressTran objCustomerAddressTran) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("customerAddressTran");
            stringer.object();

            stringer.key("CustomerAddressTranId").value(objCustomerAddressTran.getCustomerAddressTranId());
            stringer.key("linktoCustomerMasterId").value(objCustomerAddressTran.getlinktoCustomerMasterId());
            stringer.key("CustomerName").value(objCustomerAddressTran.getCustomerName());
            stringer.key("Address").value(objCustomerAddressTran.getAddress());
            stringer.key("AddressType").value(objCustomerAddressTran.getAddressType());
            stringer.key("linktoCountryMasterId").value(objCustomerAddressTran.getlinktoCountryMasterId());
            stringer.key("linktoStateMasterId").value(objCustomerAddressTran.getlinktoStateMasterId());
            stringer.key("linktoCityMasterId").value(objCustomerAddressTran.getlinktoCityMasterId());
            stringer.key("linktoAreaMasterId").value(objCustomerAddressTran.getlinktoAreaMasterId());
            stringer.key("ZipCode").value(objCustomerAddressTran.getZipCode());
            stringer.key("Phone").value(objCustomerAddressTran.getMobileNum());
            stringer.key("IsPrimary").value(objCustomerAddressTran.getIsPrimary());
            stringer.key("IsDeleted").value(objCustomerAddressTran.getIsDeleted());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateCustomerAddressTran;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateCustomerAddressTran + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse(errorCode, null, null);
                        } else {
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                        }
                    } catch (JSONException e) {
                        objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                        objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                    objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
            objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
        }
    }

    public void DeleteCustomerAddressTran(final Context context, final Fragment targetFragment, String customerAddressTranId) {
            String url = Service.Url + this.DeleteCustomerAddressTran + "/" + customerAddressTranId;
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(DeleteCustomerAddressTran + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse(errorCode, null, null);
                        }
                    } catch (JSONException e) {
                        objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                        objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                    objCustomerAddressRequestListener.CustomerAddressResponse("-1", null, null);
                }
            });
            queue.add(jsonObjectRequest);
    }

    public void SelectAllCustomerAddressTran(final Context context, final Fragment targetFragment, String linktoCustomerMasterId) {
        String url = Service.Url + this.SelectAllCustomerAddressTran + "/" + linktoCustomerMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllCustomerAddressTran + "Result");
                    if (jsonArray != null) {
                        ArrayList<CustomerAddressTran> alCustomerAddressTran = SetListPropertiesFromJSONArray(jsonArray);
                        objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                        objCustomerAddressRequestListener.CustomerAddressResponse(null, alCustomerAddressTran, null);
                    }
                } catch (Exception e) {
                    objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                    objCustomerAddressRequestListener.CustomerAddressResponse(null, null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                objCustomerAddressRequestListener.CustomerAddressResponse(null, null, null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void SelectCustomerAddressTranByMasterId(final Context context, final Fragment targetFragment, String customerAddressTranId) {
        String url = Service.Url + this.SelectCustomerAddressTran + "/" + customerAddressTranId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject != null) {
                        JSONObject jsonResponse = jsonObject.getJSONObject(SelectCustomerAddressTran + "Result");
                        if (jsonResponse != null) {
                            objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                            objCustomerAddressRequestListener.CustomerAddressResponse(null, null, SetClassPropertiesFromJSONObject(jsonResponse));
                        }
                    }
                } catch (Exception e) {
                    objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                    objCustomerAddressRequestListener.CustomerAddressResponse(null, null, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objCustomerAddressRequestListener = (CustomerAddressRequestListener) targetFragment;
                objCustomerAddressRequestListener.CustomerAddressResponse(null, null, null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface CustomerAddressRequestListener {
        void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran);
    }
}
