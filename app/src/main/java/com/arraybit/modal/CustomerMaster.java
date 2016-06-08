package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerMaster implements Parcelable {
    //region Properties

    int CustomerMasterId;
    String ShortName;
    String CustomerName;
    String Description;
    String ContactPersonName;
    String Designation;
    String Phone1;
    boolean IsPhone1DND;
    String Phone2;
    boolean IsPhone2DND;
    String Email1;
    String Email2;
    String Fax;
    String ImageNameBytes;
    String ImageName;
    String BirthDate;
    String AnniversaryDate;
    short CustomerType;
    boolean IsFavourite;
    boolean IsCredit;
    double OpeningBalance;
    short CreditDays;
    double CreditBalance;
    double CreditLimit;
    short linktoBusinessMasterId;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    boolean IsEnabled;
    boolean IsDeleted;
    String Gender;
    String Password;
    String ConfirmPassword;
    short linktoSourceMasterId;
    String Remark;
    String LastLoginDateTime;
    short linktoCountryMasterId;
    short linktoStateMasterId;
    short linktoCityMasterId;
    short linktoAreaMasterId;
    public static final Creator<CustomerMaster> CREATOR = new Creator<CustomerMaster>() {
        public CustomerMaster createFromParcel(Parcel source) {
            CustomerMaster objCustomerMaster = new CustomerMaster();
            objCustomerMaster.CustomerMasterId = source.readInt();
            objCustomerMaster.ShortName = source.readString();
            objCustomerMaster.CustomerName = source.readString();
            objCustomerMaster.Description = source.readString();
            objCustomerMaster.ContactPersonName = source.readString();
            objCustomerMaster.Designation = source.readString();
            objCustomerMaster.Phone1 = source.readString();
            objCustomerMaster.IsPhone1DND = source.readByte() != 0;
            objCustomerMaster.Phone2 = source.readString();
            objCustomerMaster.IsPhone2DND = source.readByte() != 0;
            objCustomerMaster.Email1 = source.readString();
            objCustomerMaster.Email2 = source.readString();
            objCustomerMaster.Fax = source.readString();
            objCustomerMaster.ImageNameBytes = source.readString();
            objCustomerMaster.ImageName = source.readString();
            objCustomerMaster.BirthDate = source.readString();
            objCustomerMaster.AnniversaryDate = source.readString();
            objCustomerMaster.CustomerType = (short) source.readInt();
            objCustomerMaster.IsFavourite = source.readByte() != 0;
            objCustomerMaster.IsCredit = source.readByte() != 0;
            objCustomerMaster.OpeningBalance = source.readDouble();
            objCustomerMaster.CreditDays = (short) source.readInt();
            objCustomerMaster.CreditBalance = source.readDouble();
            objCustomerMaster.CreditLimit = source.readDouble();
            objCustomerMaster.linktoBusinessMasterId = (short) source.readInt();
            objCustomerMaster.CreateDateTime = source.readString();
            objCustomerMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objCustomerMaster.UpdateDateTime = source.readString();
            objCustomerMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objCustomerMaster.IsEnabled = source.readByte() != 0;
            objCustomerMaster.IsDeleted = source.readByte() != 0;
            objCustomerMaster.Gender = source.readString();
            objCustomerMaster.Password = source.readString();
            objCustomerMaster.ConfirmPassword = source.readString();
            objCustomerMaster.linktoSourceMasterId = (short) source.readInt();
            objCustomerMaster.Remark = source.readString();
            objCustomerMaster.LastLoginDateTime = source.readString();
            objCustomerMaster.linktoCountryMasterId = (short) source.readInt();
            objCustomerMaster.linktoStateMasterId = (short) source.readInt();
            objCustomerMaster.linktoCityMasterId = (short) source.readInt();
            objCustomerMaster.linktoAreaMasterId = (short) source.readInt();
            /// Extra

            return objCustomerMaster;
        }

        public CustomerMaster[] newArray(int size) {
            return new CustomerMaster[size];
        }
    };
    String ImageNamePhysicalNameBytes;

    public int getCustomerMasterId() {
        return this.CustomerMasterId;
    }

    public void setCustomerMasterId(int customerMasterId) {
        this.CustomerMasterId = customerMasterId;
    }

    public String getShortName() {
        return this.ShortName;
    }

    public void setShortName(String shortName) {
        this.ShortName = shortName;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getContactPersonName() {
        return this.ContactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.ContactPersonName = contactPersonName;
    }

    public String getDesignation() {
        return this.Designation;
    }

    public void setDesignation(String designation) {
        this.Designation = designation;
    }

    public String getPhone1() {
        return this.Phone1;
    }

    public void setPhone1(String phone1) {
        this.Phone1 = phone1;
    }

    public boolean getIsPhone1DND() {
        return this.IsPhone1DND;
    }

    public void setIsPhone1DND(boolean isPhone1DND) {
        this.IsPhone1DND = isPhone1DND;
    }

    public String getPhone2() {
        return this.Phone2;
    }

    public void setPhone2(String phone2) {
        this.Phone2 = phone2;
    }

    public boolean getIsPhone2DND() {
        return this.IsPhone2DND;
    }

    public void setIsPhone2DND(boolean isPhone2DND) {
        this.IsPhone2DND = isPhone2DND;
    }

    public String getEmail1() {
        return this.Email1;
    }

    public void setEmail1(String email1) {
        this.Email1 = email1;
    }

    public String getEmail2() {
        return this.Email2;
    }

    public void setEmail2(String email2) {
        this.Email2 = email2;
    }

    public String getFax() {
        return this.Fax;
    }

    public void setFax(String fax) {
        this.Fax = fax;
    }

    public String getImageNameBytes() {
        return this.ImageNameBytes;
    }

    public void setImageNameBytes(String imageNameBytes) {
        this.ImageNameBytes = imageNameBytes;
    }

    public String getImageName() {
        return this.ImageName;
    }

    public void setImageName(String imageName) {
        this.ImageName = imageName;
    }

    public String getBirthDate() {
        return this.BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public String getAnniversaryDate() {
        return this.AnniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.AnniversaryDate = anniversaryDate;
    }

    public short getCustomerType() {
        return this.CustomerType;
    }

    public void setCustomerType(short customerType) {
        this.CustomerType = customerType;
    }

    public boolean getIsFavourite() {
        return this.IsFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.IsFavourite = isFavourite;
    }

    public boolean getIsCredit() {
        return this.IsCredit;
    }

    public void setIsCredit(boolean isCredit) {
        this.IsCredit = isCredit;
    }

    public double getOpeningBalance() {
        return this.OpeningBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.OpeningBalance = openingBalance;
    }

    public short getCreditDays() {
        return this.CreditDays;
    }

    public void setCreditDays(short creditDays) {
        this.CreditDays = creditDays;
    }

    public double getCreditBalance() {
        return this.CreditBalance;
    }

    public void setCreditBalance(double creditBalance) {
        this.CreditBalance = creditBalance;
    }

    public double getCreditLimit() {
        return this.CreditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.CreditLimit = creditLimit;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
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

    public boolean getIsEnabled() {
        return this.IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.IsEnabled = isEnabled;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public short getlinktoSourceMasterId() {
        return this.linktoSourceMasterId;
    }

    public void setlinktoSourceMasterId(short linktoSourceMasterId) {
        this.linktoSourceMasterId = linktoSourceMasterId;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public String getLastLoginDateTime() {
        return this.LastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.LastLoginDateTime = lastLoginDateTime;
    }

    public short getLinktoCityMasterId() {
        return linktoCityMasterId;
    }

    public void setLinktoCityMasterId(short linktoCityMasterId) {
        this.linktoCityMasterId = linktoCityMasterId;
    }

    public short getLinktoAreaMasterId() {
        return linktoAreaMasterId;
    }

    public void setLinktoAreaMasterId(short linktoAreaMasterId) {
        this.linktoAreaMasterId = linktoAreaMasterId;
    }

    public short getLinktoCountryMasterId() {
        return linktoCountryMasterId;
    }

    public void setLinktoCountryMasterId(short linktoCountryMasterId) {
        this.linktoCountryMasterId = linktoCountryMasterId;
    }

    public short getLinktoStateMasterId() {
        return linktoStateMasterId;
    }

    public void setLinktoStateMasterId(short linktoStateMasterId) {
        this.linktoStateMasterId = linktoStateMasterId;
    }

    public String getImageNamePhysicalNameBytes() {
        return ImageNamePhysicalNameBytes;
    }

    public void setImageNamePhysicalNameBytes(String imageNamePhysicalNameBytes) {
        ImageNamePhysicalNameBytes = imageNamePhysicalNameBytes;
    }

    /// Extra


    //endregion

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CustomerMasterId);
        parcel.writeString(ShortName);
        parcel.writeString(CustomerName);
        parcel.writeString(Description);
        parcel.writeString(ContactPersonName);
        parcel.writeString(Designation);
        parcel.writeString(Phone1);
        parcel.writeByte((byte) (IsPhone1DND ? 1 : 0));
        parcel.writeString(Phone2);
        parcel.writeByte((byte) (IsPhone2DND ? 1 : 0));
        parcel.writeString(Email1);
        parcel.writeString(Email2);
        parcel.writeString(Fax);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(BirthDate);
        parcel.writeString(AnniversaryDate);
        parcel.writeInt(CustomerType);
        parcel.writeByte((byte) (IsFavourite ? 1 : 0));
        parcel.writeByte((byte) (IsCredit ? 1 : 0));
        parcel.writeDouble(OpeningBalance);
        parcel.writeInt(CreditDays);
        parcel.writeDouble(CreditBalance);
        parcel.writeDouble(CreditLimit);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeString(Gender);
        parcel.writeString(Password);
        parcel.writeString(ConfirmPassword);
        parcel.writeInt(linktoSourceMasterId);
        parcel.writeString(Remark);
        parcel.writeString(LastLoginDateTime);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeInt(linktoCityMasterId);
        parcel.writeInt(linktoAreaMasterId);
        /// Extra

    }
}

