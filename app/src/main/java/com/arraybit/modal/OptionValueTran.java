package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OptionValueTran implements Parcelable {

    //region Properties

    int OptionValueTranId;
    short linktoOptionMasterId;
    String OptionValue;
    boolean IsDeleted;
    /// Extra
    String OptionName;
    public static final Parcelable.Creator<OptionValueTran> CREATOR = new Creator<OptionValueTran>() {
        public OptionValueTran createFromParcel(Parcel source) {
            OptionValueTran objOptionValueTran = new OptionValueTran();
            objOptionValueTran.OptionValueTranId = source.readInt();
            objOptionValueTran.linktoOptionMasterId = (short) source.readInt();
            objOptionValueTran.OptionValue = source.readString();
            objOptionValueTran.IsDeleted = source.readByte() != 0;

            /// Extra
            objOptionValueTran.OptionName = source.readString();
            return objOptionValueTran;
        }

        public OptionValueTran[] newArray(int size) {
            return new OptionValueTran[size];
        }
    };

    public int getOptionValueTranId() {
        return this.OptionValueTranId;
    }

    public void setOptionValueTranId(int optionValueTranId) {
        this.OptionValueTranId = optionValueTranId;
    }

    public short getlinktoOptionMasterId() {
        return this.linktoOptionMasterId;
    }

    public void setlinktoOptionMasterId(short linktoOptionMasterId) {
        this.linktoOptionMasterId = linktoOptionMasterId;
    }

    public String getOptionValue() {
        return this.OptionValue;
    }

    public void setOptionValue(String optionValue) {
        this.OptionValue = optionValue;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public String getOptionName() {
        return this.OptionName;
    }

    public void setOptionName(String option) {
        this.OptionName = option;
    }

    //endregion

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(OptionValueTranId);
        parcel.writeInt(linktoOptionMasterId);
        parcel.writeString(OptionValue);
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(OptionName);
    }
}
