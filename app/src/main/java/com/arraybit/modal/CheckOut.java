package com.arraybit.modal;

public class CheckOut {

    int orderType;
    int orderTimeIndex;
    int cityIndex;
    CustomerAddressTran objCustomerAddressTran;
    String orderDate;
    String orderTime;
    String name;
    String phone;
    String city;
    String branch;
    short linktoBusinessMasterId;
    OfferMaster objOfferMaster;
    BusinessMaster objBusinessMaster;

    public int getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }

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

    public int getOrderTimeIndex() {
        return orderTimeIndex;
    }

    public void setOrderTimeIndex(int orderTimeIndex) {
        this.orderTimeIndex = orderTimeIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public BusinessMaster getObjBusinessMaster() {
        return objBusinessMaster;
    }

    public void setObjBusinessMaster(BusinessMaster objBusinessMaster) {
        this.objBusinessMaster = objBusinessMaster;
    }

}
