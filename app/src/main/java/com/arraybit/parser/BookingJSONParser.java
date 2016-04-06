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
import com.arraybit.modal.BookingMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingJSONParser {
    public String InsertBookingMaster = "InsertBookingMaster";
    public String UpdateBookingMaster = "UpdateBookingMaster";
    public String DeleteBookingMaster = "DeleteBookingMaster";
    public String SelectBookingMaster = "SelectBookingMaster";
    public String SelectAllBookingMaster = "SelectAllBookingMasterPageWise";

    AddBooingRequestListener objAddBooingRequestListener;

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    private BookingMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BookingMaster objBookingMaster = null;
        try {
            if (jsonObject != null) {
                objBookingMaster = new BookingMaster();
                objBookingMaster.setBookingMasterId(jsonObject.getInt("BookingMasterId"));
                dt = sdfDateFormat.parse(jsonObject.getString("FromDate"));
                objBookingMaster.setFromDate(sdfControlDateFormat.format(dt));
                dt = sdfDateFormat.parse(jsonObject.getString("ToDate"));
                objBookingMaster.setToDate(sdfControlDateFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("FromTime"));
                objBookingMaster.setFromTime(sdfTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonObject.getString("ToTime"));
                objBookingMaster.setToTime(sdfTimeFormat.format(dt));
                if (!jsonObject.getString("IsHourly").equals("null")) {
                    objBookingMaster.setIsHourly(jsonObject.getBoolean("IsHourly"));
                }
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objBookingMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                objBookingMaster.setBookingPersonName(jsonObject.getString("BookingPersonName"));
                objBookingMaster.setEmail(jsonObject.getString("Email"));
                objBookingMaster.setPhone(jsonObject.getString("Phone"));
                objBookingMaster.setNoOfAdults((short) jsonObject.getInt("NoOfAdults"));
                objBookingMaster.setNoOfChildren((short) jsonObject.getInt("NoOfChildren"));
                objBookingMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objBookingMaster.setDiscountPercentage((short) jsonObject.getInt("DiscountPercentage"));
                objBookingMaster.setDiscountAmount(jsonObject.getDouble("DiscountAmount"));
                objBookingMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objBookingMaster.setNetAmount(jsonObject.getDouble("NetAmount"));
                objBookingMaster.setPaidAmount(jsonObject.getDouble("PaidAmount"));
                objBookingMaster.setBalanceAmount(jsonObject.getDouble("BalanceAmount"));
                objBookingMaster.setRemark(jsonObject.getString("Remark"));
                if (!jsonObject.getString("IsPreOrder").equals("null")) {
                    objBookingMaster.setIsPreOrder(jsonObject.getBoolean("IsPreOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objBookingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objBookingMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objBookingMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBookingMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objBookingMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                objBookingMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                if (!jsonObject.getString("BookingStatus").equals("null")) {
                    objBookingMaster.setBookingStatus((short) jsonObject.getInt("BookingStatus"));
                }

                /// Extra
//                objBookingMaster.setUserCreatedBy(jsonObject.getString("UserCreatedBy"));
//                objBookingMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
//                objBookingMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objBookingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<BookingMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BookingMaster> lstBookingMaster = new ArrayList<>();
        BookingMaster objBookingMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBookingMaster = new BookingMaster();
                objBookingMaster.setBookingMasterId(jsonArray.getJSONObject(i).getInt("BookingMasterId"));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("FromDate"));
                objBookingMaster.setFromDate(sdfControlDateFormat.format(dt));
                dt = sdfDateFormat.parse(jsonArray.getJSONObject(i).getString("ToDate"));
                objBookingMaster.setToDate(sdfControlDateFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("FromTime"));
                objBookingMaster.setFromTime(sdfTimeFormat.format(dt));
                dt = sdfTimeFormat.parse(jsonArray.getJSONObject(i).getString("ToTime"));
                objBookingMaster.setToTime(sdfTimeFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("IsHourly").equals("null")) {
                    objBookingMaster.setIsHourly(jsonArray.getJSONObject(i).getBoolean("IsHourly"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objBookingMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objBookingMaster.setBookingPersonName(jsonArray.getJSONObject(i).getString("BookingPersonName"));
                objBookingMaster.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                objBookingMaster.setPhone(jsonArray.getJSONObject(i).getString("Phone"));
                objBookingMaster.setNoOfAdults((short) jsonArray.getJSONObject(i).getInt("NoOfAdults"));
                objBookingMaster.setNoOfChildren((short) jsonArray.getJSONObject(i).getInt("NoOfChildren"));
                objBookingMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objBookingMaster.setDiscountPercentage((short) jsonArray.getJSONObject(i).getInt("DiscountPercentage"));
                objBookingMaster.setDiscountAmount(jsonArray.getJSONObject(i).getDouble("DiscountAmount"));
                objBookingMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objBookingMaster.setNetAmount(jsonArray.getJSONObject(i).getDouble("NetAmount"));
                objBookingMaster.setPaidAmount(jsonArray.getJSONObject(i).getDouble("PaidAmount"));
                objBookingMaster.setBalanceAmount(jsonArray.getJSONObject(i).getDouble("BalanceAmount"));
                objBookingMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                if (!jsonArray.getJSONObject(i).getString("IsPreOrder").equals("null")) {
                    objBookingMaster.setIsPreOrder(jsonArray.getJSONObject(i).getBoolean("IsPreOrder"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objBookingMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objBookingMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objBookingMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objBookingMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objBookingMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                objBookingMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                if (!jsonArray.getJSONObject(i).getString("BookingStatus").equals("null")) {
                    objBookingMaster.setBookingStatus((short) jsonArray.getJSONObject(i).getInt("BookingStatus"));
                }

                /// Extra
//                objBookingMaster.setUserCreatedBy(jsonArray.getJSONObject(i).getString("UserCreatedBy"));
//                objBookingMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
//                objBookingMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstBookingMaster.add(objBookingMaster);
            }
            return lstBookingMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertBookingMaster(final Context context, final Fragment targetFragment, BookingMaster objBookingMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("bookingMaster");
            stringer.object();

            stringer.key("FromDate").value(objBookingMaster.getFromDate());
            stringer.key("ToDate").value(objBookingMaster.getFromDate());
            stringer.key("FromTime").value(objBookingMaster.getFromTime());
            stringer.key("ToTime").value(objBookingMaster.getToTime());
            stringer.key("IsHourly").value(objBookingMaster.getIsHourly());
            stringer.key("linktoCustomerMasterId").value(objBookingMaster.getlinktoCustomerMasterId());
            stringer.key("BookingPersonName").value(objBookingMaster.getBookingPersonName());
            stringer.key("Email").value(objBookingMaster.getEmail());
            stringer.key("Phone").value(objBookingMaster.getPhone());
            stringer.key("NoOfAdults").value(objBookingMaster.getNoOfAdults());
            stringer.key("NoOfChildren").value(objBookingMaster.getNoOfChildren());
            stringer.key("TotalAmount").value(objBookingMaster.getTotalAmount());
            stringer.key("DiscountPercentage").value(objBookingMaster.getDiscountPercentage());
            stringer.key("DiscountAmount").value(objBookingMaster.getDiscountAmount());
            stringer.key("ExtraAmount").value(objBookingMaster.getExtraAmount());
            stringer.key("NetAmount").value(objBookingMaster.getNetAmount());
            stringer.key("PaidAmount").value(objBookingMaster.getPaidAmount());
            stringer.key("BalanceAmount").value(objBookingMaster.getBalanceAmount());
            stringer.key("Remark").value(objBookingMaster.getRemark());
            stringer.key("IsPreOrder").value(objBookingMaster.getIsPreOrder());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoUserMasterIdCreatedBy").value(objBookingMaster.getlinktoUserMasterIdCreatedBy());
            stringer.key("linktoBusinessMasterId").value(objBookingMaster.getlinktoBusinessMasterId());
            stringer.key("IsDeleted").value(objBookingMaster.getIsDeleted());
            stringer.key("BookingStatus").value(objBookingMaster.getBookingStatus());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.InsertBookingMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertBookingMaster + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorNumber"));
                            objAddBooingRequestListener = (AddBooingRequestListener) targetFragment;
                            objAddBooingRequestListener.AddBookingResponse(errorCode);
                        } else {
                            objAddBooingRequestListener = (AddBooingRequestListener) targetFragment;
                            objAddBooingRequestListener.AddBookingResponse("-1");
                        }
                    } catch (JSONException e) {
                        objAddBooingRequestListener = (AddBooingRequestListener) targetFragment;
                        objAddBooingRequestListener.AddBookingResponse("-1");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objAddBooingRequestListener = (AddBooingRequestListener) targetFragment;
                    objAddBooingRequestListener.AddBookingResponse("-1");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objAddBooingRequestListener = (AddBooingRequestListener) targetFragment;
            objAddBooingRequestListener.AddBookingResponse("-1");
        }
    }

    public interface AddBooingRequestListener{
        void AddBookingResponse(String errorCode);
    }
}

//region Commented Code
//    public String UpdateBookingMaster(BookingMaster objBookingMaster) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("bookingMaster");
//            stringer.object();
//
//            stringer.key("BookingMasterId").value(objBookingMaster.getBookingMasterId());
//            dt = sdfControlDateFormat.parse(objBookingMaster.getFromDate());
//            stringer.key("FromDate").value(sdfDateFormat.format(dt));
//            dt = sdfControlDateFormat.parse(objBookingMaster.getToDate());
//            stringer.key("ToDate").value(sdfDateFormat.format(dt));
//            dt = sdfControlDateFormat.parse(objBookingMaster.getFromTime());
//            stringer.key("FromTime").value(sdfTimeFormat.format(dt));
//            dt = sdfControlDateFormat.parse(objBookingMaster.getToTime());
//            stringer.key("ToTime").value(sdfTimeFormat.format(dt));
//            stringer.key("IsHourly").value(objBookingMaster.getIsHourly());
//            stringer.key("linktoCustomerMasterId").value(objBookingMaster.getlinktoCustomerMasterId());
//            stringer.key("BookingPersonName").value(objBookingMaster.getBookingPersonName());
//            stringer.key("Email").value(objBookingMaster.getEmail());
//            stringer.key("Phone").value(objBookingMaster.getPhone());
//            stringer.key("NoOfAdults").value(objBookingMaster.getNoOfAdults());
//            stringer.key("NoOfChildren").value(objBookingMaster.getNoOfChildren());
//            stringer.key("TotalAmount").value(objBookingMaster.getTotalAmount());
//            stringer.key("DiscountPercentage").value(objBookingMaster.getDiscountPercentage());
//            stringer.key("DiscountAmount").value(objBookingMaster.getDiscountAmount());
//            stringer.key("ExtraAmount").value(objBookingMaster.getExtraAmount());
//            stringer.key("NetAmount").value(objBookingMaster.getNetAmount());
//            stringer.key("PaidAmount").value(objBookingMaster.getPaidAmount());
//            stringer.key("BalanceAmount").value(objBookingMaster.getBalanceAmount());
//            stringer.key("Remark").value(objBookingMaster.getRemark());
//            stringer.key("IsPreOrder").value(objBookingMaster.getIsPreOrder());
//            dt = sdfControlDateFormat.parse(objBookingMaster.getUpdateDateTime());
//            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoUserMasterIdUpdatedBy").value(objBookingMaster.getlinktoUserMasterIdUpdatedBy());
//            stringer.key("linktoBusinessMasterId").value(objBookingMaster.getlinktoBusinessMasterId());
//            stringer.key("IsDeleted").value(objBookingMaster.getIsDeleted());
//            stringer.key("BookingStatus").value(objBookingMaster.getBookingStatus());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateBookingMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateBookingMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public String DeleteBookingMaster(int bookingMasterId) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("bookingMasterId").value(bookingMasterId);
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.DeleteBookingMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.DeleteBookingMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public BookingMaster SelectBookingMaster(int bookingMasterId) {
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectBookingMaster + "/" + bookingMasterId);
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectBookingMaster + "Result");
//                if (jsonObject != null) {
//                    return SetClassPropertiesFromJSONObject(jsonObject);
//                }
//            }
//            return null;
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
//
//    public ArrayList<BookingMaster> SelectAllBookingMasterPageWise(int currentPage) {
//        ArrayList<BookingMaster> lstBookingMaster = null;
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBookingMasterPageWise);
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBookingMaster + "PageWiseResult");
//                if (jsonArray != null) {
//                    lstBookingMaster = SetListPropertiesFromJSONArray(jsonArray);
//                }
//            }
//            return lstBookingMaster;
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
//endregion

