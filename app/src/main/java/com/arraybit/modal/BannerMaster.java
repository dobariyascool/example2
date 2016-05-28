package com.arraybit.modal;


import android.os.Parcel;
import android.os.Parcelable;

public class BannerMaster implements Parcelable {

    //region Properties

    int BannerMasterId;
    short linktoBusinessMasterId;
    String BannerTitle;
    String BannerDescription;
    String ImageNameBytes;
    String ImageName;
    String LGImageName;
    String FromDate;
    String ToDate;
    short Type;
    int ID;
    short SortOrder;
    boolean IsEnabled;
    boolean IsDeleted;
    String CreateDateTime;
    short linktoUserMasterIdCreatedBy;
    String UpdateDateTime;
    short linktoUserMasterIdUpdatedBy;
    /// Extra
    String Business;
    public static final Parcelable.Creator<BannerMaster> CREATOR = new Creator<BannerMaster>() {
        public BannerMaster createFromParcel(Parcel source) {
            BannerMaster objBannerMaster = new BannerMaster();
            objBannerMaster.BannerMasterId = source.readInt();
            objBannerMaster.linktoBusinessMasterId = (short)source.readInt();
            objBannerMaster.BannerTitle = source.readString();
            objBannerMaster.BannerDescription = source.readString();
            objBannerMaster.ImageNameBytes = source.readString();
            objBannerMaster.ImageName = source.readString();
            objBannerMaster.LGImageName = source.readString();
            objBannerMaster.FromDate = source.readString();
            objBannerMaster.ToDate = source.readString();
            objBannerMaster.Type = (short)source.readInt();
            objBannerMaster.ID = source.readInt();
            objBannerMaster.SortOrder = (short)source.readInt();
            objBannerMaster.IsEnabled = source.readByte() != 0;
            objBannerMaster.IsDeleted = source.readByte() != 0;
            objBannerMaster.CreateDateTime = source.readString();
            objBannerMaster.linktoUserMasterIdCreatedBy = (short)source.readInt();
            objBannerMaster.UpdateDateTime = source.readString();
            objBannerMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();

            /// Extra
            objBannerMaster.Business = source.readString();
            return objBannerMaster;
        }

        public BannerMaster[] newArray(int size) {
            return new BannerMaster[size];
        }
    };

    public int getBannerMasterId() { return this.BannerMasterId; }

    public void setBannerMasterId(int bannerMasterId) { this.BannerMasterId = bannerMasterId; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public String getBannerTitle() { return this.BannerTitle; }

    public void setBannerTitle(String bannerTitle) { this.BannerTitle = bannerTitle; }

    public String getBannerDescription() { return this.BannerDescription; }

    public void setBannerDescription(String bannerDescription) { this.BannerDescription = bannerDescription; }

    public String getImageNameBytes() { return this.ImageNameBytes; }

    public void setImageNameBytes(String imageNameBytes) { this.ImageNameBytes = imageNameBytes; }

    public String getImageName() { return this.ImageName; }

    public void setImageName(String imageName) { this.ImageName = imageName; }

    public String getLGImageName() { return this.ImageName; }

    public void setLGImageName(String imageName) { this.ImageName = imageName; }

    public String getFromDate() { return this.FromDate; }

    public void setFromDate(String fromDate) { this.FromDate = fromDate; }

    public String getToDate() { return this.ToDate; }

    public void setToDate(String toDate) { this.ToDate = toDate; }

    public short getType() { return this.Type; }

    public void setType(short type) { this.Type = type; }

    public int getID() { return this.ID; }

    public void setID(int iD) { this.ID = iD; }

    public short getSortOrder() { return this.SortOrder; }

    public void setSortOrder(short sortOrder) { this.SortOrder = sortOrder; }

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

    public String getBusiness() { return this.Business; }

    //endregion

    public void setBusiness(String business) { this.Business = business; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BannerMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeString(BannerTitle);
        parcel.writeString(BannerDescription);
        parcel.writeString(ImageNameBytes);
        parcel.writeString(ImageName);
        parcel.writeString(LGImageName);
        parcel.writeString(FromDate);
        parcel.writeString(ToDate);
        parcel.writeInt(Type);
        parcel.writeInt(ID);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));
        parcel.writeString(CreateDateTime);
        parcel.writeInt(linktoUserMasterIdCreatedBy);
        parcel.writeString(UpdateDateTime);
        parcel.writeInt(linktoUserMasterIdUpdatedBy);

        /// Extra
        parcel.writeString(Business);
    }
}
