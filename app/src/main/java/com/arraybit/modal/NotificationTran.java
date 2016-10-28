package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for NotificationTran
/// </summary>
public class NotificationTran implements Parcelable {
    //region Properties
    long NotificationTranId;
    int linktoNotificationMasterId;
    int linktoCustomerMasterId;
    String ReadDateTime;
    //endregion

    public static final Creator<NotificationTran> CREATOR = new Creator<NotificationTran>() {
        public NotificationTran createFromParcel(Parcel source) {
            NotificationTran objNotificationTran = new NotificationTran();
            objNotificationTran.NotificationTranId = source.readLong();
            objNotificationTran.linktoNotificationMasterId = source.readInt();
            objNotificationTran.linktoCustomerMasterId = source.readInt();
            objNotificationTran.ReadDateTime = source.readString();

            /// Extra
            return objNotificationTran;
        }

        public NotificationTran[] newArray(int size) {
            return new NotificationTran[size];
        }
    };


    public long getNotificationTranId() {
        return this.NotificationTranId;
    }

    public void setNotificationTranId(long notificationTranId) {
        this.NotificationTranId = notificationTranId;
    }

    public int getlinktoNotificationMasterId() {
        return this.linktoNotificationMasterId;
    }

    public void setlinktoNotificationMasterId(int linktoNotificationMasterId) {
        this.linktoNotificationMasterId = linktoNotificationMasterId;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public String getReadDateTime() {
        return this.ReadDateTime;
    }

    public void setReadDateTime(String readDateTime) {
        this.ReadDateTime = readDateTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(NotificationTranId);
        parcel.writeInt(linktoNotificationMasterId);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeString(ReadDateTime);
    }
}
