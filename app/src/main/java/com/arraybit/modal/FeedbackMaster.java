package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackMaster {
    int FeedbackMasterId;
    String Name;
    String Email;
    String Phone;
    String Feedback;
    String FeedbackDateTime;
    short FeedbackType;
    int linktoCustomerMasterId;
    short linktoBusinessMasterId;
    /// Extra
    String Customer;
    String Business;
    public static final Parcelable.Creator<FeedbackMaster> CREATOR = new Parcelable.Creator<FeedbackMaster>() {
        public FeedbackMaster createFromParcel(Parcel source) {
            FeedbackMaster objFeedbackMaster = new FeedbackMaster();
            objFeedbackMaster.FeedbackMasterId = source.readInt();
            objFeedbackMaster.Name = source.readString();
            objFeedbackMaster.Email = source.readString();
            objFeedbackMaster.Phone = source.readString();
            objFeedbackMaster.Feedback = source.readString();
            objFeedbackMaster.FeedbackDateTime = source.readString();
            objFeedbackMaster.FeedbackType = (short) source.readInt();
            objFeedbackMaster.linktoCustomerMasterId = source.readInt();
            objFeedbackMaster.linktoBusinessMasterId = (short) source.readInt();

            /// Extra
            objFeedbackMaster.Customer = source.readString();
            objFeedbackMaster.Business = source.readString();
            return objFeedbackMaster;
        }

        public FeedbackMaster[] newArray(int size) {
            return new FeedbackMaster[size];
        }
    };

    public int getFeedbackMasterId() {
        return this.FeedbackMasterId;
    }

    public void setFeedbackMasterId(int feedbackMasterId) {
        this.FeedbackMasterId = feedbackMasterId;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
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

    public String getFeedback() {
        return this.Feedback;
    }

    public void setFeedback(String feedback) {
        this.Feedback = feedback;
    }

    public String getFeedbackDateTime() {
        return this.FeedbackDateTime;
    }

    public void setFeedbackDateTime(String feedbackDateTime) {
        this.FeedbackDateTime = feedbackDateTime;
    }

    public short getFeedbackType() {
        return this.FeedbackType;
    }

    public void setFeedbackType(short feedbackType) {
        this.FeedbackType = feedbackType;
    }

    public int getlinktoCustomerMasterId() {
        return this.linktoCustomerMasterId;
    }

    public void setlinktoCustomerMasterId(int linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getCustomer() {
        return this.Customer;
    }

    public void setCustomer(String customer) {
        this.Customer = customer;
    }

    public String getBusiness() {
        return this.Business;
    }

    //endregion

    public void setBusiness(String business) {
        this.Business = business;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackMasterId);
        parcel.writeString(Name);
        parcel.writeString(Email);
        parcel.writeString(Phone);
        parcel.writeString(Feedback);
        parcel.writeString(FeedbackDateTime);
        parcel.writeInt(FeedbackType);
        parcel.writeInt(linktoCustomerMasterId);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(Customer);
        parcel.writeString(Business);
    }
}

