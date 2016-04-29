package com.arraybit.modal;

public class CheckOut {

    int orderType;
    CustomerAddressTran objCustomerAddressTran;
    String orderDate;
    String orderTime;
    OfferMaster objOfferMaster;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public CustomerAddressTran getObjCustomerAddressTran() {
        return objCustomerAddressTran;
    }

    public void setObjCustomerAddressTran(CustomerAddressTran objCustomerAddressTran) {
        this.objCustomerAddressTran = objCustomerAddressTran;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public OfferMaster getObjOfferMaster() {
        return objOfferMaster;
    }

    public void setObjOfferMaster(OfferMaster objOfferMaster) {
        this.objOfferMaster = objOfferMaster;
    }
}
