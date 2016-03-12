package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisteredUserMaster implements Parcelable {

    //region Properties

    int RegisteredUserMasterId;
    String Email;
    String Phone;
    String Password;
    String FirstName;
    String LastName;
    String Gender;
    String BirthDate;
    short linktoCountryMasterId;
    short linktoStateMasterId;
    short linktoCityMasterId;
    short linktoAreaMasterId;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    String LastLoginDateTime;
    short linktoSourceMasterId;
    String Comment;
    boolean IsEnabled;
    /// Extra
    String Area;
    short linktoBusinessMasterId;
    public static final Creator<RegisteredUserMaster> CREATOR = new Creator<RegisteredUserMaster>() {
        @Override
        public RegisteredUserMaster createFromParcel(Parcel source) {
            RegisteredUserMaster objRegisteredUserMaster = new RegisteredUserMaster();
            objRegisteredUserMaster.RegisteredUserMasterId = source.readInt();
            objRegisteredUserMaster.Email = source.readString();
            objRegisteredUserMaster.Phone = source.readString();
            objRegisteredUserMaster.Password = source.readString();
            objRegisteredUserMaster.FirstName = source.readString();
            objRegisteredUserMaster.LastName = source.readString();
            objRegisteredUserMaster.Gender = source.readString();
            objRegisteredUserMaster.BirthDate = source.readString();
            objRegisteredUserMaster.linktoCountryMasterId = (short) source.readInt();
            objRegisteredUserMaster.linktoStateMasterId = (short) source.readInt();
            objRegisteredUserMaster.linktoCityMasterId = (short) source.readInt();
            objRegisteredUserMaster.linktoAreaMasterId = (short) source.readInt();
            objRegisteredUserMaster.CreateDateTime = source.readString();
            objRegisteredUserMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objRegisteredUserMaster.UpdateDateTime = source.readString();
            objRegisteredUserMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objRegisteredUserMaster.LastLoginDateTime = source.readString();
            objRegisteredUserMaster.linktoSourceMasterId = (short) source.readInt();
            objRegisteredUserMaster.Comment = source.readString();
            objRegisteredUserMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objRegisteredUserMaster.Area = source.readString();
            objRegisteredUserMaster.linktoBusinessMasterId = (short) source.readInt();
            return objRegisteredUserMaster;

        }

        @Override
        public RegisteredUserMaster[] newArray(int size) {
            return new RegisteredUserMaster[size];
        }
    };
    String OldPassword;

    public int getRegisteredUserMasterId() {
        return this.RegisteredUserMasterId;
    }

    public void setRegisteredUserMasterId(int registeredUserMasterId) {
        this.RegisteredUserMasterId = registeredUserMasterId;
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

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getGender() {
        return this.Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getBirthDate() {
        return this.BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public short getlinktoAreaMasterId() {
        return this.linktoAreaMasterId;
    }

    public void setlinktoAreaMasterId(short linktoAreaMasterId) {
        this.linktoAreaMasterId = linktoAreaMasterId;
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

    public String getLastLoginDateTime() {
        return this.LastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.LastLoginDateTime = lastLoginDateTime;
    }

    public short getlinktoSourceMasterId() {
        return this.linktoSourceMasterId;
    }

    public void setlinktoSourceMasterId(short linktoSourceMasterId) {
        this.linktoSourceMasterId = linktoSourceMasterId;
    }

    public String getComment() {
        return this.Comment;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }

    public boolean getIsEnabled() {
        return this.IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.IsEnabled = isEnabled;
    }

    public String getArea() {
        return this.Area;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public short getlinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }


    public short getlinktoCountryMasterId() {
        return linktoCountryMasterId;
    }

    public void setlinktoCountryMasterId(short linktoCountryMasterId) {
        this.linktoCountryMasterId = linktoCountryMasterId;
    }

    public short getlinktoStateMasterId() {
        return linktoStateMasterId;
    }

    public void setlinktoStateMasterId(short linktoStateMasterId) {
        this.linktoStateMasterId = linktoStateMasterId;
    }

    public short getlinktoCityMasterId() {
        return linktoCityMasterId;
    }

    public void setlinktoCityMasterId(short linktoCityMasterId) {
        this.linktoCityMasterId = linktoCityMasterId;
    }
    //endregion


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(RegisteredUserMasterId);
        parcel.writeString(Email);
        parcel.writeString(Phone);
        parcel.writeString(Password);
        parcel.writeString(FirstName);
        parcel.writeString(LastName);
        parcel.writeString(Gender);
        parcel.writeString(BirthDate);
        parcel.writeInt(linktoAreaMasterId);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeInt(linktoCityMasterId);

        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeString(LastLoginDateTime);
        parcel.writeInt(linktoSourceMasterId);
        parcel.writeString(Comment);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(Area);
        parcel.writeInt(linktoBusinessMasterId);

    }

}
