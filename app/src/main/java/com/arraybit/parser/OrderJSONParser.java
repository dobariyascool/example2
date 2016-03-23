package com.arraybit.parser;


import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;

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
    public String UpdateOrderMaster = "UpdateOrderMaster";

    SimpleDateFormat sdfControlDateFormat = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    Date dt = null;
    SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

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
                if (!jsonObject.getString("linktoRegisteredUserMasterId").equals("null")) {
                    objOrderMaster.setlinktoRegisteredUserMasterId(jsonObject.getInt("linktoRegisteredUserMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short)jsonObject.getInt("linktoOrderTypeMasterId"));
                if (!jsonObject.getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId((short)jsonObject.getInt("linktoOrderStatusMasterId"));
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
                objOrderMaster.setTotalItemPoint((short)jsonObject.getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short)jsonObject.getInt("TotalDeductedPoint"));
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
                if (!jsonArray.getJSONObject(i).getString("linktoRegisteredUserMasterId").equals("null")) {
                    objOrderMaster.setlinktoRegisteredUserMasterId(jsonArray.getJSONObject(i).getInt("linktoRegisteredUserMasterId"));
                }
                objOrderMaster.setlinktoOrderTypeMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOrderTypeMasterId"));
                if (!jsonArray.getJSONObject(i).getString("linktoOrderStatusMasterId").equals("null")) {
                    objOrderMaster.setlinktoOrderStatusMasterId((short)jsonArray.getJSONObject(i).getInt("linktoOrderStatusMasterId"));
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
                objOrderMaster.setTotalItemPoint((short)jsonArray.getJSONObject(i).getInt("TotalItemPoint"));
                objOrderMaster.setTotalDeductedPoint((short)jsonArray.getJSONObject(i).getInt("TotalDeductedPoint"));
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

    public String InsertOrderMaster(OrderMaster objOrderMaster,ArrayList<ItemMaster> alOrderItemTran) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderNumber").value(objOrderMaster.getOrderNumber());
            stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            //stringer.key("linktoTableMasterIds").value(objOrderMaster.getlinktoTableMasterIds());
            //stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            //stringer.key("linktoBookingMasterId").value(objOrderMaster.getlinktoBookingMasterId());
            stringer.key("linktoRegisteredUserMasterId").value(objOrderMaster.getlinktoRegisteredUserMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(null);
            //stringer.key("linktoBookingMasterId").value(objOrderMaster.getlinktoBookingMasterId());
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
            //stringer.key("linktoCustomerAddressTranId").value(objOrderMaster.getlinktoCustomerAddressTranId());
            //stringer.key("linktoSalesMasterId").value(objOrderMaster.getlinktoSalesMasterId());
            //stringer.key("linktoOfferMasterId").value(objOrderMaster.getlinktoOfferMasterId());
            //stringer.key("OfferCode").value(objOrderMaster.getOfferCode());
            //dt = sdfControlDateFormat.parse(objOrderMaster.getCreateDateTime());
            stringer.key("CreateDateTime").value(sdfDateTimeFormat.format(dt));

            stringer.endObject();

            stringer.key("lstOrderItemTran");
            stringer.array();

            for(int i=0;i<alOrderItemTran.size();i++) {
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
                stringer.key("ItemRemark").value(alOrderItemTran.get(i).getRemark());
                stringer.key("ItemPoint").value(0);
                stringer.key("DeductedPoint").value(0);
                stringer.key("lstOrderItemModifierTran");
                stringer.array();
                for(int j=0;j<alOrderItemTran.get(i).getAlOrderItemModifierTran().size();j++){
                    stringer.object();
                    stringer.key("ItemModifierMasterIds").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getItemMasterId());
                    stringer.key("Rate").value(alOrderItemTran.get(i).getAlOrderItemModifierTran().get(j).getRate());
                    stringer.endObject();
                }
                stringer.endArray();
                stringer.endObject();
            }
            stringer.endArray();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.InsertOrderMaster, stringer);
            if(jsonResponse!=null) {
                JSONObject jsonObject = jsonResponse.getJSONObject(this.InsertOrderMaster + "Result");
                if(jsonObject.getInt("ErrorCode")==0){
                    return String.valueOf(jsonObject.getLong("ErrorNumber"));
                }
                else{
                    return String.valueOf(jsonObject.getInt("ErrorCode"));
                }
            }
            return "-1";
        }
        catch (Exception ex) {
            return "-1";
        }
    }

    public String UpdateOrderMaster(OrderMaster objOrderMaster) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();

            stringer.key("orderMaster");
            stringer.object();

            stringer.key("OrderMasterId").value(objOrderMaster.getOrderMasterId());
            stringer.key("OrderNumber").value(objOrderMaster.getOrderNumber());
            dt = sdfControlDateFormat.parse(objOrderMaster.getOrderDateTime());
            stringer.key("OrderDateTime").value(sdfDateTimeFormat.format(dt));
            stringer.key("linktoTableMasterIds").value(objOrderMaster.getlinktoTableMasterIds());
            stringer.key("linktoCustomerMasterId").value(objOrderMaster.getlinktoCustomerMasterId());
            stringer.key("linktoRegisteredUserMasterId").value(objOrderMaster.getlinktoRegisteredUserMasterId());
            stringer.key("linktoOrderTypeMasterId").value(objOrderMaster.getlinktoOrderTypeMasterId());
            stringer.key("linktoOrderStatusMasterId").value(objOrderMaster.getlinktoOrderStatusMasterId());
            stringer.key("linktoBookingMasterId").value(objOrderMaster.getlinktoBookingMasterId());
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
            stringer.key("linktoCustomerAddressTranId").value(objOrderMaster.getlinktoCustomerAddressTranId());
            stringer.key("linktoSalesMasterId").value(objOrderMaster.getlinktoSalesMasterId());
            stringer.key("linktoOfferMasterId").value(objOrderMaster.getlinktoOfferMasterId());
            stringer.key("OfferCode").value(objOrderMaster.getOfferCode());
            dt = sdfControlDateFormat.parse(objOrderMaster.getUpdateDateTime());
            stringer.key("UpdateDateTime").value(sdfDateTimeFormat.format(dt));

            stringer.endObject();

            stringer.endObject();

            JSONObject jsonResponse = Service.HttpPostService(Service.Url + this.UpdateOrderMaster, stringer);
            JSONObject jsonObject = jsonResponse.getJSONObject(this.UpdateOrderMaster + "Result");
            return String.valueOf(jsonObject.getInt("ErrorCode"));
        }
        catch (Exception ex) {
            return "-1";
        }
    }
}

