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
import com.arraybit.modal.ReviewMaster;

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
/// JSONParser for ReviewMaster
/// </summary>
public class ReviewJSONParser {
    public String InsertReviewMaster = "InsertReviewMaster";
    public String UpdateReviewMaster = "UpdateReviewMaster";
    public String DeleteReviewMaster = "DeleteReviewMaster";
    public String SelectAllReviewMaster = "SelectAllReviewMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    ReviewMasterRequestListener objReviewMasterRequestListener;
    InsertReviewMasterRequestListener objInsertReviewMasterRequestListener;

    private ReviewMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ReviewMaster objReviewMaster = null;
        try {
            if (jsonObject != null) {
                objReviewMaster = new ReviewMaster();
                objReviewMaster.setReviewMasterId(jsonObject.getLong("ReviewMasterId"));
                objReviewMaster.setStarRating(jsonObject.getDouble("StarRating"));
                objReviewMaster.setReview(jsonObject.getString("Review"));
                if (!jsonObject.getString("IsShow").equals("null")) {
                    objReviewMaster.setIsShow(jsonObject.getBoolean("IsShow"));
                }
                dt = sdfDateTimeFormat.parse(jsonObject.getString("ReviewDateTime"));
                objReviewMaster.setReviewDateTime(sdfControlDateFormat.format(dt));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objReviewMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                objReviewMaster.setlinktoRegisteredUserMasterId(jsonObject.getInt("linktoRegisteredUserMasterId"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objReviewMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objReviewMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));

                /// Extra
                objReviewMaster.setRegisteredUser(jsonObject.getString("RegisteredUser"));
                objReviewMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
                objReviewMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objReviewMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<ReviewMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ReviewMaster> lstReviewMaster = new ArrayList<>();
        ReviewMaster objReviewMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objReviewMaster = new ReviewMaster();
                objReviewMaster.setReviewMasterId(jsonArray.getJSONObject(i).getLong("ReviewMasterId"));
                objReviewMaster.setStarRating(jsonArray.getJSONObject(i).getDouble("StarRating"));
                objReviewMaster.setReview(jsonArray.getJSONObject(i).getString("Review"));
                if (!jsonArray.getJSONObject(i).getString("IsShow").equals("null")) {
                    objReviewMaster.setIsShow(jsonArray.getJSONObject(i).getBoolean("IsShow"));
                }
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("ReviewDateTime"));
                objReviewMaster.setReviewDateTime(sdfControlDateFormat.format(dt));
                /*dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objReviewMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));*/
                objReviewMaster.setlinktoRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("linktoRegisteredUserMasterId"));
                /*if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objReviewMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }*/
                objReviewMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));

                /// Extra
                objReviewMaster.setRegisteredUser(jsonArray.getJSONObject(i).getString("RegisteredUser"));
                objReviewMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
                objReviewMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstReviewMaster.add(objReviewMaster);

            }
            return lstReviewMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertReviewMaster(final ReviewMaster objReviewMaster, final Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("reviewMaster");
            stringer.object();

            stringer.key("StarRating").value(objReviewMaster.getStarRating());
            stringer.key("Review").value(objReviewMaster.getReview());
            stringer.key("IsShow").value(objReviewMaster.getIsShow());
            stringer.key("ReviewDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoRegisteredUserMasterId").value(objReviewMaster.getlinktoRegisteredUserMasterId());
            stringer.key("linktoBusinessMasterId").value(objReviewMaster.getlinktoBusinessMasterId());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.InsertReviewMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertReviewMaster + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objInsertReviewMasterRequestListener = (InsertReviewMasterRequestListener) targetFragment;
                            objInsertReviewMasterRequestListener.InsertReviewMasterResponse(errorCode, null);
                        } else {
                            objInsertReviewMasterRequestListener = (InsertReviewMasterRequestListener) targetFragment;
                            objInsertReviewMasterRequestListener.InsertReviewMasterResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objInsertReviewMasterRequestListener = (InsertReviewMasterRequestListener) targetFragment;
                        objInsertReviewMasterRequestListener.InsertReviewMasterResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objInsertReviewMasterRequestListener = (InsertReviewMasterRequestListener) targetFragment;
                    objInsertReviewMasterRequestListener.InsertReviewMasterResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objInsertReviewMasterRequestListener = (InsertReviewMasterRequestListener) targetFragment;
            objInsertReviewMasterRequestListener.InsertReviewMasterResponse("-1", null);
        }
    }

    public String UpdateReviewMaster(ReviewMaster objReviewMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("reviewMaster");
            stringer.object();

            stringer.key("ReviewMasterId").value(objReviewMaster.getReviewMasterId());
            stringer.key("StarRating").value(objReviewMaster.getStarRating());
            stringer.key("Review").value(objReviewMaster.getReview());
            stringer.key("IsShow").value(objReviewMaster.getIsShow());
            dt = sdfControlDateFormat.parse(objReviewMaster.getReviewDateTime());
            stringer.key("ReviewDateTime").value(sdfDateTimeFormat.format(dt));
            dt = sdfControlDateFormat.parse(objReviewMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoRegisteredUserMasterId").value(objReviewMaster.getlinktoRegisteredUserMasterId());
            stringer.key("linktoUserMasterIdUpdatedBy").value(objReviewMaster.getlinktoUserMasterIdUpdatedBy());
            stringer.key("linktoBusinessMasterId").value(objReviewMaster.getlinktoBusinessMasterId());

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateReviewMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateReviewMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    public String DeleteReviewMaster(long reviewMasterId) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("reviewMasterId").value(reviewMasterId);

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.DeleteReviewMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.DeleteReviewMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        } catch (Exception ex) {
            return "-1";
        }
    }

    /*public ReviewMaster SelectReviewMaster(long reviewMasterId) {
        try {
            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectReviewMaster + "/" + reviewMasterId);
            if (jsonResponse != null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectReviewMaster + "Result");
                if (jsonObject != null) {
                    return SetClassPropertiesFromJSONObject(jsonObject);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }*/

    public void SelectAllReviewMasterPageWise(final Fragment targetFragment, Context context, String currentPage, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllReviewMaster + "/" + currentPage + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllReviewMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<ReviewMaster> alReviewMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objReviewMasterRequestListener = (ReviewMasterRequestListener) targetFragment;
                        objReviewMasterRequestListener.ReviewMasterResponse(alReviewMaster);
                    }
                } catch (Exception e) {
                    objReviewMasterRequestListener = (ReviewMasterRequestListener) targetFragment;
                    objReviewMasterRequestListener.ReviewMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objReviewMasterRequestListener = (ReviewMasterRequestListener) targetFragment;
                objReviewMasterRequestListener.ReviewMasterResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface ReviewMasterRequestListener {
        void ReviewMasterResponse(ArrayList<ReviewMaster> alReviewMaster);
    }

    public interface InsertReviewMasterRequestListener {
        void InsertReviewMasterResponse(String errorCode, ReviewMaster objReviewMaster);
    }
}
