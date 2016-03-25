package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessInfoQuestionMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessInfoQuestionJSONParser {
    public String SelectBusinessInfoQuestionMaster = "SelectAllBusinessInfoAnswerMaster";

    BusinessInfoMasterRequestListener objBusinessInfoMasterRequestListener;

    private BusinessInfoQuestionMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = null;
        try {
            if (jsonObject != null) {
                objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(jsonObject.getInt("BusinessInfoQuestionMasterId"));
                objBusinessInfoQuestionMaster.setlinktoBusinessTypeMasterId((short) jsonObject.getInt("linktoBusinessTypeMasterId"));
                objBusinessInfoQuestionMaster.setQuestion(jsonObject.getString("Question"));
                objBusinessInfoQuestionMaster.setQuestionType((short) jsonObject.getInt("QuestionType"));

                /// Extra
            }
            return objBusinessInfoQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<BusinessInfoQuestionMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<BusinessInfoQuestionMaster> lstBusinessInfoQuestionMaster = new ArrayList<>();
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
                objBusinessInfoQuestionMaster.setBusinessInfoQuestionMasterId(jsonArray.getJSONObject(i).getInt("linktoBusinessInfoQuestionMasterId"));
                objBusinessInfoQuestionMaster.setQuestion(jsonArray.getJSONObject(i).getString("BusinessInfoQuestion"));
                objBusinessInfoQuestionMaster.setQuestionType((short) jsonArray.getJSONObject(i).getInt("QuestionType"));
                objBusinessInfoQuestionMaster.setIsAnswer(jsonArray.getJSONObject(i).getBoolean("IsAnswer"));
                if(!jsonArray.getJSONObject(i).getString("Answer").equals("")) {
                    objBusinessInfoQuestionMaster.setAnswer(jsonArray.getJSONObject(i).getString("Answer"));
                    lstBusinessInfoQuestionMaster.add(objBusinessInfoQuestionMaster);
                }
            }
            return lstBusinessInfoQuestionMaster;
        } catch (JSONException e) {
            return null;
        }
    }

    public void SelectAllBusinessInfoQuestionMaster(final Fragment targetFragment, final Context context, short linktoBusinessTypeMasterId, short linktoBusinessMasterId) {
        String url = Service.Url + this.SelectBusinessInfoQuestionMaster + "/" + linktoBusinessTypeMasterId + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectBusinessInfoQuestionMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objBusinessInfoMasterRequestListener = (BusinessInfoMasterRequestListener) targetFragment;
                        objBusinessInfoMasterRequestListener.BusinessInfoMasterResponse(alBusinessInfoQuestionMaster);
                    }
                } catch (Exception e) {
                    objBusinessInfoMasterRequestListener = (BusinessInfoMasterRequestListener) targetFragment;
                    objBusinessInfoMasterRequestListener.BusinessInfoMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objBusinessInfoMasterRequestListener = (BusinessInfoMasterRequestListener) targetFragment;
                objBusinessInfoMasterRequestListener.BusinessInfoMasterResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface BusinessInfoMasterRequestListener {
        void BusinessInfoMasterResponse(ArrayList<BusinessInfoQuestionMaster> alReviewMaster);
    }
}
