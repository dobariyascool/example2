package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactUsMaster implements Parcelable {

    //region Properties
    String Name;
    String Email;
    String Mobile;
    String Message;
    //endregion

    public static final Creator<ContactUsMaster> CREATOR = new Creator<ContactUsMaster>() {
        @Override
        public ContactUsMaster createFromParcel(Parcel source) {
            ContactUsMaster objContactUsMaster = new ContactUsMaster();
            objContactUsMaster.Name = source.readString();
            objContactUsMaster.Email = source.readString();
            objContactUsMaster.Mobile = source.readString();
            objContactUsMaster.Message = source.readString();

            return objContactUsMaster;
        }

        @Override
        public ContactUsMaster[] newArray(int size) {
            return new ContactUsMaster[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Name);
        parcel.writeString(Email);
        parcel.writeString(Mobile);
        parcel.writeString(Message);

    }
}
