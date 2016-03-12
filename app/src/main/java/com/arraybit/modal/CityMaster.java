package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class CityMaster implements Parcelable {

    //region Properties

    short CityMasterId;
    public short getCityMasterId() { return this.CityMasterId; }
    public void setCityMasterId(short cityMasterId) { this.CityMasterId = cityMasterId; }

    String CityName;
    public String getCityName() { return this.CityName; }
    public void setCityName(String cityName) { this.CityName = cityName; }

    String CityCode;
    public String getCityCode() { return this.CityCode; }
    public void setCityCode(String cityCode) { this.CityCode = cityCode; }

    short linktoStateMasterId;
    public short getlinktoStateMasterId() { return this.linktoStateMasterId; }
    public void setlinktoStateMasterId(short linktoStateMasterId) { this.linktoStateMasterId = linktoStateMasterId; }

    boolean IsEnabled;
    public boolean getIsEnabled() { return this.IsEnabled; }
    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }


    /// Extra
    String State;
    public String getState() { return this.State; }
    public void setState(String state) { this.State = state; }

    //endregion

    public static final Parcelable.Creator<CityMaster> CREATOR = new Parcelable.Creator<CityMaster>() {
        public CityMaster createFromParcel(Parcel source) {
            CityMaster objCityMaster = new CityMaster();
            objCityMaster.CityMasterId = (short)source.readInt();
            objCityMaster.CityName = source.readString();
            objCityMaster.CityCode = source.readString();
            objCityMaster.linktoStateMasterId = (short)source.readInt();
            objCityMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objCityMaster.State = source.readString();
            return objCityMaster;
        }

        public CityMaster[] newArray(int size) {
            return new CityMaster[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CityMasterId);
        parcel.writeString(CityName);
        parcel.writeString(CityCode);
        parcel.writeInt(linktoStateMasterId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(State);
    }
}

