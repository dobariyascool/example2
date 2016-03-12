package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class AreaMaster implements Parcelable {

    //region Properties

    short AreaMasterId;
    public short getAreaMasterId() { return this.AreaMasterId; }
    public void setAreaMasterId(short areaMasterId) { this.AreaMasterId = areaMasterId; }

    String AreaName;
    public String getAreaName() { return this.AreaName; }
    public void setAreaName(String areaName) { this.AreaName = areaName; }

    String ZipCode;
    public String getZipCode() { return this.ZipCode; }
    public void setZipCode(String zipCode) { this.ZipCode = zipCode; }

    short linktoCityMasterId;
    public short getlinktoCityMasterId() { return this.linktoCityMasterId; }
    public void setlinktoCityMasterId(short linktoCityMasterId) { this.linktoCityMasterId = linktoCityMasterId; }

    boolean IsEnabled;
    public boolean getIsEnabled() { return this.IsEnabled; }
    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }


    /// Extra
    String City;
    public String getCity() { return this.City; }
    public void setCity(String city) { this.City = city; }

    //endregion

    public static final Parcelable.Creator<AreaMaster> CREATOR = new Parcelable.Creator<AreaMaster>() {
        public AreaMaster createFromParcel(Parcel source) {
            AreaMaster objAreaMaster = new AreaMaster();
            objAreaMaster.AreaMasterId = (short)source.readInt();
            objAreaMaster.AreaName = source.readString();
            objAreaMaster.ZipCode = source.readString();
            objAreaMaster.linktoCityMasterId = (short)source.readInt();
            objAreaMaster.IsEnabled = source.readByte() != 0;

            /// Extra
            objAreaMaster.City = source.readString();
            return objAreaMaster;
        }

        public AreaMaster[] newArray(int size) {
            return new AreaMaster[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(AreaMasterId);
        parcel.writeString(AreaName);
        parcel.writeString(ZipCode);
        parcel.writeInt(linktoCityMasterId);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));

        /// Extra
        parcel.writeString(City);
    }
}
