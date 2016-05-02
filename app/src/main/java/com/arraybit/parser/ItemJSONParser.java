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
    public String SelectAllOrderMasterOrderItem = "SelectAllOrderMasterWithOrderItem";
    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfControlTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);
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
                objItemMaster.setTaxRate(jsonObject.getDouble("TaxRate"));
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
                objItemMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));

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

    public ArrayList<ItemMaster> SetListSelectedPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<ItemMaster> lstItemMaster = new ArrayList<>();
        ItemMaster objItemMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objItemMaster = new ItemMaster();
                objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("ItemName"));
                objItemMaster.setTax(jsonArray.getJSONObject(i).getString("Tax"));
                objItemMaster.setIsChecked((short) jsonArray.getJSONObject(i).getInt("IsChecked"));
                objItemMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objItemMaster.setMRP(jsonArray.getJSONObject(i).getDouble("MRP"));
                objItemMaster.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                objItemMaster.setSellPrice(jsonArray.getJSONObject(i).getDouble("SellPrice"));
                objItemMaster.setTax1(jsonArray.getJSONObject(i).getDouble("Tax1"));
                objItemMaster.setTax2(jsonArray.getJSONObject(i).getDouble("Tax2"));
                objItemMaster.setTax3(jsonArray.getJSONObject(i).getDouble("Tax3"));
                objItemMaster.setTax4(jsonArray.getJSONObject(i).getDouble("Tax4"));
                objItemMaster.setTax5(jsonArray.getJSONObject(i).getDouble("Tax5"));
                objItemMaster.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));
                objItemMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objItemMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objItemMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objItemMaster.setIsDeleted(jsonArray.getJSONObject(i).getBoolean("IsDeleted"));
                objItemMaster.setIsDineInOnly(jsonArray.getJSONObject(i).getBoolean("IsDineInOnly"));
                objItemMaster.setIsEnabled(jsonArray.getJSONObject(i).getBoolean("IsEnabled"));
                objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("ItemMasterId"));
                objItemMaster.setItemType((short) jsonArray.getJSONObject(i).getInt("ItemType"));
                objItemMaster.setPaymentStatus(jsonArray.getJSONObject(i).getBoolean("PaymentStatus"));
                objItemMaster.setPriceByPoint((short) jsonArray.getJSONObject(i).getInt("PriceByPoint"));
                objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                objItemMaster.setQuantity(jsonArray.getJSONObject(i).getInt("Quantity"));
                objItemMaster.setType((short) jsonArray.getJSONObject(i).getInt("Type"));
                objItemMaster.setlinktoBusinessMasterId((short) jsonArray.getJSONObject(i).getInt("linktoBusinessMasterId"));
                JSONArray objArrayModifier = jsonArray.getJSONObject(i).getJSONArray("alOrderItemModifierTran");
                ArrayList<ItemMaster> alOrderItemModifierTran = null;
                ItemMaster objItemModifier;
                if(objArrayModifier!=null){
                    alOrderItemModifierTran = new ArrayList<>();
                    for(int j=0;j<objArrayModifier.length();j++){
                        objItemModifier = new ItemMaster();
                        objItemModifier.setItemName(jsonArray.getJSONObject(i).getString("ItemName"));
                        objItemModifier.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                        objItemModifier.setMRP(jsonArray.getJSONObject(i).getDouble("MRP"));
                        objItemModifier.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                        objItemModifier.setSellPrice(jsonArray.getJSONObject(i).getDouble("SellPrice"));
                        objItemModifier.setTax1(jsonArray.getJSONObject(i).getDouble("Tax1"));
                        objItemModifier.setTax2(jsonArray.getJSONObject(i).getDouble("Tax2"));
                        objItemModifier.setTax3(jsonArray.getJSONObject(i).getDouble("Tax3"));
                        objItemModifier.setTax4(jsonArray.getJSONObject(i).getDouble("Tax4"));
                        objItemModifier.setTax5(jsonArray.getJSONObject(i).getDouble("Tax5"));
                        objItemModifier.setTaxRate(jsonArray.getJSONObject(i).getDouble("TaxRate"));
                        objItemModifier.setItemMasterId(jsonArray.getJSONObject(i).getInt("ItemMasterId"));
                        objItemModifier.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                        alOrderItemModifierTran.add(objItemModifier);
                    }
                }
                objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
                lstItemMaster.add(objItemMaster);
            }
            return lstItemMaster;
        } catch (JSONException e) {
            return null;
        }
    }
    //endregion

    public void SelectAllItemMaster(final Fragment targetFragment, final Context context, final String currentPage, String categoryMasterId, String optionMasterId, String linktoBusinessMasterId,String itemMasterIds, final boolean isOptionFilter) {
         String url = Service.Url + this.SelectAllItemMaster + "/" + currentPage + "/" + categoryMasterId + "/" + optionMasterId + "/" + linktoBusinessMasterId + "/" + itemMasterIds;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllItemMaster + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = SetListPropertiesFromJSONArray(jsonArray);
                        if(targetFragment==null){
                            objItemMasterRequestListener = (ItemMasterRequestListener) context;
                            objItemMasterRequestListener.ItemMasterResponse(alItemMaster, isOptionFilter);

                        }else {
                            objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                            objItemMasterRequestListener.ItemMasterResponse(alItemMaster, isOptionFilter);
                        }
                    }
                } catch (Exception e) {
                    if(targetFragment==null){
                        objItemMasterRequestListener = (ItemMasterRequestListener) context;
                        objItemMasterRequestListener.ItemMasterResponse(null,false);
                    }else {
                        objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                        objItemMasterRequestListener.ItemMasterResponse(null,false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(targetFragment==null){
                    objItemMasterRequestListener = (ItemMasterRequestListener) context;
                    objItemMasterRequestListener.ItemMasterResponse(null,false);
                }else {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null,false);
                }
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
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster,false);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null,false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                objItemMasterRequestListener.ItemMasterResponse(null,false);
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
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster,false);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) context;
                    objItemMasterRequestListener.ItemMasterResponse(null,false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) context;
                objItemMasterRequestListener.ItemMasterResponse(null,false);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void SelectAllOrderMasterOrderItem(final Fragment targetFragment, Context context,String currentPage,String linktoBusinessMasterId, String linktoCustomerMasterId) {
        String url = Service.Url + this.SelectAllOrderMasterOrderItem + "/" + currentPage + "/" + linktoBusinessMasterId + "/" + linktoCustomerMasterId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                Date dt;
                try {
                    jsonArray = jsonObject.getJSONArray(SelectAllOrderMasterOrderItem + "Result");
                    if (jsonArray != null) {
                        ArrayList<ItemMaster> alItemMaster = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ItemMaster objItemMaster = new ItemMaster();
                            objItemMaster.setItemMasterId(jsonArray.getJSONObject(i).getInt("linktoItemMasterId"));
                            objItemMaster.setItemName(jsonArray.getJSONObject(i).getString("Item"));
                            objItemMaster.setItemCode(jsonArray.getJSONObject(i).getString("ItemCode"));
                            objItemMaster.setItemPoint((short) jsonArray.getJSONObject(i).getInt("ItemPoint"));
                            objItemMaster.setRate(jsonArray.getJSONObject(i).getDouble("Rate"));
                            objItemMaster.setSellPrice(jsonArray.getJSONObject(i).getDouble("TotalRate"));
                            objItemMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                            objItemMaster.setQuantity(jsonArray.getJSONObject(i).getInt("Quantity"));
                            objItemMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                            objItemMaster.setRemark(jsonArray.getJSONObject(i).getString("ItemRemark"));
                            objItemMaster.setLinktoItemMasterIdModifiers(jsonArray.getJSONObject(i).getString("linktoItemMasterModifierId"));
                            objItemMaster.setLinktoOrderMasterId(jsonArray.getJSONObject(i).getInt("linktoOrderMasterId"));
                            objItemMaster.setLinktoOrderItemTranId(jsonArray.getJSONObject(i).getInt("OrderItemTranId"));
                            objItemMaster.setOrderNumber(jsonArray.getJSONObject(i).getString("OrderNumber"));
                            objItemMaster.setPaymentStatus(jsonArray.getJSONObject(i).getBoolean("PaymentStatus"));
                            dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("OrderDateTime"));
                            objItemMaster.setCreateDateTime(sdfControlDateFormat.format(dt) + "T" + sdfControlTimeFormat.format(dt));
                            if(!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")){
                                objItemMaster.setLinktoOrderStatusMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                            }
                            alItemMaster.add(objItemMaster);
                        }
                        objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                        objItemMasterRequestListener.ItemMasterResponse(alItemMaster,false);
                    }
                } catch (Exception e) {
                    objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                    objItemMasterRequestListener.ItemMasterResponse(null,false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                objItemMasterRequestListener = (ItemMasterRequestListener) targetFragment;
                objItemMasterRequestListener.ItemMasterResponse(null,false);
            }

        });
        queue.add(jsonObjectRequest);
    }

    public interface ItemMasterRequestListener {
        void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster,boolean isFilter);
    }
}