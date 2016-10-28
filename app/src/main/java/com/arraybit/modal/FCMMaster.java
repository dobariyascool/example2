package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for FCMMaster
/// </summary>
public class FCMMaster implements Parcelable {
    //region Properties
    long FCMMasterId;
    String FCMToken;
    String CreateDateTime;
    String UpdateDateTime;
    int linktoCustomerMasterId;
    short linktoBusinessMasterId;
    //endregion

    /// Extra

    public static final Creator<FCMMaster> CREATOR = new Creator<FCMMaster>() {
        public FCMMaster createFromParcel(Parcel source) {
            FCMMaster objFCMMaster = new FCMMaster();
            objFCMMaster.FCMMasterId = source.readLong();
            objFCMMaster.FCMToken = source.readString();
            objFCMMaster.CreateDateTime = source.readString();
            objFCMMaster.UpdateDateTime = source.readString();
            objFCMMaster.linktoCustomerMasterId = source.readInt();
            objFCMMaster.linktoBusinessMasterId = (short) source.readInt();

            /// Extra
            return objFCMMaster;
        }

        public FCMMaster[] newArray(int size) {
            return new FCMMaster[size];
        }
    };


    public long getFCMMasterId() {
        return this.FCMMasterId;
    }

    public void setFCMMasterId(long fCMMasterId) {
        this.FCMMasterId = fCMMasterId;
    }

    public String getFCMToken() {
        return this.FCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.FCMToken = fCMToken;
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

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getLinktoBusinessMasterId() {
        return linktoBusinessMasterId;
    }

    public void setLinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public int getLinktoCustomerMasterId() {
        return linktoCustomerMasterId;
    }

    public void setLinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(FCMMasterId);
        parcel.writeString(FCMToken);
        parcel.writeString(CreateDateTime);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
    }
}
