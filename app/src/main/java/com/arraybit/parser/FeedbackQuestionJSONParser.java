package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackMaster;
import com.arraybit.modal.FeedbackQuestionMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FeedbackQuestionJSONParser {
    public String InsertFeedbackQuestionMaster = "InsertFeedbackQuestionMaster";
    public String UpdateFeedbackQuestionMaster = "UpdateFeedbackQuestionMaster";
    public String DeleteFeedbackQuestionMaster = "DeleteFeedbackQuestionMaster";
    public String SelectFeedbackQuestionMaster = "SelectFeedbackQuestionMaster";
    public String SelectAllQuestionAnswer = "SelectAllQuestionAnswer";
    public String SelectAllFeedbackQuestionMasterFeedbackQuestion = "SelectAllFeedbackQuestionMasterFeedbackQuestion";
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    Date dt = null;
    FeedbackQuestionRequestListener objFeedbackQuestionRequestListener;
    FeedbackSubmitRequestListener objFeedbackSubmitRequestListener;

    private FeedbackQuestionMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        FeedbackQuestionMaster objFeedbackQuestionMaster = null;
        try {
            if (jsonObject != null) {
                objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                if (!jsonObject.getString("FeedbackQuestionMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setFeedbackQuestionMasterId(jsonObject.getInt("FeedbackQuestionMasterId"));
                }
                objFeedbackQuestionMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("linktoFeedbackQuestionGroupMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setlinktoFeedbackQuestionGroupMasterId((short) jsonObject.getInt("linktoFeedbackQuestionGroupMasterId"));
                }
                objFeedbackQuestionMaster.setFeedbackQuestion(jsonObject.getString("FeedbackQuestion"));
                if (!jsonObject.getString("QuestionType").equals("null")) {
                    objFeedbackQuestionMaster.setQuestionType((short) jsonObject.getInt("QuestionType"));
                }
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objFeedbackQuestionMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                objFeedbackQuestionMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objFeedbackQuestionMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objFeedbackQuestionMaster.setBusiness(jsonObject.getString("Business"));
                objFeedbackQuestionMaster.setFeedbackQuestionGroup(jsonObject.getString("GroupName"));
                if (!jsonObject.getString("linktoFeedbackAnswerMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setlinktoFeedbackAnswerMasterId(jsonObject.getInt("linktoFeedbackAnswerMasterId"));
                }
                objFeedbackQuestionMaster.setFeedbackAnswer(jsonObject.getString("FeedbackAnswer"));
            }
            return objFeedbackQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<FeedbackQuestionMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<FeedbackQuestionMaster> lstFeedbackQuestionMaster = new ArrayList<>();
        FeedbackQuestionMaster objFeedbackQuestionMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                objFeedbackQuestionMaster.setFeedbackRowPosition(-1);
                if (!jsonArray.getJSONObject(i).getString("FeedbackQuestionMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setFeedbackQuestionMasterId(jsonArray.getJSONObject(i).getInt("FeedbackQuestionMasterId"));
                }
                objFeedbackQuestionMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoFeedbackQuestionGroupMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setlinktoFeedbackQuestionGroupMasterId((short) jsonArray.getJSONObject(i).getInt("linktoFeedbackQuestionGroupMasterId"));
                }
                objFeedbackQuestionMaster.setFeedbackQuestion(jsonArray.getJSONObject(i).getString("FeedbackQuestion"));
                if (!jsonArray.getJSONObject(i).getString("QuestionType").equals("null")) {
                    objFeedbackQuestionMaster.setQuestionType((short) jsonArray.getJSONObject(i).getInt("QuestionType"));
                }
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objFeedbackQuestionMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objFeedbackQuestionMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objFeedbackQuestionMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objFeedbackQuestionMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                objFeedbackQuestionMaster.setFeedbackQuestionGroup(jsonArray.getJSONObject(i).getString("GroupName"));
                if (!jsonArray.getJSONObject(i).getString("linktoFeedbackAnswerMasterId").equals("null")) {
                    objFeedbackQuestionMaster.setlinktoFeedbackAnswerMasterId(jsonArray.getJSONObject(i).getInt("linktoFeedbackAnswerMasterId"));
                }
                objFeedbackQuestionMaster.setFeedbackAnswer(jsonArray.getJSONObject(i).getString("FeedbackAnswer"));
                if (!jsonArray.getJSONObject(i).getString("FeedbackQuestionMasterId").equals("null")) {
                    lstFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
                }
            }
            return lstFeedbackQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    //region Insert
    public void InsertFeedbackMaster(final Fragment targetFragment, Context context, final FeedbackMaster objFeedbackMaster, ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("feedbackMaster");
            stringer.object();

            stringer.key("Name").value(objFeedbackMaster.getName());
            stringer.key("Email").value(objFeedbackMaster.getEmail());
            stringer.key("Phone").value(objFeedbackMaster.getPhone());
            stringer.key("Feedback").value(objFeedbackMaster.getFeedback());
            stringer.key("FeedbackDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("FeedbackType").value(objFeedbackMaster.getFeedbackType());
            stringer.key("linktoCustomerMasterId").value(objFeedbackMaster.getlinktoCustomerMasterId());
            stringer.key("linktoBusinessMasterId").value(objFeedbackMaster.getlinktoBusinessMasterId());

            stringer.endObject();

            stringer.key("lstFeedbackTran");
            stringer.array();

            for (int i = 0; i < alFeedbackAnswerMaster.size(); i++) {
                stringer.object();
                stringer.key("linktoFeedbackQuestionMasterId").value(alFeedbackAnswerMaster.get(i).getlinktoFeedbackQuestionMasterId());
                stringer.key("linktoFeedbackAnswerMasterId").value(alFeedbackAnswerMaster.get(i).getFeedbackAnswerMasterId());
                stringer.key("Answer").value(alFeedbackAnswerMaster.get(i).getAnswer());
                stringer.endObject();
            }

            stringer.endArray();
            stringer.endObject();

            String url = Service.Url + this.InsertFeedbackQuestionMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertFeedbackQuestionMaster + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            objFeedbackSubmitRequestListener = (FeedbackSubmitRequestListener) targetFragment;
                            objFeedbackSubmitRequestListener.FeedbackSubmitResponse(errorCode, null);
                        } else {
                            objFeedbackSubmitRequestListener = (FeedbackSubmitRequestListener) targetFragment;
                            objFeedbackSubmitRequestListener.FeedbackSubmitResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objFeedbackSubmitRequestListener = (FeedbackSubmitRequestListener) targetFragment;
                        objFeedbackSubmitRequestListener.FeedbackSubmitResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objFeedbackSubmitRequestListener = (FeedbackSubmitRequestListener) targetFragment;
                    objFeedbackSubmitRequestListener.FeedbackSubmitResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objFeedbackSubmitRequestListener = (FeedbackSubmitRequestListener) targetFragment;
            objFeedbackSubmitRequestListener.FeedbackSubmitResponse("-1", null);
        }
    }
    //endregion

    //region SelectAll
    public void SelectAllFeedbackQuestionAnswer(final Context context, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllQuestionAnswer + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllQuestionAnswer + "Result");
                    if (jsonArray != null) {
                        ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objFeedbackQuestionRequestListener = (FeedbackQuestionRequestListener) context;
                        objFeedbackQuestionRequestListener.FeedbackQuestionResponse(alFeedbackQuestionMaster);
                    }
                } catch (Exception e) {
                    objFeedbackQuestionRequestListener = (FeedbackQuestionRequestListener) context;
                    objFeedbackQuestionRequestListener.FeedbackQuestionResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objFeedbackQuestionRequestListener = (FeedbackQuestionRequestListener) context;
                objFeedbackQuestionRequestListener.FeedbackQuestionResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }
    //endregion

    public interface FeedbackQuestionRequestListener {
        void FeedbackQuestionResponse(ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster);
    }

    public interface FeedbackSubmitRequestListener {
        void FeedbackSubmitResponse(String errorCode, FeedbackMaster objCustomerMaster);
    }

    //    public ArrayList<FeedbackQuestionMaster> SelectAllFeedbackQuestionMasterPageWise(int currentPage) {
//        ArrayList<FeedbackQuestionMaster> lstFeedbackQuestionMaster = null;
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFeedbackQuestionMasterPageWise);
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFeedbackQuestionMaster + "PageWiseResult");
//                if (jsonArray != null) {
//                    lstFeedbackQuestionMaster = SetListPropertiesFromJSONArray(jsonArray);
//                }
//            }
//            return lstFeedbackQuestionMaster;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
}