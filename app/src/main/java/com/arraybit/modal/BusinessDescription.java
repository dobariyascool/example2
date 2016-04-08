package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for BusinessDescription
/// </summary>
public class BusinessDescription implements Parcelable {
	//region Properties

	short BusinessDescriptionId;
	String Keyword;
	String Description;
	short linktoBusinessMasterId;
	boolean IsDefault;
	short linktoUserMasterIdCreatedBy;
	String CreateDateTime;
	short linktoUserMasterIdUpdatedBy;
	String UpdateDateTime;
	/// Extra
	String Business;
	public static final Creator<BusinessDescription> CREATOR = new Creator<BusinessDescription>() {
		public BusinessDescription createFromParcel(Parcel source) {
			BusinessDescription objBusinessDescription = new BusinessDescription();
			objBusinessDescription.BusinessDescriptionId = (short)source.readInt();
			objBusinessDescription.Keyword = source.readString();
			objBusinessDescription.Description = source.readString();
			objBusinessDescription.linktoBusinessMasterId = (short)source.readInt();
			objBusinessDescription.IsDefault = source.readByte() != 0;
			objBusinessDescription.linktoUserMasterIdCreatedBy = (short)source.readInt();
			objBusinessDescription.CreateDateTime = source.readString();
			objBusinessDescription.linktoUserMasterIdUpdatedBy = (short)source.readInt();
			objBusinessDescription.UpdateDateTime = source.readString();

			/// Extra
			objBusinessDescription.Business = source.readString();
			return objBusinessDescription;
		}

		public BusinessDescription[] newArray(int size) {
			return new BusinessDescription[size];
		}
	};

	public short getBusinessDescriptionId() { return this.BusinessDescriptionId; }

	public void setBusinessDescriptionId(short businessDescriptionId) { this.BusinessDescriptionId = businessDescriptionId; }

	public String getKeyword() { return this.Keyword; }

	public void setKeyword(String keyword) { this.Keyword = keyword; }

	public String getDescription() { return this.Description; }

	public void setDescription(String description) { this.Description = description; }

	public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

	public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

	public boolean getIsDefault() { return this.IsDefault; }

	public void setIsDefault(boolean isDefault) { this.IsDefault = isDefault; }

	public short getlinktoUserMasterIdCreatedBy() { return this.linktoUserMasterIdCreatedBy; }

	public void setlinktoUserMasterIdCreatedBy(short linktoUserMasterIdCreatedBy) { this.linktoUserMasterIdCreatedBy = linktoUserMasterIdCreatedBy; }

	public String getCreateDateTime() { return this.CreateDateTime; }

	public void setCreateDateTime(String createDateTime) { this.CreateDateTime = createDateTime; }

	public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

	public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

	public String getUpdateDateTime() { return this.UpdateDateTime; }

	public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

	public String getBusiness() { return this.Business; }

	//endregion

	public void setBusiness(String business) { this.Business = business; }

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(BusinessDescriptionId);
		parcel.writeString(Keyword);
		parcel.writeString(Description);
		parcel.writeInt(linktoBusinessMasterId);
		parcel.writeByte((byte)(IsDefault ? 1 : 0));
		parcel.writeInt(linktoUserMasterIdCreatedBy);
		parcel.writeString(CreateDateTime);
		parcel.writeInt(linktoUserMasterIdUpdatedBy);
		parcel.writeString(UpdateDateTime);

		/// Extra
		parcel.writeString(Business);
	}
}
