package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

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
    String ImageNameBytes;
    String ImageName;
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
    /// Extra
    String Unit;
    String Category;
    String Business;
    String UserCreatedBy;
    String UserUpdatedBy;
    public static final Parcelable.Creator<ItemMaster> CREATOR = new Creator<ItemMaster>() {
        public ItemMaster createFromParcel(Parcel source) {
            ItemMaster objItemMaster = new ItemMaster();
            objItemMaster.ItemMasterId = source.readInt();
            objItemMaster.ShortName = source.readString();
            objItemMaster.ItemName = source.readString();
            objItemMaster.ItemCode = source.readString();
            objItemMaster.BarCode = source.readString();
            objItemMaster.ShortDescription = source.readString();
            objItemMaster.linktoUnitMasterId = (short)source.readInt();
            objItemMaster.linktoCategoryMasterId = (short)source.readInt();
            objItemMaster.IsFavourite = source.readByte() != 0;
            objItemMaster.ImageNameBytes = source.readString();
            objItemMaster.ImageName = source.readString();
            objItemMaster.linktoItemStatusMasterId = (short)source.readInt();
            objItemMaster.ItemPoint = (short)source.readInt();
            objItemMaster.PriceByPoint = (short)source.readInt();
            objItemMaster.SearchWords = source.readString();
            objItemMaster.linktoBusinessMasterId = (short)source.readInt();
            objItemMaster.SortOrder = source.readInt();
            objItemMaster.IsEnabled = source.readByte() != 0;
            objItemMaster.IsDeleted = source.readByte() != 0;
            objItemMaster.IsDineInOnly = source.readByte() != 0;
            objItemMaster.ItemType = (short)source.readInt();
            objItemMaster.CreateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objItemMaster.UpdateDateTime = source.readString();
            objItemMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();

            /// Extra
            objItemMaster.Unit = source.readString();
            objItemMaster.Category = source.readString();
            objItemMaster.Business = source.readString();
            objItemMaster.UserCreatedBy = source.readString();
            objItemMaster.UserUpdatedBy = source.readString();
            return objItemMaster;
        }

        public ItemMaster[] newArray(int size) {
            return new ItemMaster[size];
        }
    };

    public int getItemMasterId() { return this.ItemMasterId; }

    public void setItemMasterId(int itemMasterId) { this.ItemMasterId = itemMasterId; }

    public String getShortName() { return this.ShortName; }

    public void setShortName(String shortName) { this.ShortName = shortName; }

    public String getItemName() { return this.ItemName; }

    public void setItemName(String itemName) { this.ItemName = itemName; }

    public String getItemCode() { return this.ItemCode; }

    public void setItemCode(String itemCode) { this.ItemCode = itemCode; }

    public String getBarCode() { return this.BarCode; }

    public void setBarCode(String barCode) { this.BarCode = barCode; }

    public String getShortDescription() { return this.ShortDescription; }

    public void setShortDescription(String shortDescription) { this.ShortDescription = shortDescription; }

    public short getlinktoUnitMasterId() { return this.linktoUnitMasterId; }

    public void setlinktoUnitMasterId(short linktoUnitMasterId) { this.linktoUnitMasterId = linktoUnitMasterId; }

    public short getlinktoCategoryMasterId() { return this.linktoCategoryMasterId; }

    public void setlinktoCategoryMasterId(short linktoCategoryMasterId) { this.linktoCategoryMasterId = linktoCategoryMasterId; }

    public boolean getIsFavourite() { return this.IsFavourite; }

    public void setIsFavourite(boolean isFavourite) { this.IsFavourite = isFavourite; }

    public String getImageNameBytes() { return this.ImageNameBytes; }

    public void setImageNameBytes(String imageNameBytes) { this.ImageNameBytes = imageNameBytes; }

    public String getImageName() { return this.ImageName; }

    public void setImageName(String imageName) { this.ImageName = imageName; }

    public short getlinktoItemStatusMasterId() { return this.linktoItemStatusMasterId; }

    public void setlinktoItemStatusMasterId(short linktoItemStatusMasterId) { this.linktoItemStatusMasterId = linktoItemStatusMasterId; }

    public short getItemPoint() { return this.ItemPoint; }

    public void setItemPoint(short itemPoint) { this.ItemPoint = itemPoint; }

    public short getPriceByPoint() { return this.PriceByPoint; }

    public void setPriceByPoint(short priceByPoint) { this.PriceByPoint = priceByPoint; }

    public String getSearchWords() { return this.SearchWords; }

    public void setSearchWords(String searchWords) { this.SearchWords = searchWords; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public int getSortOrder() { return this.SortOrder; }

    public void setSortOrder(int sortOrder) { this.SortOrder = sortOrder; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public boolean getIsDineInOnly() { return this.IsDineInOnly; }

    public void setIsDineInOnly(boolean isDineInOnly) { this.IsDineInOnly = isDineInOnly; }

    public short getItemType() { return this.ItemType; }

    public void setItemType(short itemType) { this.ItemType = itemType; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public String getUnit() { return this.Unit; }

    public void setUnit(String unit) { this.Unit = unit; }

    public String getCategory() { return this.Category; }

    public void setCategory(String category) { this.Category = category; }

    public String getBusiness() { return this.Business; }

    public void setBusiness(String business) { this.Business = business; }

    public String getUserCreatedBy() { return this.UserCreatedBy; }

    public void setUserCreatedBy(String userCreatedBy) { this.UserCreatedBy = userCreatedBy; }

    public String getUserUpdatedBy() { return this.UserUpdatedBy; }

    //endregion

    public void setUserUpdatedBy(String userUpdatedBy) { this.UserUpdatedBy = userUpdatedBy; }

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
        parcel.writeByte((byte)(IsFavourite ? 1 : 0));
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeInt(linktoItemStatusMasterId);
        parcel.writeInt(ItemPoint);
        parcel.writeInt(PriceByPoint);
        parcel.writeString(SearchWords);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));
        parcel.writeByte((byte)(IsDineInOnly ? 1 : 0));
        parcel.writeInt(ItemType);
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeString(Unit);
        parcel.writeString(Category);
        parcel.writeString(Business);
        parcel.writeString(UserCreatedBy);
        parcel.writeString(UserUpdatedBy);
    }
}