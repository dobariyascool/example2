package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessGalleryTran implements Parcelable {

    //region Properties

    int BusinessGalleryTranId;
    public int getBusinessGalleryTranId() { return this.BusinessGalleryTranId; }
    public void setBusinessGalleryTranId(int businessGalleryTranId) { this.BusinessGalleryTranId = businessGalleryTranId; }

    String ImageTitle;
    public String getImageTitle() { return this.ImageTitle; }
    public void setImageTitle(String imageTitle) { this.ImageTitle = imageTitle; }

    String xs_ImagePhysicalName;
    public String getXSImagePhysicalName() { return this.xs_ImagePhysicalName; }
    public void setXSImagePhysicalName(String xs_ImagePhysicalName) { this.xs_ImagePhysicalName = xs_ImagePhysicalName; }

    String sm_ImagePhysicalName;
    public String getSMImagePhysicalName() { return this.sm_ImagePhysicalName; }
    public void setSMImagePhysicalName(String sm_ImagePhysicalName) { this.sm_ImagePhysicalName = sm_ImagePhysicalName; }

    String md_ImagePhysicalName;
    public String getMDImagePhysicalName() { return this.md_ImagePhysicalName; }
    public void setMDImagePhysicalName(String md_ImagePhysicalName) { this.md_ImagePhysicalName = md_ImagePhysicalName; }

    String lg_ImagePhysicalName;
    public String getLGImagePhysicalName() { return this.lg_ImagePhysicalName; }
    public void setLGImagePhysicalName(String lg_ImagePhysicalName) { this.lg_ImagePhysicalName = lg_ImagePhysicalName; }

    String xl_ImagePhysicalName;
    public String getXLImagePhysicalName() { return this.xl_ImagePhysicalName; }
    public void setXLImagePhysicalName(String xl_ImagePhysicalName) { this.xl_ImagePhysicalName = xl_ImagePhysicalName; }

    short linktoBusinessMasterId;
    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }
    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    short SortOrder;
    public short getSortOrder() { return this.SortOrder; }
    public void setSortOrder(short sortOrder) { this.SortOrder = sortOrder; }


    /// Extra
    String Business;
    public String getBusiness() { return this.Business; }
    public void setBusiness(String business) { this.Business = business; }

    //endregion

    public static final Parcelable.Creator<BusinessGalleryTran> CREATOR = new Creator<BusinessGalleryTran>() {
        public BusinessGalleryTran createFromParcel(Parcel source) {
            BusinessGalleryTran objBusinessGalleryTran = new BusinessGalleryTran();
            objBusinessGalleryTran.BusinessGalleryTranId = source.readInt();
            objBusinessGalleryTran.ImageTitle = source.readString();
            objBusinessGalleryTran.xs_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.sm_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.md_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.lg_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.xl_ImagePhysicalName = source.readString();
            objBusinessGalleryTran.linktoBusinessMasterId = (short)source.readInt();
            objBusinessGalleryTran.SortOrder = (short)source.readInt();

            /// Extra
            objBusinessGalleryTran.Business = source.readString();
            return objBusinessGalleryTran;
        }

        public BusinessGalleryTran[] newArray(int size) {
            return new BusinessGalleryTran[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessGalleryTranId);
        parcel.writeString(ImageTitle);
        parcel.writeString(xs_ImagePhysicalName);
        parcel.writeString(sm_ImagePhysicalName);
        parcel.writeString(md_ImagePhysicalName);
        parcel.writeString(lg_ImagePhysicalName);
        parcel.writeString(xl_ImagePhysicalName);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(SortOrder);

        /// Extra
        parcel.writeString(Business);
    }
}
