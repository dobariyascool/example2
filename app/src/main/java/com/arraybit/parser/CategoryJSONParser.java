package com.arraybit.parser;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CategoryJSONParser {

    public String SelectAllCategoryMaster = "SelectAllCategoryMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    CategoryRequestListener objCategoryRequestListener;

    //region ClassMethod

    private CategoryMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        CategoryMaster objCategoryMaster = null;
        try {
            if (jsonObject != null) {
                objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) jsonObject.getInt("CategoryMasterId"));
                objCategoryMaster.setCategoryName(jsonObject.getString("CategoryName"));
                objCategoryMaster.setImageName(jsonObject.getString("ImageName"));
                objCategoryMaster.setCategoryColor(jsonObject.getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonObject.getString("Description"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonObject.getInt("SortOrder"));
                }
                objCategoryMaster.setSEOPageTitle(jsonObject.getString("SEOPageTitle"));
                objCategoryMaster.setSEOMetaDescription(jsonObject.getString("SEOMetaDescription"));
                objCategoryMaster.setSEOMetaKeywords(jsonObject.getString("SEOMetaKeywords"));
                objCategoryMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objCategoryMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objCategoryMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCategoryMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objCategoryMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCategoryMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objCategoryMaster.setCategoryParent(jsonObject.getString("CategoryParent"));
                objCategoryMaster.setBusiness(jsonObject.getString("Business"));
            }
            return objCategoryMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<CategoryMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<CategoryMaster> lstCategoryMaster = new ArrayList<>();
        CategoryMaster objCategoryMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objCategoryMaster = new CategoryMaster();
                objCategoryMaster.setCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("CategoryMasterId"));
                objCategoryMaster.setCategoryName(jsonArray.getJSONObject(i).getString("CategoryName"));
                objCategoryMaster.setImageName(jsonArray.getJSONObject(i).getString("ImageName"));
                objCategoryMaster.setCategoryColor(jsonArray.getJSONObject(i).getString("CategoryColor"));
                objCategoryMaster.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                objCategoryMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objCategoryMaster.setSortOrder((short) jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objCategoryMaster.setSEOPageTitle(jsonArray.getJSONObject(i).getString("SEOPageTitle"));
                objCategoryMaster.setSEOMetaDescription(jsonArray.getJSONObject(i).getString("SEOMetaDescription"));
                objCategoryMaster.setSEOMetaKeywords(jsonArray.getJSONObject(i).getString("SEOMetaKeywords"));
                objCategoryMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objCategoryMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objCategoryMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objCategoryMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objCategoryMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objCategoryMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }

                /// Extra
                objCategoryMaster.setCategoryParent(jsonArray.getJSONObject(i).getString("CategoryParent"));
                objCategoryMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                lstCategoryMaster.add(objCategoryMaster);
            }
            return lstCategoryMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
    //endregion

    //region Commented Code
//    public String InsertCategoryMaster(CategoryMaster objCategoryMaster) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("categoryMaster");
//            stringer.object();
//
//            stringer.key("CategoryName").value(objCategoryMaster.getCategoryName());
//            stringer.key("linktoCategoryMasterIdParent").value(objCategoryMaster.getlinktoCategoryMasterIdParent());
//            stringer.key("ImageNameBytes").value(objCategoryMaster.getImageNameBytes());
//            stringer.key("CategoryColor").value(objCategoryMaster.getCategoryColor());
//            stringer.key("Description").value(objCategoryMaster.getDescription());
//            stringer.key("linktoBusinessMasterId").value(objCategoryMaster.getlinktoBusinessMasterId());
//            stringer.key("SortOrder").value(objCategoryMaster.getSortOrder());
//            stringer.key("SEOPageTitle").value(objCategoryMaster.getSEOPageTitle());
//            stringer.key("SEOMetaDescription").value(objCategoryMaster.getSEOMetaDescription());
//            stringer.key("SEOMetaKeywords").value(objCategoryMaster.getSEOMetaKeywords());
//            stringer.key("IsEnabled").value(objCategoryMaster.getIsEnabled());
//            stringer.key("IsDeleted").value(objCategoryMaster.getIsDeleted());
//            dt = sdfControlDateFormat.parse(objCategoryMaster.getCreateDateTime());
//            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoUserMasterIdCreatedBy").value(objCategoryMaster.getlinktoUserMasterIdCreatedBy());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertCategoryMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertCategoryMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public String UpdateCategoryMaster(CategoryMaster objCategoryMaster) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("categoryMaster");
//            stringer.object();
//
//            stringer.key("CategoryMasterId").value(objCategoryMaster.getCategoryMasterId());
//            stringer.key("CategoryName").value(objCategoryMaster.getCategoryName());
//            stringer.key("linktoCategoryMasterIdParent").value(objCategoryMaster.getlinktoCategoryMasterIdParent());
//            stringer.key("ImageNameBytes").value(objCategoryMaster.getImageNameBytes());
//            stringer.key("CategoryColor").value(objCategoryMaster.getCategoryColor());
//            stringer.key("Description").value(objCategoryMaster.getDescription());
//            stringer.key("linktoBusinessMasterId").value(objCategoryMaster.getlinktoBusinessMasterId());
//            stringer.key("SortOrder").value(objCategoryMaster.getSortOrder());
//            stringer.key("SEOPageTitle").value(objCategoryMaster.getSEOPageTitle());
//            stringer.key("SEOMetaDescription").value(objCategoryMaster.getSEOMetaDescription());
//            stringer.key("SEOMetaKeywords").value(objCategoryMaster.getSEOMetaKeywords());
//            stringer.key("IsEnabled").value(objCategoryMaster.getIsEnabled());
//            stringer.key("IsDeleted").value(objCategoryMaster.getIsDeleted());
//            dt = sdfControlDateFormat.parse(objCategoryMaster.getUpdateDateTime());
//            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoUserMasterIdUpdatedBy").value(objCategoryMaster.getlinktoUserMasterIdUpdatedBy());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateCategoryMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateCategoryMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public String DeleteCategoryMaster(short categoryMasterId) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("categoryMasterId").value(categoryMasterId);
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.DeleteCategoryMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.DeleteCategoryMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public CategoryMaster SelectCategoryMaster(short categoryMasterId) {
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectCategoryMaster + "/" + categoryMasterId);
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectCategoryMaster + "Result");
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
//    public ArrayList<CategoryMaster> SelectAllCategoryMasterPageWise(int currentPage) {
//        ArrayList<CategoryMaster> lstCategoryMaster = null;
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectAllCategoryMasterPageWise);
//            if (jsonResponse != null) {
//                JSONArray jsonArray = jsonResponse.getJSONArray(this.SelectAllCategoryMaster + "PageWiseResult");
//                if (jsonArray != null) {
//                    lstCategoryMaster = SetListPropertiesFromJSONArray(jsonArray);
//                }
//            }
//            return lstCategoryMaster;
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
    //endregion

    public void SelectAllCategoryMaster(final Context context, String linktoBusinessMasterId) {
        try {
            String url = Service.Url + this.SelectAllCategoryMaster + "/" + linktoBusinessMasterId;

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObject.getJSONArray(SelectAllCategoryMaster + "Result");
                        if (jsonArray != null) {
                            ArrayList<CategoryMaster> alCategoryMaster = SetListPropertiesFromJSONArray(jsonArray);
                            objCategoryRequestListener = (CategoryRequestListener) context;
                            objCategoryRequestListener.CategoryResponse(alCategoryMaster);
                        }
                    } catch (Exception e) {
                        objCategoryRequestListener = (CategoryRequestListener) context;
                        objCategoryRequestListener.CategoryResponse(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objCategoryRequestListener = (CategoryRequestListener) context;
                    objCategoryRequestListener.CategoryResponse(null);
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            objCategoryRequestListener = (CategoryRequestListener) context;
            objCategoryRequestListener.CategoryResponse(null);
        }
    }

    public interface CategoryRequestListener {
        void CategoryResponse(ArrayList<CategoryMaster> alCategoryMaster);
    }
}
