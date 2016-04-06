package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingMaster implements Parcelable {
    //region Properties

    int BookingMasterId;
    String FromDate;
    String ToDate;
    String FromTime;
    String ToTime;
    boolean IsHourly;
    int linktoCustomerMasterId;
    String BookingPersonName;
    String Email;
    String Phone;
    short NoOfAdults;
    short NoOfChildren;
    double TotalAmount;
    short DiscountPercentage;
    double DiscountAmount;
    double ExtraAmount;
    double NetAmount;
    double PaidAmount;
    double BalanceAmount;
    String Remark;
    boolean IsPreOrder;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    boolean IsDeleted;
    short BookingStatus;
    public static final Parcelable.Creator<BookingMaster> CREATOR = new Creator<BookingMaster>() {
        public BookingMaster createFromParcel(Parcel source) {
            BookingMaster objBookingMaster = new BookingMaster();
            objBookingMaster.BookingMasterId = source.readInt();
            objBookingMaster.FromDate = source.readString();
            objBookingMaster.ToDate = source.readString();
            objBookingMaster.FromTime = source.readString();
            objBookingMaster.ToTime = source.readString();
            objBookingMaster.IsHourly = source.readByte() != 0;
            objBookingMaster.linktoCustomerMasterId = source.readInt();
            objBookingMaster.BookingPersonName = source.readString();
            objBookingMaster.Email = source.readString();
            objBookingMaster.Phone = source.readString();
            objBookingMaster.NoOfAdults = (short) source.readInt();
            objBookingMaster.NoOfChildren = (short) source.readInt();
            objBookingMaster.TotalAmount = source.readDouble();
            objBookingMaster.DiscountPercentage = (short) source.readInt();
            objBookingMaster.DiscountAmount = source.readDouble();
            objBookingMaster.ExtraAmount = source.readDouble();
            objBookingMaster.NetAmount = source.readDouble();
            objBookingMaster.PaidAmount = source.readDouble();
            objBookingMaster.BalanceAmount = source.readDouble();
            objBookingMaster.Remark = source.readString();
            objBookingMaster.IsPreOrder = source.readByte() != 0;
            objBookingMaster.CreateDateTime = source.readString();
            objBookingMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objBookingMaster.UpdateDateTime = source.readString();
            objBookingMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objBookingMaster.linktoBusinessMasterId = (short) source.readInt();
            objBookingMaster.IsDeleted = source.readByte() != 0;
            objBookingMaster.BookingStatus = (short) source.readInt();

            /// Extra
//            objBookingMaster.UserCreatedBy = source.readString();
//            objBookingMaster.UserUpdatedBy = source.readString();
//            objBookingMaster.Business = source.readString();
            return objBookingMaster;
        }

        public BookingMaster[] newArray(int size) {
            return new BookingMaster[size];
        }
    };
    /// Extra
    String UserCreatedBy;
    String UserUpdatedBy;
    String Business;

    public int getBookingMasterId() {
        return this.BookingMasterId;
    }

    public void setBookingMasterId(int bookingMasterId) {
        this.BookingMasterId = bookingMasterId;
    }

    public String getFromDate() {
        return this.FromDate;
    }

    public void setFromDate(String fromDate) {
        this.FromDate = fromDate;
    }

    public String getToDate() {
        return this.ToDate;
    }

    public void setToDate(String toDate) {
        this.ToDate = toDate;
    }

    public String getFromTime() {
        return this.FromTime;
    }

    public void setFromTime(String fromTime) {
        this.FromTime = fromTime;
    }

    public String getToTime() {
        return this.ToTime;
    }

    public void setToTime(String toTime) {
        this.ToTime = toTime;
    }

    public boolean getIsHourly() {
        return this.IsHourly;
    }

    public void setIsHourly(boolean isHourly) {
        this.IsHourly = isHourly;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public String getBookingPersonName() {
        return this.BookingPersonName;
    }

    public void setBookingPersonName(String bookingPersonName) {
        this.BookingPersonName = bookingPersonName;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public short getNoOfAdults() {
        return this.NoOfAdults;
    }

    public void setNoOfAdults(short noOfAdults) {
        this.NoOfAdults = noOfAdults;
    }

    public short getNoOfChildren() {
        return this.NoOfChildren;
    }

    public void setNoOfChildren(short noOfChildren) {
        this.NoOfChildren = noOfChildren;
    }

    public double getTotalAmount() {
        return this.TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.TotalAmount = totalAmount;
    }

    public short getDiscountPercentage() {
        return this.DiscountPercentage;
    }

    public void setDiscountPercentage(short discountPercentage) {
        this.DiscountPercentage = discountPercentage;
    }

    public double getDiscountAmount() {
        return this.DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.DiscountAmount = discountAmount;
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

    public String getCreateDateTime() {
        return this.CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.CreateDateTime = createDateTime;
    }

    public short getlinktoUserMasterIdCreatedBy() {
        return this.linktoUserMasterIdCreatedBy;
    }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) {
        this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy;
    }

    public String getUpdateDateTime() {
        return this.UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.UpdateDateTime = updateDateTime;
    }

    public short getlinktoUserMasterIdUpdatedBy() {
        return this.linktoUserMasterIdUpdatedBy;
    }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) {
        this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public short getBookingStatus() {
        return this.BookingStatus;
    }

    public void setBookingStatus(short bookingStatus) {
        this.BookingStatus = bookingStatus;
    }

    //    public String getUserCreatedBy() {
//        return this.UserCreatedBy;
//    }
//
//    public void setUserCreatedBy(String userCreatedBy) {
//        this.UserCreatedBy = userCreatedBy;
//    }
//
//    public String getUserUpdatedBy() {
//        return this.UserUpdatedBy;
//    }
//
//    public void setUserUpdatedBy(String userUpdatedBy) {
//        this.UserUpdatedBy = userUpdatedBy;
//    }
//
//    public void setBusiness(String business) {
//        this.Business = business;
//    }
//    public String getBusiness() {
//        return this.Business;
//    }

    //endregion

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BookingMasterId);
        parcel.writeString(FromDate);
        parcel.writeString(ToDate);
        parcel.writeString(FromTime);
        parcel.writeString(ToTime);
        parcel.writeByte((byte) (IsHourly ? 1 : 0));
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeString(BookingPersonName);
        parcel.writeString(Email);
        parcel.writeString(Phone);
        parcel.writeInt(NoOfAdults);
        parcel.writeInt(NoOfChildren);
        parcel.writeDouble(TotalAmount);
        parcel.writeInt(DiscountPercentage);
        parcel.writeDouble(DiscountAmount);
        parcel.writeDouble(ExtraAmount);
        parcel.writeDouble(NetAmount);
        parcel.writeDouble(PaidAmount);
        parcel.writeDouble(BalanceAmount);
        parcel.writeString(Remark);
        parcel.writeByte((byte) (IsPreOrder ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeInt(BookingStatus);

        /// Extra
//        parcel.writeString(UserCreatedBy);
//        parcel.writeString(UserUpdatedBy);
//        parcel.writeString(Business);
    }
}
