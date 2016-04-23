package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for StateMaster
/// </summary>
public class StateMaster implements Parcelable {
    //region Properties

    short StateMasterId;
    String StateName;
    String StateCode;
    short linktoCountryMasterId;
    boolean IsEnabled;
    boolean IsDeleted;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    /// Extra
    String Country;
    String UserCreatedBy;
    String UserUpdatedBy;
    public static final Parcelable.Creator<StateMaster> CREATOR = new Creator<StateMaster>() {
        public StateMaster createFromParcel(Parcel source) {
            StateMaster objStateMaster = new StateMaster();
            objStateMaster.StateMasterId = (short)source.readInt();
            objStateMaster.StateName = source.readString();
            objStateMaster.StateCode = source.readString();
            objStateMaster.linktoCountryMasterId = (short)source.readInt();
            objStateMaster.IsEnabled = source.readByte() != 0;
            objStateMaster.IsDeleted = source.readByte() != 0;
            objStateMaster.CreateDateTime = source.readString();
            objStateMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objStateMaster.UpdateDateTime = source.readString();
            objStateMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();

            /// Extra
            objStateMaster.Country = source.readString();
            objStateMaster.UserCreatedBy = source.readString();
            objStateMaster.UserUpdatedBy = source.readString();
            return objStateMaster;
        }

        public StateMaster[] newArray(int size) {
            return new StateMaster[size];
        }
    };

    public short getStateMasterId() { return this.StateMasterId; }

    public void setStateMasterId(short stateMasterId) { this.StateMasterId = stateMasterId; }

    public String getStateName() { return this.StateName; }

    public void setStateName(String stateName) { this.StateName = stateName; }

    public String getStateCode() { return this.StateCode; }

    public void setStateCode(String stateCode) { this.StateCode = stateCode; }

    public short getlinktoCountryMasterId() { return this.linktoCountryMasterId; }

    public void setlinktoCountryMasterId(short linktoCountryMasterId) { this.linktoCountryMasterId = linktoCountryMasterId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public String getCountry() { return this.Country; }

    public void setCountry(String country) { this.Country = country; }

    public String getUserCreatedBy() { return this.UserCreatedBy; }

    public void setUserCreatedBy(String userCreatedBy) { this.UserCreatedBy = userCreatedBy; }

    public String getUserUpdatedBy() { return this.UserUpdatedBy; }

    //endregion

    public void setUserUpdatedBy(String userUpdatedBy) { this.UserUpdatedBy = userUpdatedBy; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(StateMasterId);
        parcel.writeString(StateName);
        parcel.writeString(StateCode);
        parcel.writeInt(linktoCountryMasterId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeString(Country);
        parcel.writeString(UserCreatedBy);
        parcel.writeString(UserUpdatedBy);
    }
}
