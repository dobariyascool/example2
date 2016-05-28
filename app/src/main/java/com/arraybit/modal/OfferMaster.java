package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferMaster implements Parcelable {

    //region Properties
    int OfferMasterId;
    short linktoOfferTypeMasterId;
    String OfferTitle;
    String OfferContent;
    String FromDate;
    String ToDate;
    String FromTime;
    String ToTime;
    double MinimumBillAmount;
    double Discount;
    boolean IsDiscountPercentage;
    short OfferLimit;
    int RedeemCount;
    String OfferCode;
    String ImagePhysicalName;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    short linktoBusinessMasterId;
    String TermsAndConditions;
    boolean IsEnabled;
    boolean IsDeleted;
    boolean IsForCustomers;
    boolean IsUnconditional;
    int BuyItemCount;
    int GetItemCount;
    short linktoCounterMasterId;
    short linktoOrderTypeMasterId;
    short linktoCustomerMasterId;
    /// Extra
    String OfferType;
    String Business;
    String ValidBuyItems;
    String ValidDays;
    String ValidGetItems;
    String ValidItems;
    String xs_ImagePhysicalName;
    String sm_ImagePhysicalName;
    String md_ImagePhysicalName;
    String lg_ImagePhysicalName;
    String xl_ImagePhysicalName;

    public static final Creator<OfferMaster> CREATOR = new Creator<OfferMaster>() {
        public OfferMaster createFromParcel(Parcel source) {
            OfferMaster objOfferMaster = new OfferMaster();
            objOfferMaster.OfferMasterId = source.readInt();
            objOfferMaster.linktoOfferTypeMasterId = (short) source.readInt();
            objOfferMaster.OfferTitle = source.readString();
            objOfferMaster.OfferContent = source.readString();
            objOfferMaster.FromDate = source.readString();
            objOfferMaster.ToDate = source.readString();
            objOfferMaster.FromTime = source.readString();
            objOfferMaster.ToTime = source.readString();
            objOfferMaster.MinimumBillAmount = source.readDouble();
            objOfferMaster.Discount = source.readDouble();
            objOfferMaster.IsDiscountPercentage = source.readByte() != 0;
            objOfferMaster.OfferLimit = (short) source.readInt();
            objOfferMaster.RedeemCount = source.readInt();
            objOfferMaster.OfferCode = source.readString();
            objOfferMaster.ImagePhysicalName = source.readString();
            objOfferMaster.CreateDateTime = source.readString();
            objOfferMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objOfferMaster.UpdateDateTime = source.readString();
            objOfferMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objOfferMaster.linktoBusinessMasterId = (short) source.readInt();
            objOfferMaster.TermsAndConditions = source.readString();
            objOfferMaster.IsEnabled = source.readByte() != 0;
            objOfferMaster.IsDeleted = source.readByte() != 0;
            objOfferMaster.IsForCustomers = source.readByte() != 0;
            objOfferMaster.IsUnconditional = source.readByte() != 0;
            objOfferMaster.BuyItemCount = source.readInt();
            objOfferMaster.GetItemCount = source.readInt();
            objOfferMaster.linktoCounterMasterId = (short) source.readInt();
            objOfferMaster.linktoOrderTypeMasterId = (short) source.readInt();

            /// Extra
            objOfferMaster.OfferType = source.readString();
            objOfferMaster.Business = source.readString();
            objOfferMaster.ValidBuyItems = source.readString();
            objOfferMaster.ValidGetItems = source.readString();
            objOfferMaster.ValidDays = source.readString();
            objOfferMaster.ValidItems = source.readString();
            objOfferMaster.sm_ImagePhysicalName = source.readString();
            objOfferMaster.xs_ImagePhysicalName = source.readString();
            objOfferMaster.md_ImagePhysicalName = source.readString();
            objOfferMaster.lg_ImagePhysicalName = source.readString();
            objOfferMaster.xl_ImagePhysicalName = source.readString();
            return objOfferMaster;
        }

        public OfferMaster[] newArray(int size) {
            return new OfferMaster[size];
        }
    };
    short Counter;

    public int getOfferMasterId() {
        return this.OfferMasterId;
    }

    public void setOfferMasterId(int offerMasterId) {
        this.OfferMasterId = offerMasterId;
    }

    public short getlinktoOfferTypeMasterId() {
        return this.linktoOfferTypeMasterId;
    }

    public void setlinktoOfferTypeMasterId(short linktoOfferTypeMasterId) {
        this.linktoOfferTypeMasterId = linktoOfferTypeMasterId;
    }

    public String getOfferTitle() {
        return this.OfferTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.OfferTitle = offerTitle;
    }

    public String getOfferContent() {
        return this.OfferContent;
    }

    public void setOfferContent(String offerContent) {
        this.OfferContent = offerContent;
    }

    public String getFromDate() {
        return this.FromDate;
    }

    public void setFromDate(String fromDate) {
        this.FromDate = fromDate;
    }

    public String getToDate() {
        return this.ToDate;
    }

    public void setToDate(String toDate) {
        this.ToDate = toDate;
    }

    public String getFromTime() {
        return this.FromTime;
    }

    public void setFromTime(String fromTime) {
        this.FromTime = fromTime;
    }

    public String getToTime() {
        return this.ToTime;
    }

    public void setToTime(String toTime) {
        this.ToTime = toTime;
    }

    public double getMinimumBillAmount() {
        return this.MinimumBillAmount;
    }

    public void setMinimumBillAmount(double minimumBillAmount) {
        this.MinimumBillAmount = minimumBillAmount;
    }

    public double getDiscount() {
        return this.Discount;
    }

    public void setDiscount(double discount) {
        this.Discount = discount;
    }

    public boolean getIsDiscountPercentage() {
        return this.IsDiscountPercentage;
    }

    public void setIsDiscountPercentage(boolean isDiscountPercentage) {
        this.IsDiscountPercentage = isDiscountPercentage;
    }

    public short getOfferLimit() {
        return this.OfferLimit;
    }

    public void setOfferLimit(short offerLimit) {
        this.OfferLimit = offerLimit;
    }

    public int getRedeemCount() {
        return this.RedeemCount;
    }

    public void setRedeemCount(int redeemCount) {
        this.RedeemCount = redeemCount;
    }

    public String getOfferCode() {
        return this.OfferCode;
    }

    public void setOfferCode(String offerCode) {
        this.OfferCode = offerCode;
    }

    public String getImagePhysicalName() {
        return this.ImagePhysicalName;
    }

    public void setImagePhysicalName(String imagePhysicalName) {
        this.ImagePhysicalName = imagePhysicalName;
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

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getTermsAndConditions() {
        return this.TermsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.TermsAndConditions = termsAndConditions;
    }

    public boolean getIsEnabled() {
        return this.IsEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.IsEnabled = isEnabled;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.IsDeleted = isDeleted;
    }

    public boolean getIsForCustomers() {
        return this.IsForCustomers;
    }

    public void setIsForCustomers(boolean isForCustomers) {
        this.IsForCustomers = isForCustomers;
    }

    public boolean getIsUnconditional() {
        return this.IsUnconditional;
    }

    public void setIsUnconditional(boolean isUnconditional) {
        this.IsUnconditional = isUnconditional;
    }

    public int getBuyItemCount() {
        return this.BuyItemCount;
    }

    public void setBuyItemCount(int buyItemCount) {
        this.BuyItemCount = buyItemCount;
    }

    public int getGetItemCount() {
        return this.GetItemCount;
    }

    public void setGetItemCount(int getItemCount) {
        this.GetItemCount = getItemCount;
    }

    public short getlinktoCounterMasterId() {
        return this.linktoCounterMasterId;
    }

    public void setlinktoCounterMasterId(short linktoCounterMasterId) {
        this.linktoCounterMasterId = linktoCounterMasterId;
    }

    public short getlinktoOrderTypeMasterId() {
        return this.linktoOrderTypeMasterId;
    }

    public void setlinktoOrderTypeMasterId(short linktoOrderTypeMasterId) {
        this.linktoOrderTypeMasterId = linktoOrderTypeMasterId;
    }

    public String getOfferType() {
        return this.OfferType;
    }

    public void setOfferType(String offerType) {
        this.OfferType = offerType;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public short getCounter() {
        return this.Counter;
    }

    public void setCounter(short counter) {
        this.Counter = counter;
    }

    public short getLinktoCustomerMasterId() {
        return linktoCustomerMasterId;
    }

    public void setLinktoCustomerMasterId(short linktoCustomerMasterId) {
        this.linktoCustomerMasterId = linktoCustomerMasterId;
    }

    public String getValidBuyItems() {
        return ValidBuyItems;
    }

    public void setValidBuyItems(String validBuyItems) {
        ValidBuyItems = validBuyItems;
    }

    public String getValidDays() {
        return ValidDays;
    }

    public void setValidDays(String validDays) {
        ValidDays = validDays;
    }

    public String getValidGetItems() {
        return ValidGetItems;
    }

    public void setValidGetItems(String validGetItems) {
        ValidGetItems = validGetItems;
    }

    public String getValidItems() {
        return ValidItems;
    }

    public void setValidItems(String validItems) {
        ValidItems = validItems;
    }

    public String getXs_ImagePhysicalName() {
        return xs_ImagePhysicalName;
    }

    public void setXs_ImagePhysicalName(String xs_ImagePhysicalName) {
        this.xs_ImagePhysicalName = xs_ImagePhysicalName;
    }

    public String getSm_ImagePhysicalName() {
        return sm_ImagePhysicalName;
    }

    public void setSm_ImagePhysicalName(String sm_ImagePhysicalName) {
        this.sm_ImagePhysicalName = sm_ImagePhysicalName;
    }

    public String getMd_ImagePhysicalName() {
        return md_ImagePhysicalName;
    }

    public void setMd_ImagePhysicalName(String md_ImagePhysicalName) {
        this.md_ImagePhysicalName = md_ImagePhysicalName;
    }

    public String getLg_ImagePhysicalName() {
        return lg_ImagePhysicalName;
    }

    public void setLg_ImagePhysicalName(String lg_ImagePhysicalName) {
        this.lg_ImagePhysicalName = lg_ImagePhysicalName;
    }

    public String getXl_ImagePhysicalName() {
        return xl_ImagePhysicalName;
    }

    public void setXl_ImagePhysicalName(String xl_ImagePhysicalName) {
        this.xl_ImagePhysicalName = xl_ImagePhysicalName;
    }

    //endregion


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(OfferMasterId);
        parcel.writeInt(linktoOfferTypeMasterId);
        parcel.writeString(OfferTitle);
        parcel.writeString(OfferContent);
        parcel.writeString(FromDate);
        parcel.writeString(ToDate);
        parcel.writeString(FromTime);
        parcel.writeString(ToTime);
        parcel.writeDouble(MinimumBillAmount);
        parcel.writeDouble(Discount);
        parcel.writeByte((byte) (IsDiscountPercentage ? 1 : 0));
        parcel.writeInt(OfferLimit);
        parcel.writeInt(RedeemCount);
        parcel.writeString(OfferCode);
        parcel.writeString(ImagePhysicalName);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(TermsAndConditions);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeByte((byte) (IsForCustomers ? 1 : 0));
        parcel.writeByte((byte) (IsUnconditional ? 1 : 0));
        parcel.writeInt(BuyItemCount);
        parcel.writeInt(GetItemCount);
        parcel.writeInt(linktoCounterMasterId);
        parcel.writeInt(linktoOrderTypeMasterId);

        /// Extra
        parcel.writeString(OfferType);
        parcel.writeString(Business);
        parcel.writeString(ValidDays);
        parcel.writeString(ValidBuyItems);
        parcel.writeString(ValidGetItems);
        parcel.writeString(ValidItems);
        parcel.writeString(xs_ImagePhysicalName);
        parcel.writeString(md_ImagePhysicalName);
        parcel.writeString(lg_ImagePhysicalName);
        parcel.writeString(xl_ImagePhysicalName);
        parcel.writeString(sm_ImagePhysicalName);
    }
}
