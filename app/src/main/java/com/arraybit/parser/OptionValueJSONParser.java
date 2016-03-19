package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Service;
import com.arraybit.modal.OptionValueTran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OptionValueJSONParser
{
    public String SelectAllOptionValueTran = "SelectAllItemOption";
    OptionValueRequestListener objOptionValueRequestListener;

    private OptionValueTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OptionValueTran objOptionValueTran = null;
        try {
            if (jsonObject != null) {
                objOptionValueTran = new OptionValueTran();
                objOptionValueTran.setOptionValueTranId(jsonObject.getInt("OptionValueTranId"));
                objOptionValueTran.setlinktoOptionMasterId((short)jsonObject.getInt("linktoOptionMasterId"));
                objOptionValueTran.setOptionValue(jsonObject.getString("OptionValue"));
                objOptionValueTran.setIsDeleted(jsonObject.getBoolean("IsDeleted"));

                /// Extra
                objOptionValueTran.setOptionName(jsonObject.getString("OptionName"));
            }
            return objOptionValueTran;
        } catch (JSONException e) {
            return null;
        }
    }

    private ArrayList<OptionValueTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OptionValueTran> lstOptionValueTran = new ArrayList<>();
        OptionValueTran objOptionValueTran;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOptionValueTran = new OptionValueTran();
                objOptionValueTran.setOptionValueTranId(jsonArray.getJSONObject(i).getInt("OptionValueTranId"));
                objOptionValueTran.setlinktoOptionMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOptionMasterId"));
                objOptionValueTran.setOptionValue(jsonArray.getJSONObject(i).getString("OptionValue"));
                objOptionValueTran.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));

                /// Extra
                objOptionValueTran.setOptionName(jsonArray.getJSONObject(i).getString("OptionName"));
                lstOptionValueTran.add(objOptionValueTran);
            }
            return lstOptionValueTran;
        } catch (JSONException e) {
            return null;
        }
    }

    public void SelectAllItemOptionValue(String linktoItemMasterId, final Context context, final Fragment targetFragment) {
        String url = Service.Url + this.SelectAllOptionValueTran + "/" + linktoItemMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllOptionValueTran + "Result");
                    if (jsonArray != null) {
                        ArrayList<OptionValueTran> alOptionValueTran = SetListPropertiesFromJSONArray(jsonArray);
                        objOptionValueRequestListener = (OptionValueRequestListener)targetFragment;
                        objOptionValueRequestListener.OptionValueResponse(alOptionValueTran);
                    }
                } catch (Exception e) {
                    objOptionValueRequestListener = (OptionValueRequestListener)targetFragment;
                    objOptionValueRequestListener.OptionValueResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objOptionValueRequestListener = (OptionValueRequestListener)targetFragment;
                objOptionValueRequestListener.OptionValueResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public interface OptionValueRequestListener {
        void OptionValueResponse(ArrayList<OptionValueTran> alOptionValueTran);
    }
}
