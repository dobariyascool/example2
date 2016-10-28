package com.arraybit.parser;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.FCMMaster;

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
/// JSONParser for FCMMaster
/// </summary>
public class FCMJSONParser
{
	public String InsertFCMMaster = "InsertFCMMaster";
	public String UpdateFCMMaster = "UpdateFCMMaster";
	public String UpdateFCMMasterByCustomerId = "UpdateFCMMasterByCustomerId";
	public String SelectAllFCMMaster = "SelectAllFCMMaster";
	public String SelectAllFCMMasterFCMMasterId = "SelectAllFCMMasterFCMMasterId";

	SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
	Date dt = null;
	SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

	private FCMMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
		FCMMaster objFCMMaster = null;
		try {
			if (jsonObject != null) {
				objFCMMaster = new FCMMaster();
				objFCMMaster.setFCMMasterId(jsonObject.getLong("FCMMasterId"));
				objFCMMaster.setFCMToken(jsonObject.getString("FCMToken"));
//				dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
//				objFCMMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
//				dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
//				objFCMMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
				if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
					objFCMMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
				}

				/// Extra
			}
			return objFCMMaster;
		} catch (Exception e) {
			return null;
		}
	}

	private ArrayList<FCMMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
		ArrayList<FCMMaster> lstFCMMaster = new ArrayList<>();
		FCMMaster objFCMMaster;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				objFCMMaster = new FCMMaster();
				objFCMMaster.setFCMMasterId(jsonArray.getJSONObject(i).getLong("FCMMasterId"));
				objFCMMaster.setFCMToken(jsonArray.getJSONObject(i).getString("FCMToken"));
//				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
//				objFCMMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
//				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
//				objFCMMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
				if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
					objFCMMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
				}

				/// Extra
				lstFCMMaster.add(objFCMMaster);
			}
			return lstFCMMaster;
		} catch (Exception e) {
			return null;
		}
	}

	public void InsertFCMMaster(FCMMaster objFCMMaster, Context context) {
		dt = new Date();
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("fCMMaster");
			stringer.object();

			stringer.key("FCMToken").value(objFCMMaster.getFCMToken());
			stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
			if(objFCMMaster.getlinktoCustomerMasterId()>0) {
				stringer.key("linktoCustomerMasterId").value(objFCMMaster.getlinktoCustomerMasterId());
			}
			stringer.key("linktoBusinessMasterId").value(Globals.linktoBusinessMasterId);
			stringer.endObject();

			stringer.endObject();

			String url = Service.Url + this.InsertFCMMaster;

			RequestQueue queue = Volley.newRequestQueue(context);

			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject jsonObject) {
					try {
						Log.e("jsonObject", " " + jsonObject);
						JSONObject jsonResponse = jsonObject.getJSONObject(InsertFCMMaster + "Result");
						if (jsonResponse != null) {
//							objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//							objNotificationAddListener.NotificationAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
						}
					} catch (JSONException e) {
//						objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//						objNotificationAddListener.NotificationAddResponse("-1", null);
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
//					objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//					objNotificationAddListener.NotificationAddResponse("-1", null);
				}
			});

			jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
					30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			queue.add(jsonObjectRequest);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void UpdateFCMMasterByCustomerId(FCMMaster objFCMMaster, Context context) {
		dt = new Date();
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("fCMMaster");
			stringer.object();

			stringer.key("FCMToken").value(objFCMMaster.getFCMToken());
			stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
			if(objFCMMaster.getlinktoCustomerMasterId()>0) {
				stringer.key("linktoCustomerMasterId").value(objFCMMaster.getlinktoCustomerMasterId());
			}
			stringer.key("linktoBusinessMasterId").value(Globals.linktoBusinessMasterId);
			stringer.endObject();

			stringer.endObject();

			String url = Service.Url + this.UpdateFCMMasterByCustomerId;

			RequestQueue queue = Volley.newRequestQueue(context);

			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject jsonObject) {
					try {
						Log.e("jsonObject", " " + jsonObject);
						JSONObject jsonResponse = jsonObject.getJSONObject(UpdateFCMMasterByCustomerId + "Result");
						if (jsonResponse != null) {
//							objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//							objNotificationAddListener.NotificationAddResponse("0", SetClassPropertiesFromJSONObject(jsonResponse));
						}
					} catch (JSONException e) {
//						objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//						objNotificationAddListener.NotificationAddResponse("-1", null);
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
//					objNotificationAddListener = (NotificationJSONParser.NotificationAddListener) targetFragment;
//					objNotificationAddListener.NotificationAddResponse("-1", null);
				}
			});

			jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
					30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			queue.add(jsonObjectRequest);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String UpdateFCMMaster(FCMMaster objFCMMaster) {
		try {
			JSONStringer stringer = new JSONStringer();
			stringer.object();

			stringer.key("fCMMaster");
			stringer.object();

			stringer.key("FCMMasterId").value(objFCMMaster.getFCMMasterId());
			stringer.key("FCMToken").value(objFCMMaster.getFCMToken());
			dt = sdfControlDateFormat.parse(objFCMMaster.getUpdateDateTime());
			stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
			stringer.key("linktoCustomerMasterId").value(objFCMMaster.getlinktoCustomerMasterId());

			stringer.endObject();

			stringer.endObject();

			JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateFCMMaster, stringer);
			JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateFCMMaster + "Result");
			return String.valueOf(jsonObject.getInt("ErrorCode"));
		}
		catch (Exception ex) {
			return "-1";
		}
	}

//	public FCMMaster SelectFCMMaster(long fCMMasterId) {
//		try {
//			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectFCMMaster + "/" + fCMMasterId);
//			if (jsonResponse != null) {
//				JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectFCMMaster + "Result");
//				if (jsonObject != null) {
//					return SetClassPropertiesFromJSONObject(jsonObject);
//				}
//			}
//			return null;
//		}
//		catch (Exception ex) {
//			return null;
//		}
//	}
//
//	public ArrayList<FCMMaster> SelectAllFCMMaster() {
//		ArrayList<FCMMaster> lstFCMMaster = null;
//		try {
//			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFCMMaster);
//			if (jsonResponse != null) {
//				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFCMMaster + "Result");
//				if (jsonArray != null) {
//					lstFCMMaster = SetListPropertiesFromJSONArray(jsonArray);
//				}
//			}
//			return lstFCMMaster;
//		}
//		catch (Exception ex) {
//			return null;
//		}
//	}

//	public ArrayList<SpinnerItem> SelectAllFCMMasterFCMMasterId() {
//		ArrayList<SpinnerItem> lstSpinnerItem = null;
//		try {
//			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllFCMMasterFCMMasterId);
//			if (jsonResponse != null) {
//				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllFCMMasterFCMMasterId + "Result");
//				if (jsonArray != null) {
//					lstSpinnerItem = new ArrayList<>();
//					SpinnerItem objSpinnerItem;
//					for (int i = 0; i < jsonArray.length(); i++) {
//						objSpinnerItem = new SpinnerItem();
//						objSpinnerItem.setText(jsonArray.getJSONObject(i).getString("FCMMasterId"));
//						objSpinnerItem.setValue(jsonArray.getJSONObject(i).getInt("FCMMasterId"));
//						lstSpinnerItem.add(objSpinnerItem);
//					}
//				}
//			}
//			return lstSpinnerItem;
//		}
//		catch (Exception ex) {
//			return null;
//		}
//	}

}
