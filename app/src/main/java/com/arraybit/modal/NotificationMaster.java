package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for NotificationMaster
/// </summary>
public class NotificationMaster implements Parcelable {

    int NotificationMasterId;
    String NotificationTitle;
    String NotificationText;
    String NotificationImageNameBytes;
    String NotificationImageName;
    String NotificationDateTime;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    short IsRead;
    boolean IsDeleted;
    short Type;
    int ID;

    public static final Creator<NotificationMaster> CREATOR = new Creator<NotificationMaster>() {
        public NotificationMaster createFromParcel(Parcel source) {
            NotificationMaster objNotificationMaster = new NotificationMaster();
            objNotificationMaster.NotificationMasterId = source.readInt();
            objNotificationMaster.NotificationTitle = source.readString();
            objNotificationMaster.NotificationText = source.readString();
            objNotificationMaster.NotificationImageNameBytes = source.readString();
            objNotificationMaster.NotificationImageName = source.readString();
            objNotificationMaster.NotificationDateTime = source.readString();
            objNotificationMaster.CreateDateTime = source.readString();
            objNotificationMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objNotificationMaster.UpdateDateTime = source.readString();
            objNotificationMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objNotificationMaster.linktoBusinessMasterId = (short) source.readInt();
            objNotificationMaster.IsRead = (short) source.readInt();
            objNotificationMaster.IsDeleted = source.readByte() != 0;
            objNotificationMaster.Type = (short)source.readInt();
            objNotificationMaster.ID = source.readInt();

            return objNotificationMaster;
        }

        public NotificationMaster[] newArray(int size) {
            return new NotificationMaster[size];
        }
    };
    //region Properties

    public int getNotificationMasterId() {
        return this.NotificationMasterId;
    }

    public void setNotificationMasterId(int notificationMasterId) {
        this.NotificationMasterId = notificationMasterId;
    }

    public String getNotificationTitle() {
        return this.NotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.NotificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return this.NotificationText;
    }

    public void setNotificationText(String notificationText) {
        this.NotificationText = notificationText;
    }

    public String getNotificationImageNameBytes() {
        return this.NotificationImageNameBytes;
    }

    public void setNotificationImageNameBytes(String notificationImageNameBytes) {
        this.NotificationImageNameBytes = notificationImageNameBytes;
    }

    public String getNotificationImageName() {
        return this.NotificationImageName;
    }

    public void setNotificationImageName(String notificationImageName) {
        this.NotificationImageName = notificationImageName;
    }

    public String getNotificationDateTime() {
        return this.NotificationDateTime;
    }

    public void setNotificationDateTime(String notificationDateTime) {
        this.NotificationDateTime = notificationDateTime;
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

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public short getType() { return this.Type; }

    public void setType(short type) { this.Type = type; }

    public int getID() { return this.ID; }

    public void setID(int iD) { this.ID = iD; }

    public short getIsRead() {
        return IsRead;
    }

    public void setIsRead(short isRead) {
        IsRead = isRead;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(NotificationMasterId);
        parcel.writeString(NotificationTitle);
        parcel.writeString(NotificationText);
        parcel.writeString(NotificationImageNameBytes);
        parcel.writeString(NotificationImageName);
        parcel.writeString(NotificationDateTime);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(IsRead);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeInt(Type);
        parcel.writeInt(ID);

    }
}
