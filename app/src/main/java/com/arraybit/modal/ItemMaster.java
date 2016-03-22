package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ItemMaster implements Parcelable {
    //region Properties

    int ItemMasterId;
    String ShortName;
    String ItemName;
    String ItemCode;
    String BarCode;
    String ShortDescription;
    short linktoUnitMasterId;
    short linktoCategoryMasterId;
    boolean IsFavourite;
    short linktoItemStatusMasterId;
    short ItemPoint;
    short PriceByPoint;
    String SearchWords;
    short linktoBusinessMasterId;
    int SortOrder;
    boolean IsEnabled;
    boolean IsDeleted;
    boolean IsDineInOnly;
    short ItemType;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    double Rate;
    double MRP;
    double SellPrice;
    String xs_ImagePhysicalName;
    String sm_ImagePhysicalName;
    String md_ImagePhysicalName;
    String lg_ImagePhysicalName;
    String xl_ImagePhysicalName;
    String Tax;
    double TotalTax;
    double Tax1;
    double Tax2;
    double Tax3;
    double Tax4;
    double Tax5;
    /// Extra
    String Unit;
    String Category;
    String Business;
    String UserCreatedBy;
    String UserUpdatedBy;
    String linktoItemMasterIdModifiers;
    String linktoOptionMasterIds;
    public static final Parcelable.Creator<ItemMaster> CREATOR = new Creator<ItemMaster>() {
        public ItemMaster createFromParcel(Parcel source) {
            ItemMaster objItemMaster = new ItemMaster();
            objItemMaster.ItemMasterId = source.readInt();
            objItemMaster.ShortName = source.readString();
            objItemMaster.ItemName = source.readString();
            objItemMaster.ItemCode = source.readString();
            objItemMaster.BarCode = source.readString();
            objItemMaster.ShortDescription = source.readString();
            objItemMaster.linktoUnitMasterId = (short) source.readInt();
            objItemMaster.linktoCategoryMasterId = (short) source.readInt();
            objItemMaster.IsFavourite = source.readByte() != 0;
            objItemMaster.linktoItemStatusMasterId = (short) source.readInt();
            objItemMaster.ItemPoint = (short) source.readInt();
            objItemMaster.PriceByPoint = (short) source.readInt();
            objItemMaster.SearchWords = source.readString();
            objItemMaster.linktoBusinessMasterId = (short) source.readInt();
            objItemMaster.SortOrder = source.readInt();
            objItemMaster.IsEnabled = source.readByte() != 0;
            objItemMaster.IsDeleted = source.readByte() != 0;
            objItemMaster.IsDineInOnly = source.readByte() != 0;
            objItemMaster.ItemType = (short) source.readInt();
            objItemMaster.CreateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdCreatedBy = (short) source.readInt();
            objItemMaster.UpdateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdUpdatedBy = (short) source.readInt();
            objItemMaster.Rate = source.readDouble();
            objItemMaster.MRP = source.readDouble();
            objItemMaster.xs_ImagePhysicalName = source.readString();
            objItemMaster.sm_ImagePhysicalName = source.readString();
            objItemMaster.md_ImagePhysicalName = source.readString();
            objItemMaster.lg_ImagePhysicalName = source.readString();
            objItemMaster.xl_ImagePhysicalName = source.readString();

            /// Extra
            objItemMaster.Unit = source.readString();
            objItemMaster.Category = source.readString();
            objItemMaster.Business = source.readString();
            objItemMaster.UserCreatedBy = source.readString();
            objItemMaster.UserUpdatedBy = source.readString();
            objItemMaster.linktoItemMasterIdModifiers = source.readString();
            objItemMaster.linktoOptionMasterIds = source.readString();
            objItemMaster.SellPrice = source.readDouble();
            objItemMaster.Tax = source.readString();
            return objItemMaster;
        }

        public ItemMaster[] newArray(int size) {
            return new ItemMaster[size];
        }
    };
    int Quantity;
    String Remark;
    double TotalAmount;
    double ExtraAmount;
    ArrayList<ItemMaster> alOrderItemModifierTran;

    public int getItemMasterId() {
        return this.ItemMasterId;
    }

    public void setItemMasterId(int itemMasterId) {
        this.ItemMasterId = itemMasterId;
    }

    public String getShortName() {
        return this.ShortName;
    }

    public void setShortName(String shortName) {
        this.ShortName = shortName;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public String getItemCode() {
        return this.ItemCode;
    }

    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }

    public String getBarCode() {
        return this.BarCode;
    }

    public void setBarCode(String barCode) {
        this.BarCode = barCode;
    }

    public String getShortDescription() {
        return this.ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.ShortDescription = shortDescription;
    }

    public short getlinktoUnitMasterId() {
        return this.linktoUnitMasterId;
    }

    public void setlinktoUnitMasterId(short linktoUnitMasterId) {
        this.linktoUnitMasterId = linktoUnitMasterId;
    }

    public short getlinktoCategoryMasterId() {
        return this.linktoCategoryMasterId;
    }

    public void setlinktoCategoryMasterId(short linktoCategoryMasterId) {
        this.linktoCategoryMasterId = linktoCategoryMasterId;
    }

    public boolean getIsFavourite() {
        return this.IsFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.IsFavourite = isFavourite;
    }

    public short getlinktoItemStatusMasterId() {
        return this.linktoItemStatusMasterId;
    }

    public void setlinktoItemStatusMasterId(short linktoItemStatusMasterId) {
        this.linktoItemStatusMasterId = linktoItemStatusMasterId;
    }

    public short getItemPoint() {
        return this.ItemPoint;
    }

    public void setItemPoint(short itemPoint) {
        this.ItemPoint = itemPoint;
    }

    public short getPriceByPoint() {
        return this.PriceByPoint;
    }

    public void setPriceByPoint(short priceByPoint) {
        this.PriceByPoint = priceByPoint;
    }

    public String getSearchWords() {
        return this.SearchWords;
    }

    public void setSearchWords(String searchWords) {
        this.SearchWords = searchWords;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public int getSortOrder() {
        return this.SortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.SortOrder = sortOrder;
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

    public boolean getIsDineInOnly() {
        return this.IsDineInOnly;
    }

    public void setIsDineInOnly(boolean isDineInOnly) {
        this.IsDineInOnly = isDineInOnly;
    }

    public short getItemType() {
        return this.ItemType;
    }

    public void setItemType(short itemType) {
        this.ItemType = itemType;
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

    public String getUnit() {
        return this.Unit;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getBusiness() {
        return this.Business;
    }

    public void setBusiness(String business) {
        this.Business = business;
    }

    public String getUserCreatedBy() {
        return this.UserCreatedBy;
    }

    public void setUserCreatedBy(String userCreatedBy) {
        this.UserCreatedBy = userCreatedBy;
    }

    public String getUserUpdatedBy() {
        return this.UserUpdatedBy;
    }

    public void setUserUpdatedBy(String userUpdatedBy) {
        this.UserUpdatedBy = userUpdatedBy;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public String getXl_ImagePhysicalName() {
        return xl_ImagePhysicalName;
    }

    public void setXl_ImagePhysicalName(String xl_ImagePhysicalName) {
        this.xl_ImagePhysicalName = xl_ImagePhysicalName;
    }

    public String getLg_ImagePhysicalName() {
        return lg_ImagePhysicalName;
    }

    public void setLg_ImagePhysicalName(String lg_ImagePhysicalName) {
        this.lg_ImagePhysicalName = lg_ImagePhysicalName;
    }

    public String getMd_ImagePhysicalName() {
        return md_ImagePhysicalName;
    }

    public void setMd_ImagePhysicalName(String md_ImagePhysicalName) {
        this.md_ImagePhysicalName = md_ImagePhysicalName;
    }

    public String getSm_ImagePhysicalName() {
        return sm_ImagePhysicalName;
    }

    public void setSm_ImagePhysicalName(String sm_ImagePhysicalName) {
        this.sm_ImagePhysicalName = sm_ImagePhysicalName;
    }

    public String getXs_ImagePhysicalName() {
        return xs_ImagePhysicalName;
    }

    public void setXs_ImagePhysicalName(String xs_ImagePhysicalName) {
        this.xs_ImagePhysicalName = xs_ImagePhysicalName;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public String getLinktoItemMasterIdModifiers() {
        return linktoItemMasterIdModifiers;
    }

    public void setLinktoItemMasterIdModifiers(String linktoItemMasterIdModifiers) {
        this.linktoItemMasterIdModifiers = linktoItemMasterIdModifiers;
    }

    public String getLinktoOptionMasterIds() {
        return linktoOptionMasterIds;
    }

    public void setLinktoOptionMasterIds(String linktoOptionMasterIds) {
        this.linktoOptionMasterIds = linktoOptionMasterIds;
    }

    public double getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(double sellPrice) {
        SellPrice = sellPrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getExtraAmount() {
        return ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        ExtraAmount = extraAmount;
    }

    public ArrayList<ItemMaster> getAlOrderItemModifierTran() {
        return alOrderItemModifierTran;
    }

    public void setAlOrderItemModifierTran(ArrayList<ItemMaster> alOrderItemModifierTran) {
        this.alOrderItemModifierTran = alOrderItemModifierTran;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        this.Tax = tax;
    }

    public double getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(double totalTax) {
        this.TotalTax = totalTax;
    }

    public double getTax5() {
        return Tax5;
    }

    public void setTax5(double tax5) {
        Tax5 = tax5;
    }

    public double getTax4() {
        return Tax4;
    }

    public void setTax4(double tax4) {
        Tax4 = tax4;
    }

    public double getTax3() {
        return Tax3;
    }

    public void setTax3(double tax3) {
        Tax3 = tax3;
    }

    public double getTax2() {
        return Tax2;
    }

    public void setTax2(double tax2) {
        Tax2 = tax2;
    }

    public double getTax1() {
        return Tax1;
    }

    public void setTax1(double tax1) {
        Tax1 = tax1;
    }
    //endregion

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(ItemMasterId);
        parcel.writeString(ShortName);
        parcel.writeString(ItemName);
        parcel.writeString(ItemCode);
        parcel.writeString(BarCode);
        parcel.writeString(ShortDescription);
        parcel.writeInt(linktoUnitMasterId);
        parcel.writeInt(linktoCategoryMasterId);
        parcel.writeByte((byte) (IsFavourite ? 1 : 0));
        parcel.writeInt(linktoItemStatusMasterId);
        parcel.writeInt(ItemPoint);
        parcel.writeInt(PriceByPoint);
        parcel.writeString(SearchWords);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte) (IsEnabled ? 1 : 0));
        parcel.writeByte((byte) (IsDeleted ? 1 : 0));
        parcel.writeByte((byte) (IsDineInOnly ? 1 : 0));
        parcel.writeInt(ItemType);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);
        parcel.writeDouble(Rate);
        parcel.writeDouble(MRP);
        parcel.writeString(xs_ImagePhysicalName);
        parcel.writeString(sm_ImagePhysicalName);
        parcel.writeString(md_ImagePhysicalName);
        parcel.writeString(lg_ImagePhysicalName);
        parcel.writeString(xl_ImagePhysicalName);

        /// Extra
        parcel.writeString(Unit);
        parcel.writeString(Category);
        parcel.writeString(Business);
        parcel.writeString(UserCreatedBy);
        parcel.writeString(UserUpdatedBy);
        parcel.writeString(linktoItemMasterIdModifiers);
        parcel.writeString(linktoOptionMasterIds);
        parcel.writeDouble(SellPrice);
        parcel.writeString(Tax);
    }
}