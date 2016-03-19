package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OptionMaster implements Parcelable {

    //region Properties

    short OptionMasterId;
    int OptionRowId;
    String OptionName;
    public static final Parcelable.Creator<OptionMaster> CREATOR = new Parcelable.Creator<OptionMaster>() {
        public OptionMaster createFromParcel(Parcel source) {
            OptionMaster objOptionMaster = new OptionMaster();
            objOptionMaster.OptionMasterId = (short) source.readInt();
            objOptionMaster.OptionName = source.readString();
            return objOptionMaster;
        }

        public OptionMaster[] newArray(int size) {
            return new OptionMaster[size];
        }
    };
    ArrayList<OptionValueTran> alOptionValueTran;

    public int getOptionRowId() {
        return OptionRowId;
    }

    public void setOptionRowId(int optionRowId) {
        OptionRowId = optionRowId;
    }

    public short getOptionMasterId() {
        return this.OptionMasterId;
    }

    public void setOptionMasterId(short optionMasterId) {
        this.OptionMasterId = optionMasterId;
    }

    public String getOptionName() {
        return this.OptionName;
    }

    public void setOptionName(String optionName) {
        this.OptionName = optionName;
    }

    public ArrayList<OptionValueTran> getAlOptionValueTran() {
        return alOptionValueTran;
    }

    //endregion

    public void setAlOptionValueTran(ArrayList<OptionValueTran> alOptionValueTran) {
        this.alOptionValueTran = alOptionValueTran;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(OptionMasterId);
        parcel.writeString(OptionName);
    }

}