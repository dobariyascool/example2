package com.arraybit.parser;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemJSONParser {
    public String SelectAllItemMaster = "SelectAllItemMasterByCategoryMasterId";
    public String SelectAllItemModifier = "SelectAllItemModifier";
    public String SelectAllItemSuggested = "SelectAllItemSuggested";
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    ItemMasterRequestListener objItemMasterRequestListener;

    //region Class Methods
    private ItemMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        ItemMaster objItemMaster = null;
        try {
            if (jsonObject != null) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonObject.getInt("ItemMasterId"));
                objItemMaster.setShortName(jsonObject.getString("ShortName"));
                objItemMaster.setItemName(jsonObject.getString("ItemName"));
                objItemMaster.setItemCode(jsonObject.getString("ItemCode"));
                objItemMaster.setBarCode(jsonObject.getString("BarCode"));
                objItemMaster.setShortDescription(jsonObject.getString("ShortDescription"));
                if (!jsonObject.getString("linktoUnitMasterId").equals("null")) {
                    objItemMaster.setlinktoUnitMasterId((short) jsonObject.getInt("linktoUnitMasterId"));
                }
                objItemMaster.setlinktoCategoryMasterId((short) jsonObject.getInt("linktoCategoryMasterId"));
                if (!jsonObject.getString("IsFavourite").equals("null")) {
                    objItemMaster.setIsFavourite(jsonObject.getBoolean("IsFavourite"));
                }
                objItemMaster.setXs_ImagePhysicalName(jsonObject.getString("xs_ImagePhysicalName"));
                objItemMaster.setSm_ImagePhysicalName(jsonObject.getString("sm_ImagePhysicalName"));
                objItemMaster.setLg_ImagePhysicalName(jsonObject.getString("lg_ImagePhysicalName"));
                objItemMaster.setXl_ImagePhysicalName(jsonObject.getString("xl_ImagePhysicalName"));
                objItemMaster.setMd_ImagePhysicalName(jsonObject.getString("md_ImagePhysicalName"));
                objItemMaster.setItemPoint((short) jsonObject.getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonObject.getInt("PriceByPoint"));
                objItemMaster.setSearchWords(jsonObject.getString("SearchWords"));
                objItemMaster.setlinktoBusinessMasterId((short) jsonObject.getInt("linktoBusinessMasterId"));
                if (!jsonObject.getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonObject.getInt("SortOrder"));
                }
                objItemMaster.setIsEnabled(jsonObject.getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonObject.getBoolean("IsDeleted"));
                objItemMaster.setIsDineInOnly(jsonObject.getBoolean("IsDineInOnly"));
                objItemMaster.setItemType((short) jsonObject.getInt("ItemType"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonObject.getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonObject.getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonObject.getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setRate(jsonObject.getDouble("Rate"));
                objItemMaster.setMRP(jsonObject.getDouble("MRP"));

                /// Extra
                objItemMaster.setUnit(jsonObject.getString("Unit"));
                objItemMaster.setCategory(jsonObject.getString("Category"));
                objItemMaster.setBusiness(jsonObject.getString("Business"));
                objItemMaster.setUserCreatedBy(jsonObject.getString("UserCreatedBy"));
                objItemMaster.setUserUpdatedBy(jsonObject.getString("UserUpdatedBy"));
                objItemMaster.setLinktoItemMasterIdModifiers(jsonObject.getString("linktoItemMasterIdModifiers"));
                objItemMaster.setLinktoOptionMasterIds(jsonObject.getString("linktoOptionMasterIds"));
                objItemMaster.setIsChecked((short) -1);
                objItemMaster.setIsDeleted(false);
            }
            return objItemMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<ItemMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemMaster> lstItemMaster = new ArrayList<>();
        ItemMaster objItemMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("ItemMasterId"));
                objItemMaster.setShortName(jsonArray.getJSONObject(i).getString("ShortName"));
                objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("ItemName"));
                objItemMaster.setItemCode(jsonArray.getJSONObject(i).getString("ItemCode"));
                objItemMaster.setBarCode(jsonArray.getJSONObject(i).getString("BarCode"));
                objItemMaster.setShortDescription(jsonArray.getJSONObject(i).getString("ShortDescription"));
                if (!jsonArray.getJSONObject(i).getString("linktoUnitMasterId").equals("null")) {
                    objItemMaster.setlinktoUnitMasterId((short) jsonArray.getJSONObject(i).getInt("linktoUnitMasterId"));
                }
                objItemMaster.setlinktoCategoryMasterId((short) jsonArray.getJSONObject(i).getInt("linktoCategoryMasterId"));
                if (!jsonArray.getJSONObject(i).getString("IsFavourite").equals("null")) {
                    objItemMaster.setIsFavourite(jsonArray.getJSONObject(i).getBoolean("IsFavourite"));
                }
                objItemMaster.setXs_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xs_ImagePhysicalName"));
                objItemMaster.setSm_ImagePhysicalName(jsonArray.getJSONObject(i).getString("sm_ImagePhysicalName"));
                objItemMaster.setLg_ImagePhysicalName(jsonArray.getJSONObject(i).getString("lg_ImagePhysicalName"));
                objItemMaster.setXl_ImagePhysicalName(jsonArray.getJSONObject(i).getString("xl_ImagePhysicalName"));
                objItemMaster.setMd_ImagePhysicalName(jsonArray.getJSONObject(i).getString("md_ImagePhysicalName"));
                objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objItemMaster.setPriceByPoint((short) jsonArray.getJSONObject(i).getInt("PriceByPoint"));
                objItemMaster.setSearchWords(jsonArray.getJSONObject(i).getString("SearchWords"));
                objItemMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                if (!jsonArray.getJSONObject(i).getString("SortOrder").equals("null")) {
                    objItemMaster.setSortOrder(jsonArray.getJSONObject(i).getInt("SortOrder"));
                }
                objItemMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objItemMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objItemMaster.setIsDineInOnly(jsonArray.getJSONObject(i).getBoolean("IsDineInOnly"));
                objItemMaster.setItemType((short) jsonArray.getJSONObject(i).getInt("ItemType"));
//                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
//                objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                objItemMaster.setlinktoUserMasterIdCreatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdCreatedBy"));
                if (!jsonArray.getJSONObject(i).getString("linktoUserMasterIdUpdatedBy").equals("null")) {
                    objItemMaster.setlinktoUserMasterIdUpdatedBy((short) jsonArray.getJSONObject(i).getInt("linktoUserMasterIdUpdatedBy"));
                }
                objItemMaster.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                objItemMaster.setMRP(jsonArray.getJSONObject(i).getDouble("MRP"));
                objItemMaster.setTax(jsonArray.getJSONObject(i).getString("Tax"));

                /// Extra
                objItemMaster.setUnit(jsonArray.getJSONObject(i).getString("Unit"));
                objItemMaster.setCategory(jsonArray.getJSONObject(i).getString("Category"));
                objItemMaster.setBusiness(jsonArray.getJSONObject(i).getString("Business"));
                objItemMaster.setUserCreatedBy(jsonArray.getJSONObject(i).getString("UserCreatedBy"));
                objItemMaster.setUserUpdatedBy(jsonArray.getJSONObject(i).getString("UserUpdatedBy"));
                objItemMaster.setLinktoItemMasterIdModifiers(jsonArray.getJSONObject(i).getString("linktoItemMasterIdModifiers"));
                objItemMaster.setLinktoOptionMasterIds(jsonArray.getJSONObject(i).getString("linktoOptionMasterIds"));
                objItemMaster.setIsChecked((short) -1);
                objItemMaster.setIsDeleted(false);
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        }
    }
    //endregion

    //region Commented Codes
//    public String InsertItemMaster(ItemMaster objItemMaster) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("itemMaster");
//            stringer.object();
//
//            stringer.key("ShortName").value(objItemMaster.getShortName());
//            stringer.key("ItemName").value(objItemMaster.getItemName());
//            stringer.key("ItemCode").value(objItemMaster.getItemCode());
//            stringer.key("BarCode").value(objItemMaster.getBarCode());
//            stringer.key("ShortDescription").value(objItemMaster.getShortDescription());
//            stringer.key("linktoUnitMasterId").value(objItemMaster.getlinktoUnitMasterId());
//            stringer.key("linktoCategoryMasterId").value(objItemMaster.getlinktoCategoryMasterId());
//            stringer.key("IsFavourite").value(objItemMaster.getIsFavourite());
//            stringer.key("ImageNameBytes").value(objItemMaster.getImageNameBytes());
//            stringer.key("linktoItemStatusMasterId").value(objItemMaster.getlinktoItemStatusMasterId());
//            stringer.key("ItemPoint").value(objItemMaster.getItemPoint());
//            stringer.key("PriceByPoint").value(objItemMaster.getPriceByPoint());
//            stringer.key("SearchWords").value(objItemMaster.getSearchWords());
//            stringer.key("linktoBusinessMasterId").value(objItemMaster.getlinktoBusinessMasterId());
//            stringer.key("SortOrder").value(objItemMaster.getSortOrder());
//            stringer.key("IsEnabled").value(objItemMaster.getIsEnabled());
//            stringer.key("IsDeleted").value(objItemMaster.getIsDeleted());
//            stringer.key("IsDineInOnly").value(objItemMaster.getIsDineInOnly());
//            stringer.key("ItemType").value(objItemMaster.getItemType());
//            dt = sdfControlDateFormat.parse(objItemMaster.getCreateDateTime());
//            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoUserMasterIdCreatedBy").value(objItemMaster.getlinktoUserMasterIdCreatedBy());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertItemMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertItemMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public String UpdateItemMaster(ItemMaster objItemMaster) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("itemMaster");
//            stringer.object();
//
//            stringer.key("ItemMasterId").value(objItemMaster.getItemMasterId());
//            stringer.key("ShortName").value(objItemMaster.getShortName());
//            stringer.key("ItemName").value(objItemMaster.getItemName());
//            stringer.key("ItemCode").value(objItemMaster.getItemCode());
//            stringer.key("BarCode").value(objItemMaster.getBarCode());
//            stringer.key("ShortDescription").value(objItemMaster.getShortDescription());
//            stringer.key("linktoUnitMasterId").value(objItemMaster.getlinktoUnitMasterId());
//            stringer.key("linktoCategoryMasterId").value(objItemMaster.getlinktoCategoryMasterId());
//            stringer.key("IsFavourite").value(objItemMaster.getIsFavourite());
//            stringer.key("ImageNameBytes").value(objItemMaster.getImageNameBytes());
//            stringer.key("linktoItemStatusMasterId").value(objItemMaster.getlinktoItemStatusMasterId());
//            stringer.key("ItemPoint").value(objItemMaster.getItemPoint());
//            stringer.key("PriceByPoint").value(objItemMaster.getPriceByPoint());
//            stringer.key("SearchWords").value(objItemMaster.getSearchWords());
//            stringer.key("linktoBusinessMasterId").value(objItemMaster.getlinktoBusinessMasterId());
//            stringer.key("SortOrder").value(objItemMaster.getSortOrder());
//            stringer.key("IsEnabled").value(objItemMaster.getIsEnabled());
//            stringer.key("IsDeleted").value(objItemMaster.getIsDeleted());
//            stringer.key("IsDineInOnly").value(objItemMaster.getIsDineInOnly());
//            stringer.key("ItemType").value(objItemMaster.getItemType());
//            dt = sdfControlDateFormat.parse(objItemMaster.getUpdateDateTime());
//            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));
//            stringer.key("linktoUserMasterIdUpdatedBy").value(objItemMaster.getlinktoUserMasterIdUpdatedBy());
//
//            stringer.endObject();
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateItemMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateItemMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public String DeleteItemMaster(int itemMasterId) {
//        try {
//            JSONStringer stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("itemMasterId").value(itemMasterId);
//
//            stringer.endObject();
//
//            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.DeleteItemMaster, stringer);
//            JSONObject jsonObject = jsonResponse.getJSONObject(this.DeleteItemMaster + "Result");
//            return String.valueOf(jsonObject.getInt("ErrorCode"));
//        }
//        catch (Exception ex) {
//            return "-1";
//        }
//    }
//
//    public ItemMaster SelectItemMaster(int itemMasterId) {
//        try {
//            JSONObject jsonResponse = Service.HttpGetService(Service.Url + this.SelectItemMaster + "/" + itemMasterId);
//            if (jsonResponse != null) {
//                JSONObject jsonObject = jsonResponse.getJSONObject(this.SelectItemMaster + "Result");
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
    //endregion

    public void SelectAllItemMaster(final Fragment targetFragment, Context context, String currentPage, String categoryMasterId, String optionMasterId, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllItemMaster + "/" + currentPage + "/" + categoryMasterId + "/" + optionMasterId + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllItemMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                objItemMasterRequestListener.ItemMasterResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public void SelectAllItemModifier(final Fragment targetFragment, Context context, String linktoItemMasterId, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllItemModifier + "/" + linktoItemMasterId + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllItemModifier + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                objItemMasterRequestListener.ItemMasterResponse(null);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public void SelectAllItemSuggested(final Context context, String linktoItemMasterId, String linktoBusinessMasterId) {
        String url = Service.Url + this.SelectAllItemSuggested + "/" + linktoItemMasterId + "/" + linktoBusinessMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllItemSuggested + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                        objItemMasterRequestListener = (ItemMasterRequestListener) context;
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) context;
                    objItemMasterRequestListener.ItemMasterResponse(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) context;
                objItemMasterRequestListener.ItemMasterResponse(null);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface ItemMasterRequestListener {
        void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster);
    }
}