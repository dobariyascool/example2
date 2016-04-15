package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for CategoryMaster
/// </summary>
public class CategoryMaster implements Parcelable {
    //region Properties

    short CategoryMasterId;
    String CategoryName;
    short linktoCategoryMasterIdParent;
    String ImageNameBytes;
    String ImageName;
    String CategoryColor;
    String Description;
    short linktoBusinessMasterId;
    short SortOrder;
    String SEOPageTitle;
    String SEOMetaDescription;
    String SEOMetaKeywords;
    boolean IsEnabled;
    boolean IsDeleted;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    /// Extra
    String CategoryParent;
    String Business;
    public static final Creator<CategoryMaster> CREATOR = new Creator<CategoryMaster>() {
        public CategoryMaster createFromParcel(Parcel source) {
            CategoryMaster objCategoryMaster = new CategoryMaster();
            objCategoryMaster.CategoryMasterId = (short)source.readInt();
            objCategoryMaster.CategoryName = source.readString();
            objCategoryMaster.linktoCategoryMasterIdParent = (short)source.readInt();
            objCategoryMaster.ImageNameBytes = source.readString();
            objCategoryMaster.ImageName = source.readString();
            objCategoryMaster.CategoryColor = source.readString();
            objCategoryMaster.Description = source.readString();
            objCategoryMaster.linktoBusinessMasterId = (short)source.readInt();
            objCategoryMaster.SortOrder = (short)source.readInt();
            objCategoryMaster.SEOPageTitle = source.readString();
            objCategoryMaster.SEOMetaDescription = source.readString();
            objCategoryMaster.SEOMetaKeywords = source.readString();
            objCategoryMaster.IsEnabled = source.readByte() != 0;
            objCategoryMaster.IsDeleted = source.readByte() != 0;
            objCategoryMaster.CreateDateTime = source.readString();
            objCategoryMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objCategoryMaster.UpdateDateTime = source.readString();
            objCategoryMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();

            /// Extra
            objCategoryMaster.CategoryParent = source.readString();
            objCategoryMaster.Business = source.readString();
            return objCategoryMaster;
        }

        public CategoryMaster[] newArray(int size) {
            return new CategoryMaster[size];
        }
    };

    public short getCategoryMasterId() { return this.CategoryMasterId; }

    public void setCategoryMasterId(short categoryMasterId) { this.CategoryMasterId = categoryMasterId; }

    public String getCategoryName() { return this.CategoryName; }

    public void setCategoryName(String categoryName) { this.CategoryName = categoryName; }

    public short getlinktoCategoryMasterIdParent() { return this.linktoCategoryMasterIdParent; }

    public void setlinktoCategoryMasterIdParent(short linktoCategoryMasterIdParent) { this.linktoCategoryMasterIdParent = linktoCategoryMasterIdParent; }

    public String getImageNameBytes() { return this.ImageNameBytes; }

    public void setImageNameBytes(String imageNameBytes) { this.ImageNameBytes = imageNameBytes; }

    public String getImageName() { return this.ImageName; }

    public void setImageName(String imageName) { this.ImageName = imageName; }

    public String getCategoryColor() { return this.CategoryColor; }

    public void setCategoryColor(String categoryColor) { this.CategoryColor = categoryColor; }

    public String getDescription() { return this.Description; }

    public void setDescription(String description) { this.Description = description; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public short getSortOrder() { return this.SortOrder; }

    public void setSortOrder(short sortOrder) { this.SortOrder = sortOrder; }

    public String getSEOPageTitle() { return this.SEOPageTitle; }

    public void setSEOPageTitle(String sEOPageTitle) { this.SEOPageTitle = sEOPageTitle; }

    public String getSEOMetaDescription() { return this.SEOMetaDescription; }

    public void setSEOMetaDescription(String sEOMetaDescription) { this.SEOMetaDescription = sEOMetaDescription; }

    public String getSEOMetaKeywords() { return this.SEOMetaKeywords; }

    public void setSEOMetaKeywords(String sEOMetaKeywords) { this.SEOMetaKeywords = sEOMetaKeywords; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getCreateDateTime() { return this.CreateDateTime; }

    public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

    public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

    public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

    public String getUpdateDateTime() { return this.UpdateDateTime; }

    public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

    public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

    public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

    public String getCategoryParent() { return this.CategoryParent; }

    public void setCategoryParent(String categoryParent) { this.CategoryParent = categoryParent; }

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(CategoryMasterId);
        parcel.writeString(CategoryName);
        parcel.writeInt(linktoCategoryMasterIdParent);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(CategoryColor);
        parcel.writeString(Description);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);
        parcel.writeString(SEOPageTitle);
        parcel.writeString(SEOMetaDescription);
        parcel.writeString(SEOMetaKeywords);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeString(CategoryParent);
        parcel.writeString(Business);
    }
}

