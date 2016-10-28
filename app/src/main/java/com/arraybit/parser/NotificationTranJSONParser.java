package com.arraybit.parser;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.NotificationMaster;
import com.arraybit.modal.NotificationTran;

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
/// JSONParser for NotificationTran
/// </summary>
public class NotificationTranJSONParser
{
	public String InsertNotificationTran = "InsertNotificationTran";

	SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
	Date dt = null;
	SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	NotificationInsertListener objNotificationInsertListener;

	private NotificationTran SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
		NotificationTran objNotificationTran = null;
		try {
			if (jsonObject != null) {
				objNotificationTran = new NotificationTran();
				objNotificationTran.setNotificationTranId(jsonObject.getLong("NotificationTranId"));
				objNotificationTran.setlinktoNotificationMasterId(jsonObject.getInt("linktoNotificationMasterId"));
				objNotificationTran.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
				dt = sdfDateTimeFormat.parse(jsonObject.getString("ReadDateTime"));
				objNotificationTran.setReadDateTime(sdfControlDateFormat.format(dt));
			}
			return objNotificationTran;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	private ArrayList<NotificationTran> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
		ArrayList<NotificationTran> lstNotificationTran = new ArrayList<>();
		NotificationTran objNotificationTran;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				objNotificationTran = new NotificationTran();
				objNotificationTran.setNotificationTranId(jsonArray.getJSONObject(i).getLong("NotificationTranId"));
				objNotificationTran.setlinktoNotificationMasterId(jsonArray.getJSONObject(i).getInt("linktoNotificationMasterId"));
				objNotificationTran.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("ReadDateTime"));
				objNotificationTran.setReadDateTime(sdfControlDateFormat.format(dt));
				lstNotificationTran.add(objNotificationTran);
			}
			return lstNotificationTran;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	public void InsertNotificationTran(NotificationTran objNotificationTran, final Context context) {
		dt= new Date();
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("notificationTran");
			stringer.object();

			stringer.key("linktoNotificationMasterId").value(objNotificationTran.getlinktoNotificationMasterId());
			stringer.key("linktoCustomerMasterId").value(objNotificationTran.getlinktoCustomerMasterId());
			stringer.key("ReadDateTime").value(sdfDateTimeFormat.format(dt));

			stringer.endObject();

			stringer.endObject();

			String url = Service.Url + this.InsertNotificationTran;

			RequestQueue queue = Volley.newRequestQueue(context);

			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject jsonObject) {
					try {
						JSONObject jsonResponse = jsonObject.getJSONObject(InsertNotificationTran + "Result");
						if (jsonResponse != null) {
							objNotificationInsertListener = (NotificationInsertListener) context;
							objNotificationInsertListener.NotificationResponse(String.valueOf(jsonResponse.getInt("ErrorCode")));
						}
					} catch (JSONException e) {
						objNotificationInsertListener = (NotificationInsertListener) context;
						objNotificationInsertListener.NotificationResponse("-1");
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					objNotificationInsertListener = (NotificationInsertListener) context;
					objNotificationInsertListener.NotificationResponse("-1");
				}
			});

			jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
					30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			queue.add(jsonObjectRequest);
		}
		catch (Exception ex) {
			objNotificationInsertListener = (NotificationInsertListener) context;
			objNotificationInsertListener.NotificationResponse("-1");
		}
	}

	//region interface
	public interface NotificationInsertListener {
		void NotificationResponse(String errorCode);
	}
	//endregion

}
