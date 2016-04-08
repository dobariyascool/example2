package com.arraybit.parser;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessDescription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/// <summary>
/// JSONParser for BusinessDescription
/// </summary>
public class BusinessDescriptionJSONParser
{
	public String SelectAllBusinessDescription = "SelectAllBusinessDescriptionByBusinessMasterId";

	SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
	Date dt = null;
	SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

	private BusinessDescription SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
		BusinessDescription objBusinessDescription = null;
		try {
			if (jsonObject != null) {
				objBusinessDescription = new BusinessDescription();
				objBusinessDescription.setBusinessDescriptionId((short)jsonObject.getInt("BusinessDescriptionId"));
				objBusinessDescription.setKeyword(jsonObject.getString("Keyword"));
				objBusinessDescription.setDescription(jsonObject.getString("Description"));
				objBusinessDescription.setlinktoBusinessMasterId((short)jsonObject.getInt("linktoBusinessMasterId"));
				objBusinessDescription.setIsDefault(jsonObject.getBoolean("IsDefault"));
				objBusinessDescription.setlinktoUserMasterIdCreatedBy((short)jsonObject.getInt("linktoUserMasterIdCreatedBy"));
				dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
				objBusinessDescription.setCreateDateTime(sdfControlDateFormat.format(dt));
				if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
					objBusinessDescription.setlinktoUserMasterIdUpdatedBy((short)jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
				}
				dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
				objBusinessDescription.setUpdateDateTime(sdfControlDateFormat.format(dt));

				/// Extra
				objBusinessDescription.setBusiness(jsonObject.getString("Business"));
			}
			return objBusinessDescription;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	private ArrayList<BusinessDescription> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
		ArrayList<BusinessDescription> lstBusinessDescription = new ArrayList<>();
		BusinessDescription objBusinessDescription;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				objBusinessDescription = new BusinessDescription();
				objBusinessDescription.setBusinessDescriptionId((short)jsonArray.getJSONObject(i).getInt("BusinessDescriptionId"));
				objBusinessDescription.setKeyword(jsonArray.getJSONObject(i).getString("Keyword"));
				objBusinessDescription.setDescription(jsonArray.getJSONObject(i).getString("Description"));
				objBusinessDescription.setlinktoBusinessMasterId((short)jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
				objBusinessDescription.setIsDefault(jsonArray.getJSONObject(i).getBoolean("IsDefault"));
				objBusinessDescription.setlinktoUserMasterIdCreatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
				objBusinessDescription.setCreateDateTime(sdfControlDateFormat.format(dt));
				if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
					objBusinessDescription.setlinktoUserMasterIdUpdatedBy((short)jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
				}
				dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
				objBusinessDescription.setUpdateDateTime(sdfControlDateFormat.format(dt));

				/// Extra
				objBusinessDescription.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
				lstBusinessDescription.add(objBusinessDescription);
			}
			return lstBusinessDescription;
		} catch (JSONException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

/*	public ArrayList<BusinessDescription> SelectAllBusinessDescription(String ) {
		ArrayList<BusinessDescription> lstBusinessDescription = null;
		try {
			JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllBusinessDescription);
			if (jsonResponse != null) {
				JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllBusinessDescription + "Result");
				if (jsonArray != null) {
					lstBusinessDescription = SetListPropertiesFromJSONArray(jsonArray);
				}
			}
			return lstBusinessDescription;
		}
		catch (Exception ex) {
			return null;
		}
	}*/

}
