package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderMaster implements Parcelable {

    //region Properties
    long OrderMasterId;
    String OrderNumber;
    String OrderDateTime;
    String linktoTableMasterIds;
    int linktoCustomerMasterId;
    short linktoOrderTypeMasterId;
    short linktoOrderStatusMasterId;
    short linktoBusinessMasterId;
    int linktoBookingMasterId;
    double TotalAmount;
    double TotalTax;
    double Discount;
    double ExtraAmount;
    double NetAmount;
    double PaidAmount;
    double BalanceAmount;
    short TotalItemPoint;
    short TotalDeductedPoint;
    String Remark;
    boolean IsPreOrder;
    int linktoCustomerAddressTranId;
    long linktoSalesMasterId;
    int linktoOfferMasterId;
    String OfferCode;
    String CreateDateTime;
    String UpdateDateTime;
    /// Extra
    String Customer;
    String RegisteredUser;
    String OrderType;
    String OrderStatus;
    int CustomerAddress;
    String Offer;
    public static final Creator<OrderMaster> CREATOR = new Creator<OrderMaster>() {
        public OrderMaster createFromParcel(Parcel source) {
            OrderMaster objOrderMaster = new OrderMaster();
            objOrderMaster.OrderMasterId = source.readLong();
            objOrderMaster.OrderNumber = source.readString();
            objOrderMaster.OrderDateTime = source.readString();
            objOrderMaster.linktoTableMasterIds = source.readString();
            objOrderMaster.linktoCustomerMasterId = source.readInt();
            objOrderMaster.linktoOrderTypeMasterId = (short) source.readInt();
            objOrderMaster.linktoOrderStatusMasterId = (short) source.readInt();
            objOrderMaster.linktoBusinessMasterId = (short) source.readInt();
            objOrderMaster.linktoBookingMasterId = source.readInt();
            objOrderMaster.TotalAmount = source.readDouble();
            objOrderMaster.TotalTax = source.readDouble();
            objOrderMaster.Discount = source.readDouble();
            objOrderMaster.ExtraAmount = source.readDouble();
            objOrderMaster.NetAmount = source.readDouble();
            objOrderMaster.PaidAmount = source.readDouble();
            objOrderMaster.BalanceAmount = source.readDouble();
            objOrderMaster.TotalItemPoint = (short) source.readInt();
            objOrderMaster.TotalDeductedPoint = (short) source.readInt();
            objOrderMaster.Remark = source.readString();
            objOrderMaster.IsPreOrder = source.readByte() != 0;
            objOrderMaster.linktoCustomerAddressTranId = source.readInt();
            objOrderMaster.linktoSalesMasterId = source.readLong();
            objOrderMaster.linktoOfferMasterId = source.readInt();
            objOrderMaster.OfferCode = source.readString();
            objOrderMaster.CreateDateTime = source.readString();
            objOrderMaster.UpdateDateTime = source.readString();

            /// Extra
            objOrderMaster.Customer = source.readString();
            objOrderMaster.RegisteredUser = source.readString();
            objOrderMaster.OrderType = source.readString();
            objOrderMaster.OrderStatus = source.readString();
            objOrderMaster.CustomerAddress = source.readInt();
            objOrderMaster.Offer = source.readString();
            return objOrderMaster;
        }

        public OrderMaster[] newArray(int size) {
            return new OrderMaster[size];
        }
    };
    short Type;
    ArrayList<ItemMaster> alOrderItemTran;

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public long getOrderMasterId() {
        return this.OrderMasterId;
    }

    public void setOrderMasterId(long orderMasterId) {
        this.OrderMasterId = orderMasterId;
    }

    public String getOrderNumber() {
        return this.OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.OrderNumber = orderNumber;
    }

    public String getOrderDateTime() {
        return this.OrderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.OrderDateTime = orderDateTime;
    }

    public String getlinktoTableMasterIds() {
        return this.linktoTableMasterIds;
    }

    public void setlinktoTableMasterIds(String linktoTableMasterIds) {
        this.linktoTableMasterIds = linktoTableMasterIds;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getlinktoOrderTypeMasterId() {
        return this.linktoOrderTypeMasterId;
    }

    public void setlinktoOrderTypeMasterId(short linktoOrderTypeMasterId) {
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    public short getlinktoOrderStatusMasterId() {
        return this.linktoOrderStatusMasterId;
    }

    public void setlinktoOrderStatusMasterId(short linktoOrderStatusMasterId) {
        this.linktoOrderStatusMasterId = linktoOrderStatusMasterId;
    }

    public int getlinktoBookingMasterId() {
        return this.linktoBookingMasterId;
    }

    public void setlinktoBookingMasterId(int linktoBookingMasterId) {
        this.linktoBookingMasterId = linktoBookingMasterId;
    }

    public double getTotalAmount() {
        return this.TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.TotalAmount = totalAmount;
    }

    public double getTotalTax() {
        return this.TotalTax;
    }

    public void setTotalTax(double totalTax) {
        this.TotalTax = totalTax;
    }

    public double getDiscount() {
        return this.Discount;
    }

    public void setDiscount(double discount) {
        this.Discount = discount;
    }

    public double getExtraAmount() {
        return this.ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        this.ExtraAmount = extraAmount;
    }

    public double getNetAmount() {
        return this.NetAmount;
    }

    public void setNetAmount(double netAmount) {
        this.NetAmount = netAmount;
    }

    public double getPaidAmount() {
        return this.PaidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.PaidAmount = paidAmount;
    }

    public double getBalanceAmount() {
        return this.BalanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.BalanceAmount = balanceAmount;
    }

    public short getTotalItemPoint() {
        return this.TotalItemPoint;
    }

    public void setTotalItemPoint(short totalItemPoint) {
        this.TotalItemPoint = totalItemPoint;
    }

    public short getTotalDeductedPoint() {
        return this.TotalDeductedPoint;
    }

    public void setTotalDeductedPoint(short totalDeductedPoint) {
        this.TotalDeductedPoint = totalDeductedPoint;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public boolean getIsPreOrder() {
        return this.IsPreOrder;
    }

    public void setIsPreOrder(boolean isPreOrder) {
        this.IsPreOrder = isPreOrder;
    }

    public int getlinktoCustomerAddressTranId() {
        return this.linktoCustomerAddressTranId;
    }

    public void setlinktoCustomerAddressTranId(int linktoCustomerAddressTranId) {
        this.linktoCustomerAddressTranId = linktoCustomerAddressTranId;
    }

    public long getlinktoSalesMasterId() {
        return this.linktoSalesMasterId;
    }

    public void setlinktoSalesMasterId(long linktoSalesMasterId) {
        this.linktoSalesMasterId = linktoSalesMasterId;
    }

    public int getlinktoOfferMasterId() {
        return this.linktoOfferMasterId;
    }

    public void setlinktoOfferMasterId(int linktoOfferMasterId) {
        this.linktoOfferMasterId = linktoOfferMasterId;
    }

    public String getOfferCode() {
        return this.OfferCode;
    }

    public void setOfferCode(String offerCode) {
        this.OfferCode = offerCode;
    }

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public String getCustomer() {
        return this.Customer;
    }

    public void setCustomer(String customer) {
        this.Customer = customer;
    }

    public String getRegisteredUser() {
        return this.RegisteredUser;
    }

    public void setRegisteredUser(String registeredUser) {
        this.RegisteredUser = registeredUser;
    }

    public String getOrderType() {
        return this.OrderType;
    }

    public void setOrderType(String orderType) {
        this.OrderType = orderType;
    }

    public String getOrderStatus() {
        return this.OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.OrderStatus = orderStatus;
    }

    public int getCustomerAddress() {
        return this.CustomerAddress;
    }

    public void setCustomerAddress(int customerAddress) {
        this.CustomerAddress = customerAddress;
    }

    public String getOffer() {
        return this.Offer;
    }

    public void setOffer(String offer) {
        this.Offer = offer;
    }

    public ArrayList<ItemMaster> getAlOrderItemTran() {
        return alOrderItemTran;
    }

    public void setAlOrderItemTran(ArrayList<ItemMaster> alOrderItemTran) {
        this.alOrderItemTran = alOrderItemTran;
    }

    public short getType() {
        return Type;
    }
    //endregion

    public void setType(short type) {
        Type = type;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(OrderMasterId);
        parcel.writeString(OrderNumber);
        parcel.writeString(OrderDateTime);
        parcel.writeString(linktoTableMasterIds);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);
        parcel.writeInt(linktoOrderStatusMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(linktoBookingMasterId);
        parcel.writeDouble(TotalAmount);
        parcel.writeDouble(TotalTax);
        parcel.writeDouble(Discount);
        parcel.writeDouble(ExtraAmount);
        parcel.writeDouble(NetAmount);
        parcel.writeDouble(PaidAmount);
        parcel.writeDouble(BalanceAmount);
        parcel.writeInt(TotalItemPoint);
        parcel.writeInt(TotalDeductedPoint);
        parcel.writeString(Remark);
        parcel.writeByte((byte) (IsPreOrder ? 1 : 0));
        parcel.writeInt(linktoCustomerAddressTranId);
        parcel.writeLong(linktoSalesMasterId);
        parcel.writeInt(linktoOfferMasterId);
        parcel.writeString(OfferCode);
        parcel.writeString(CreateDateTime);
        parcel.writeString(UpdateDateTime);

        /// Extra
        parcel.writeString(Customer);
        parcel.writeString(RegisteredUser);
        parcel.writeString(OrderType);
        parcel.writeString(OrderStatus);
        parcel.writeInt(CustomerAddress);
        parcel.writeString(Offer);
    }
}
