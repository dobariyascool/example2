package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class TaxMaster implements Parcelable {

    //region Properties

    short TaxMasterId;
    String TaxName;
    String TaxCaption;
    short TaxIndex;
    double TaxRate;
    boolean IsPercentage;
    short linktoBusinessMasterId;
    boolean IsEnabled;
    String CreatedDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    /// Extra
    double DefaultTaxRate;
    int linktoItemMasterId;
    public static final Creator<TaxMaster> CREATOR = new Creator<TaxMaster>() {
        public TaxMaster createFromParcel(Parcel source) {
            TaxMaster objTaxMaster = new TaxMaster();
            objTaxMaster.TaxMasterId = (short)source.readInt();
            objTaxMaster.TaxName = source.readString();
            objTaxMaster.TaxCaption = source.readString();
            objTaxMaster.TaxIndex = (short)source.readInt();
            objTaxMaster.TaxRate = source.readDouble();
            objTaxMaster.IsPercentage = source.readByte() != 0;
            objTaxMaster.linktoBusinessMasterId = (short)source.readInt();
            objTaxMaster.IsEnabled = source.readByte() != 0;
            objTaxMaster.CreatedDateTime = source.readString();
            objTaxMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objTaxMaster.UpdateDateTime = source.readString();
            objTaxMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();

            /// Extra
            objTaxMaster.DefaultTaxRate = source.readDouble();
            objTaxMaster.linktoItemMasterId = source.readInt();
            return objTaxMaster;
        }

        public TaxMaster[] newArray(int size) {
            return new TaxMaster[size];
        }
    };

    public short getTaxMasterId() { return this.TaxMasterId; }

    public void setTaxMasterId(short taxMasterId) { this.TaxMasterId = taxMasterId; }

    public String getTaxName() { return this.TaxName; }

    public void setTaxName(String taxName) { this.TaxName = taxName; }

    public String getTaxCaption() { return this.TaxCaption; }

    public void setTaxCaption(String taxCaption) { this.TaxCaption = taxCaption; }

    public short getTaxIndex() { return this.TaxIndex; }

    public void setTaxIndex(short taxIndex) { this.TaxIndex = taxIndex; }

    public double getTaxRate() { return this.TaxRate; }

    public void setTaxRate(double taxRate) { this.TaxRate = taxRate; }

    public boolean getIsPercentage() { return this.IsPercentage; }

    public void setIsPercentage(boolean isPercentage) { this.IsPercentage = isPercentage; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public String getCreatedDateTime() { return this.CreatedDateTime; }

    public void setCreatedDateTime(String createdDateTime) { this.CreatedDateTime = createdDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public double getDefaultTaxRate() { return this.DefaultTaxRate; }

    public void setDefaultTaxRate(double defaultTaxRate) { this.DefaultTaxRate = defaultTaxRate; }

    public int getlinktoItemMasterId() { return this.linktoItemMasterId; }

    //endregion

    public void setlinktoItemMasterId(int linktoItemMasterId) { this.linktoItemMasterId = linktoItemMasterId; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(TaxMasterId);
        parcel.writeString(TaxName);
        parcel.writeString(TaxCaption);
        parcel.writeInt(TaxIndex);
        parcel.writeDouble(TaxRate);
        parcel.writeByte((byte) (IsPercentage ? 1 : 0));
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeString(CreatedDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeDouble(DefaultTaxRate);
        parcel.writeInt(linktoItemMasterId);
    }
}
