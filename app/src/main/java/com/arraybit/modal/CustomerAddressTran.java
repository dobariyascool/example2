package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerAddressTran implements Parcelable {
    //region Properties

    int CustomerAddressTranId;
    int linktoCustomerMasterId;
    int linktoRegisteredUserMasterId;
    String Address;
    short AddressType;
    short linktoCountryMasterId;
    short linktoStateMasterId;
    short linktoCityMasterId;
    short linktoAreaMasterId;
    String ZipCode;
    boolean IsPrimary;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    boolean IsDeleted;
    String MobileNum;
    /// Extra
    String CustomerName;
    String RegisteredUser;
    String Country;
    String State;
    String City;
    String Area;
    String UserCreatedBy;
    public static final Creator<CustomerAddressTran> CREATOR = new Creator<CustomerAddressTran>() {
        public CustomerAddressTran createFromParcel(Parcel source) {
            CustomerAddressTran objCustomerAddressTran = new CustomerAddressTran();
            objCustomerAddressTran.CustomerAddressTranId = source.readInt();
            objCustomerAddressTran.linktoCustomerMasterId = source.readInt();
            objCustomerAddressTran.CustomerName = source.readString();
            objCustomerAddressTran.linktoRegisteredUserMasterId = source.readInt();
            objCustomerAddressTran.Address = source.readString();
            objCustomerAddressTran.AddressType = (short) source.readInt();
            objCustomerAddressTran.linktoCountryMasterId = (short) source.readInt();
            objCustomerAddressTran.linktoStateMasterId = (short) source.readInt();
            objCustomerAddressTran.linktoCityMasterId = (short) source.readInt();
            objCustomerAddressTran.linktoAreaMasterId = (short) source.readInt();
            objCustomerAddressTran.ZipCode = source.readString();
            objCustomerAddressTran.MobileNum = source.readString();
            objCustomerAddressTran.IsPrimary = source.readByte() != 0;
            objCustomerAddressTran.CreateDateTime = source.readString();
            objCustomerAddressTran.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objCustomerAddressTran.IsDeleted = source.readByte() != 0;

            /// Extra
            objCustomerAddressTran.RegisteredUser = source.readString();
            objCustomerAddressTran.Country = source.readString();
            objCustomerAddressTran.State = source.readString();
            objCustomerAddressTran.City = source.readString();
            objCustomerAddressTran.Area = source.readString();
            objCustomerAddressTran.UserCreatedBy = source.readString();
            return objCustomerAddressTran;
        }

        public CustomerAddressTran[] newArray(int size) {
            return new CustomerAddressTran[size];
        }
    };

    public int getCustomerAddressTranId() {
        return this.CustomerAddressTranId;
    }

    public void setCustomerAddressTranId(int customerAddressTranId) {
        this.CustomerAddressTranId = customerAddressTranId;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customer) {
        this.CustomerName = customer;
    }

    public int getlinktoRegisteredUserMasterId() {
        return this.linktoRegisteredUserMasterId;
    }

    public void setlinktoRegisteredUserMasterId(int linktoRegisteredUserMasterId) {
        this.linktoRegisteredUserMasterId = linktoRegisteredUserMasterId;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public short getAddressType() {
        return AddressType;
    }

    public void setAddressType(short addressType) {
        AddressType = addressType;
    }

    public short getlinktoCountryMasterId() {
        return this.linktoCountryMasterId;
    }

    public void setlinktoCountryMasterId(short linktoCountryMasterId) {
        this.linktoCountryMasterId = linktoCountryMasterId;
    }

    public short getlinktoStateMasterId() {
        return this.linktoStateMasterId;
    }

    public void setlinktoStateMasterId(short linktoStateMasterId) {
        this.linktoStateMasterId = linktoStateMasterId;
    }

    public short getlinktoCityMasterId() {
        return this.linktoCityMasterId;
    }

    public void setlinktoCityMasterId(short linktoCityMasterId) {
        this.linktoCityMasterId = linktoCityMasterId;
    }

    public short getlinktoAreaMasterId() {
        return this.linktoAreaMasterId;
    }

    public void setlinktoAreaMasterId(short linktoAreaMasterId) {
        this.linktoAreaMasterId = linktoAreaMasterId;
    }

    public String getZipCode() {
        return this.ZipCode;
    }

    public void setZipCode(String zipCode) {
        this.ZipCode = zipCode;
    }

    public boolean getIsPrimary() {
        return this.IsPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.IsPrimary = isPrimary;
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

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public String getRegisteredUser() {
        return this.RegisteredUser;
    }

    public void setRegisteredUser(String registeredUser) {
        this.RegisteredUser = registeredUser;
    }

    public String getCountry() {
        return this.Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public String getState() {
        return this.State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public String getCity() {
        return this.City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getArea() {
        return this.Area;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public String getUserCreatedBy() {
        return this.UserCreatedBy;
    }

    public void setUserCreatedBy(String userCreatedBy) {
        this.UserCreatedBy = userCreatedBy;
    }

    public String getMobileNum() {
        return MobileNum;
    }

    public void setMobileNum(String mobileNum) {
        MobileNum = mobileNum;
    }

    //endregion

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CustomerAddressTranId);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeString(CustomerName);
        parcel.writeInt(linktoRegisteredUserMasterId);
        parcel.writeString(Address);
        parcel.writeInt(AddressType);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeInt(linktoCityMasterId);
        parcel.writeInt(linktoAreaMasterId);
        parcel.writeString(ZipCode);
        parcel.writeString(MobileNum);
        parcel.writeByte((byte) (IsPrimary ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(RegisteredUser);
        parcel.writeString(Country);
        parcel.writeString(State);
        parcel.writeString(City);
        parcel.writeString(Area);
        parcel.writeString(UserCreatedBy);
    }
}