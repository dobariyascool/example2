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
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderJSONParser {

    public String InsertOrderMaster = "InsertOrderMaster";
    public String UpdateOrderMasterStatus = "UpdateOrderMasterStatus";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    OrderMasterRequestListener objOrderMasterRequestListener;

    private OrderMaster SetClassPropertiesFromJSONObject(JSONObject jsonObject) {
        OrderMaster objOrderMaster = null;
        try {
            if (jsonObject != null) {
                objOrderMaster = new OrderMaster();
                objOrderMaster.setOrderMasterId(jsonObject.getLong("OrderMasterId"));
                objOrderMaster.setOrderNumber(jsonObject.getString("OrderNumber"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("OrderDateTime"));
                objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoTableMasterIds(jsonObject.getString("linktoTableMasterIds"));
                if (!jsonObject.getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId(jsonObject.getInt("linktoCustomerMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonObject.getInt("linktoOrderTypeMasterId"));
                if (!jsonObject.getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId((short) jsonObject.getInt("linktoOrderStatusMasterId"));
                }
                if (!jsonObject.getString("linktoBookingMasterId").equals("null")) {
                    objOrderMaster.setlinktoBookingMasterId(jsonObject.getInt("linktoBookingMasterId"));
                }
                objOrderMaster.setTotalAmount(jsonObject.getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonObject.getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonObject.getDouble("Discount"));
                objOrderMaster.setExtraAmount(jsonObject.getDouble("ExtraAmount"));
                objOrderMaster.setNetAmount(jsonObject.getDouble("NetAmount"));
                objOrderMaster.setPaidAmount(jsonObject.getDouble("PaidAmount"));
                objOrderMaster.setBalanceAmount(jsonObject.getDouble("BalanceAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonObject.getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonObject.getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonObject.getString("Remark"));
                objOrderMaster.setIsPreOrder(jsonObject.getBoolean("IsPreOrder"));
                if (!jsonObject.getString("linktoCustomerAddressTranId").equals("null")) {
                    objOrderMaster.setlinktoCustomerAddressTranId(jsonObject.getInt("linktoCustomerAddressTranId"));
                }
                if (!jsonObject.getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonObject.getLong("linktoSalesMasterId"));
                }
                if (!jsonObject.getString("linktoOfferMasterId").equals("null")) {
                    objOrderMaster.setlinktoOfferMasterId(jsonObject.getInt("linktoOfferMasterId"));
                }
                objOrderMaster.setOfferCode(jsonObject.getString("OfferCode"));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                dt = sdfDateTimeFormat.parse(jsonObject.getString("UpdateDateTime"));
                objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));

                /// Extra
                objOrderMaster.setCustomer(jsonObject.getString("Customer"));
                objOrderMaster.setRegisteredUser(jsonObject.getString("RegisteredUser"));
                objOrderMaster.setOrderType(jsonObject.getString("OrderType"));
                objOrderMaster.setOrderStatus(jsonObject.getString("OrderStatus"));
                objOrderMaster.setCustomerAddress(jsonObject.getInt("CustomerAddress"));
                objOrderMaster.setOffer(jsonObject.getString("Offer"));
            }
            return objOrderMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private ArrayList<OrderMaster> SetListPropertiesFromJSONArray(JSONArray jsonArray) {
        ArrayList<OrderMaster> lstOrderMaster = new ArrayList<>();
        OrderMaster objOrderMaster;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                objOrderMaster = new OrderMaster();
                objOrderMaster.setOrderMasterId(jsonArray.getJSONObject(i).getLong("OrderMasterId"));
                objOrderMaster.setOrderNumber(jsonArray.getJSONObject(i).getString("OrderNumber"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("OrderDateTime"));
                objOrderMaster.setOrderDateTime(sdfControlDateFormat.format(dt));
                objOrderMaster.setlinktoTableMasterIds(jsonArray.getJSONObject(i).getString("linktoTableMasterIds"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerMasterId").equals("null")) {
                    objOrderMaster.setlinktoCustomerMasterId(jsonArray.getJSONObject(i).getInt("linktoCustomerMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId((short) jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoBookingMasterId").equals("null")) {
                    objOrderMaster.setlinktoBookingMasterId(jsonArray.getJSONObject(i).getInt("linktoBookingMasterId"));
                }
                objOrderMaster.setTotalAmount(jsonArray.getJSONObject(i).getDouble("TotalAmount"));
                objOrderMaster.setTotalTax(jsonArray.getJSONObject(i).getDouble("TotalTax"));
                objOrderMaster.setDiscount(jsonArray.getJSONObject(i).getDouble("Discount"));
                objOrderMaster.setExtraAmount(jsonArray.getJSONObject(i).getDouble("ExtraAmount"));
                objOrderMaster.setNetAmount(jsonArray.getJSONObject(i).getDouble("NetAmount"));
                objOrderMaster.setPaidAmount(jsonArray.getJSONObject(i).getDouble("PaidAmount"));
                objOrderMaster.setBalanceAmount(jsonArray.getJSONObject(i).getDouble("BalanceAmount"));
                objOrderMaster.setTotalItemPoint((short) jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short) jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
                objOrderMaster.setRemark(jsonArray.getJSONObject(i).getString("Remark"));
                objOrderMaster.setIsPreOrder(jsonArray.getJSONObject(i).getBoolean("IsPreOrder"));
                if (!jsonArray.getJSONObject(i).getString("linktoCustomerAddressTranId").equals("null")) {
                    objOrderMaster.setlinktoCustomerAddressTranId(jsonArray.getJSONObject(i).getInt("linktoCustomerAddressTranId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoSalesMasterId").equals("null")) {
                    objOrderMaster.setlinktoSalesMasterId(jsonArray.getJSONObject(i).getLong("linktoSalesMasterId"));
                }
                if (!jsonArray.getJSONObject(i).getString("linktoOfferMasterId").equals("null")) {
                    objOrderMaster.setlinktoOfferMasterId(jsonArray.getJSONObject(i).getInt("linktoOfferMasterId"));
                }
                objOrderMaster.setOfferCode(jsonArray.getJSONObject(i).getString("OfferCode"));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("CreateDateTime"));
                objOrderMaster.setCreateDateTime(sdfControlDateFormat.format(dt));
                dt = sdfDateTimeFormat.parse(jsonArray.getJSONObject(i).getString("UpdateDateTime"));
                objOrderMaster.setUpdateDateTime(sdfControlDateFormat.format(dt));

                /// Extra
                objOrderMaster.setCustomer(jsonArray.getJSONObject(i).getString("Customer"));
                objOrderMaster.setRegisteredUser(jsonArray.getJSONObject(i).getString("RegisteredUser"));
                objOrderMaster.setOrderType(jsonArray.getJSONObject(i).getString("OrderType"));
                objOrderMaster.setOrderStatus(jsonArray.getJSONObject(i).getString("OrderStatus"));
                objOrderMaster.setCustomerAddress(jsonArray.getJSONObject(i).getInt("CustomerAddress"));
                objOrderMaster.setOffer(jsonArray.getJSONObject(i).getString("Offer"));
                lstOrderMaster.add(objOrderMaster);
            }
            return lstOrderMaster;
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    public void InsertOrderMaster(OrderMaster objOrderMaster, ArrayList<ItemMaster> alOrderItemTran, ArrayList<TaxMaster> alTaxMaster, final Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            //stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("OrderDateTime").value(objOrderMaster.getOrderDateTime());
            stringer.key("linktoBusinessMasterId").value(objOrderMaster.getLinktoBusinessMasterId());
            stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(null);
            stringer.key("TotalAmount").value(objOrderMaster.getTotalAmount());
            stringer.key("TotalTax").value(objOrderMaster.getTotalTax());
            stringer.key("Discount").value(objOrderMaster.getDiscount());
            stringer.key("ExtraAmount").value(objOrderMaster.getExtraAmount());
            stringer.key("NetAmount").value(objOrderMaster.getNetAmount());
            stringer.key("PaidAmount").value(objOrderMaster.getPaidAmount());
            stringer.key("BalanceAmount").value(objOrderMaster.getBalanceAmount());
            stringer.key("TotalItemPoint").value(objOrderMaster.getTotalItemPoint());
            stringer.key("TotalDeductedPoint").value(objOrderMaster.getTotalDeductedPoint());
            stringer.key("Remark").value(objOrderMaster.getRemark());
            stringer.key("IsPreOrder").value(objOrderMaster.getIsPreOrder());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoOfferMasterId").value(objOrderMaster.getlinktoOfferMasterId());
            stringer.key("OfferCode").value(objOrderMaster.getOfferCode());
            stringer.key("linktoCustomerAddressTranId").value(objOrderMaster.getlinktoCustomerAddressTranId());

            stringer.endObject();

            stringer.key("lstOrderItemTran");
            stringer.array();

            for (int i = 0; i < alOrderItemTran.size(); i++) {
                stringer.object();
                stringer.key("ItemMasterId").value(alOrderItemTran.get(i).getItemMasterId());
                stringer.key("ItemName").value(alOrderItemTran.get(i).getItemName());
                stringer.key("ItemCode").value(alOrderItemTran.get(i).getItemCode());
                stringer.key("Quantity").value(alOrderItemTran.get(i).getQuantity());
                stringer.key("Rate").value(alOrderItemTran.get(i).getRate());
                stringer.key("DiscountPercentage").value(0.00);
                stringer.key("DiscountAmount").value(0.00);
                stringer.key("ItemRemark").value(alOrderItemTran.get(i).getRemark());
                stringer.key("Tax1").value(alOrderItemTran.get(i).getTax1());
                stringer.key("Tax2").value(alOrderItemTran.get(i).getTax2());
                stringer.key("Tax3").value(alOrderItemTran.get(i).getTax3());
                stringer.key("Tax4").value(alOrderItemTran.get(i).getTax4());
                stringer.key("Tax5").value(alOrderItemTran.get(i).getTax5());
                stringer.key("Remark").value(alOrderItemTran.get(i).getRemark());
                stringer.key("ItemPoint").value(0);
                stringer.key("DeductedPoint").value(0);
                stringer.key("lstOrderItemModifierTran");
                stringer.array();
                if (alOrderItemTran.get(i).getAlOrderItemModifierTran() != null) {
                    for (int j = 0; j < alOrderItemTran.get(i).getAlOrderItemModifierTran().size(); j++) {
                        stringer.object();
                        stringer.key("ItemMasterId").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getItemMasterId());
                        stringer.key("Rate").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getRate());
                        stringer.endObject();
                    }
                }
                stringer.endArray();
                stringer.endObject();
            }
            stringer.endArray();

            stringer.key("lstTaxMaster");
            stringer.array();

            if(alTaxMaster!=null && alTaxMaster.size()!=0) {
                for (int i = 0; i < alTaxMaster.size(); i++) {
                    stringer.object();
                    stringer.key("TaxMasterId").value(alTaxMaster.get(i).getTaxMasterId());
                    stringer.key("TaxName").value(alTaxMaster.get(i).getTaxName());
                    stringer.key("TaxRate").value(alTaxMaster.get(i).getTaxRate());
                    stringer.key("IsPercentage").value(alTaxMaster.get(i).getIsPercentage());
                    stringer.endObject();
                }
            }
            stringer.endArray();

            stringer.endObject();

            String url = Service.Url + this.InsertOrderMaster;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(InsertOrderMaster + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorCode"));
                            if(targetFragment==null){
                                objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                                objOrderMasterRequestListener.OrderMasterResponse(errorCode, null);
                            }else{
                                objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                                objOrderMasterRequestListener.OrderMasterResponse(errorCode, null);
                            }

                        } else {
                            if(targetFragment==null){
                                objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                                objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                            }else{
                                objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                                objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                            }

                        }
                    } catch (JSONException e) {
                        if(targetFragment==null){
                            objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                        }else{
                            objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(targetFragment==null){
                        objOrderMasterRequestListener = (OrderMasterRequestListener) context;
                        objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                    }else{
                        objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                        objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                    }

                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception ex) {
            objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
        }
    }

    public void UpdateOrderMasterStatus(String orderMasterId, final Context context, final Fragment targetFragment) {
        dt = new Date();
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderMasterId").value(orderMasterId);
            stringer.key("linktoOrderStatusMasterId").value(Globals.OrderStatus.Cancelled.getValue());

            stringer.endObject();

            stringer.endObject();

            String url = Service.Url + this.UpdateOrderMasterStatus;

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(stringer.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONObject jsonResponse = jsonObject.getJSONObject(UpdateOrderMasterStatus + "Result");

                        if (jsonResponse != null) {
                            String errorCode = String.valueOf(jsonResponse.getInt("ErrorNumber"));
                            objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                            objOrderMasterRequestListener.OrderMasterResponse(errorCode, null);
                        } else {
                            objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                        }
                    } catch (JSONException e) {
                        objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                        objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
                    objOrderMasterRequestListener.OrderMasterResponse("-1", null);
                }
            });
            queue.add(jsonObjectRequest);

        } catch (Exception ex) {
            objOrderMasterRequestListener = (OrderMasterRequestListener) targetFragment;
            objOrderMasterRequestListener.OrderMasterResponse("-1", null);
        }
    }



    public interface OrderMasterRequestListener {
        void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster);
    }

}

