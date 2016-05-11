package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessMaster implements Parcelable {

    //region Properties

    short BusinessMasterId;
    short linktoBusinessGroupMasterId;
    String BusinessName;
    String BusinessShortName;
    String Address;
    String Phone1;
    String Phone2;
    String Email;
    String Fax;
    String Website;
    short linktoCountryMasterId;
    short linktoStateMasterId;
    String City;
    String ZipCode;
    String xs_ImagePhysicalName;
    String sm_ImagePhysicalName;
    String md_ImagePhysicalName;
    String lg_ImagePhysicalName;
    String xl_ImagePhysicalName;
    String ExtraText;
    short linktoBusinessTypeMasterId;
    String UniqueId;
    boolean IsEnabled;
    /// Extra
    String Country;
    String State;
    String BusinessType;
    public static final Parcelable.Creator<BusinessMaster> CREATOR = new Creator<BusinessMaster>() {
        public BusinessMaster createFromParcel(Parcel source) {
            BusinessMaster objBusinessMaster = new BusinessMaster();
            objBusinessMaster.BusinessMasterId = (short)source.readInt();
            objBusinessMaster.linktoBusinessGroupMasterId = (short)source.readInt();
            objBusinessMaster.BusinessName = source.readString();
            objBusinessMaster.BusinessShortName = source.readString();
            objBusinessMaster.Address = source.readString();
            objBusinessMaster.Phone1 = source.readString();
            objBusinessMaster.Phone2 = source.readString();
            objBusinessMaster.Email = source.readString();
            objBusinessMaster.Fax = source.readString();
            objBusinessMaster.Website = source.readString();
            objBusinessMaster.linktoCountryMasterId = (short)source.readInt();
            objBusinessMaster.linktoStateMasterId = (short)source.readInt();
            objBusinessMaster.City = source.readString();
            objBusinessMaster.ZipCode = source.readString();
            objBusinessMaster.xs_ImagePhysicalName = source.readString();
            objBusinessMaster.sm_ImagePhysicalName = source.readString();
            objBusinessMaster.md_ImagePhysicalName = source.readString();
            objBusinessMaster.lg_ImagePhysicalName = source.readString();
            objBusinessMaster.xl_ImagePhysicalName = source.readString();
            objBusinessMaster.ExtraText = source.readString();
            objBusinessMaster.linktoBusinessTypeMasterId = (short)source.readInt();
            objBusinessMaster.UniqueId = source.readString();
            objBusinessMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objBusinessMaster.Country = source.readString();
            objBusinessMaster.State = source.readString();
            objBusinessMaster.BusinessType = source.readString();
            return objBusinessMaster;
        }

        public BusinessMaster[] newArray(int size) {
            return new BusinessMaster[size];
        }
    };

    public short getBusinessMasterId() { return this.BusinessMasterId; }

    public void setBusinessMasterId(short businessMasterId) { this.BusinessMasterId = businessMasterId; }

    public String getBusinessName() { return this.BusinessName; }

    public void setBusinessName(String businessName) { this.BusinessName = businessName; }

    public String getBusinessShortName() { return this.BusinessShortName; }

    public void setBusinessShortName(String businessShortName) { this.BusinessShortName = businessShortName; }

    public String getAddress() { return this.Address; }

    public void setAddress(String address) { this.Address = address; }

    public String getPhone1() { return this.Phone1; }

    public void setPhone1(String phone1) { this.Phone1 = phone1; }

    public String getPhone2() { return this.Phone2; }

    public void setPhone2(String phone2) { this.Phone2 = phone2; }

    public String getEmail() { return this.Email; }

    public void setEmail(String email) { this.Email = email; }

    public String getFax() { return this.Fax; }

    public void setFax(String fax) { this.Fax = fax; }

    public String getWebsite() { return this.Website; }

    public void setWebsite(String website) { this.Website = website; }

    public short getlinktoCountryMasterId() { return this.linktoCountryMasterId; }

    public void setlinktoCountryMasterId(short linktoCountryMasterId) { this.linktoCountryMasterId = linktoCountryMasterId; }

    public short getlinktoStateMasterId() { return this.linktoStateMasterId; }

    public void setlinktoStateMasterId(short linktoStateMasterId) { this.linktoStateMasterId = linktoStateMasterId; }

    public String getCity() { return this.City; }

    public void setCity(String city) { this.City = city; }

    public String getZipCode() { return this.ZipCode; }

    public void setZipCode(String zipCode) { this.ZipCode = zipCode; }

    public String getXSImagePhysicalName() { return this.xs_ImagePhysicalName; }

    public void setXSImagePhysicalName(String xs_ImagePhysicalName) { this.xs_ImagePhysicalName = xs_ImagePhysicalName; }

    public String getSMImagePhysicalName() { return this.sm_ImagePhysicalName; }

    public void setSMImagePhysicalName(String sm_ImagePhysicalName) { this.sm_ImagePhysicalName = sm_ImagePhysicalName; }

    public String getMDImagePhysicalName() { return this.md_ImagePhysicalName; }

    public void setMDImagePhysicalName(String md_ImagePhysicalName) { this.md_ImagePhysicalName = md_ImagePhysicalName; }

    public String getLGImagePhysicalName() { return this.lg_ImagePhysicalName; }

    public void setLGImagePhysicalName(String lg_ImagePhysicalName) { this.lg_ImagePhysicalName = lg_ImagePhysicalName; }

    public String getXLImagePhysicalName() { return this.xl_ImagePhysicalName; }

    public void setXLImagePhysicalName(String xl_ImagePhysicalName) { this.xl_ImagePhysicalName = xl_ImagePhysicalName; }

    public String getExtraText() { return this.ExtraText; }

    public void setExtraText(String extraText) { this.ExtraText = extraText; }

    public short getlinktoBusinessTypeMasterId() { return this.linktoBusinessTypeMasterId; }

    public void setlinktoBusinessTypeMasterId(short linktoBusinessTypeMasterId) { this.linktoBusinessTypeMasterId = linktoBusinessTypeMasterId; }

    public String getUniqueId() { return this.UniqueId; }

    public void setUniqueId(String uniqueId) { this.UniqueId = uniqueId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getCountry() { return this.Country; }

    public void setCountry(String country) { this.Country = country; }

    public String getState() { return this.State; }

    public void setState(String state) { this.State = state; }

    public String getBusinessType() { return this.BusinessType; }

    public void setBusinessType(String businessType) { this.BusinessType = businessType; }

    public short getLinktoBusinessGroupMasterId() {
        return linktoBusinessGroupMasterId;
    }

    //endregion

    public void setLinktoBusinessGroupMasterId(short linktoBusinessGroupMasterId) {
        this.linktoBusinessGroupMasterId = linktoBusinessGroupMasterId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessMasterId);
        parcel.writeInt(linktoBusinessGroupMasterId);
        parcel.writeString(BusinessName);
        parcel.writeString(BusinessShortName);
        parcel.writeString(Address);
        parcel.writeString(Phone1);
        parcel.writeString(Phone2);
        parcel.writeString(Email);
        parcel.writeString(Fax);
        parcel.writeString(Website);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeString(City);
        parcel.writeString(ZipCode);
        parcel.writeString(xs_ImagePhysicalName);
        parcel.writeString(sm_ImagePhysicalName);
        parcel.writeString(md_ImagePhysicalName);
        parcel.writeString(lg_ImagePhysicalName);
        parcel.writeString(xl_ImagePhysicalName);
        parcel.writeString(ExtraText);
        parcel.writeInt(linktoBusinessTypeMasterId);
        parcel.writeString(UniqueId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(Country);
        parcel.writeString(State);
        parcel.writeString(BusinessType);
    }
}
