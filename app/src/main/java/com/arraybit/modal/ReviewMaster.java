package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

/// <summary>
/// Model for ReviewMaster
/// </summary>
public class ReviewMaster implements Parcelable {
	//region Properties

	long ReviewMasterId;
	double StarRating;
	String Review;
	boolean IsShow;
	String ReviewDateTime;
	String UpdateDateTime;
	int linktoRegisteredUserMasterId;
	short linktoUserMasterIdUpdatedBy;
	short linktoBusinessMasterId;
	/// Extra
	String RegisteredUser;
	String UserUpdatedBy;
	String Business;
	public static final Creator<ReviewMaster> CREATOR = new Creator<ReviewMaster>() {
		public ReviewMaster createFromParcel(Parcel source) {
			ReviewMaster objReviewMaster = new ReviewMaster();
			objReviewMaster.ReviewMasterId = source.readLong();
			objReviewMaster.StarRating = source.readDouble();
			objReviewMaster.Review = source.readString();
			objReviewMaster.IsShow = source.readByte() != 0;
			objReviewMaster.ReviewDateTime = source.readString();
			objReviewMaster.UpdateDateTime = source.readString();
			objReviewMaster.linktoRegisteredUserMasterId = source.readInt();
			objReviewMaster.linktoUserMasterIdUpdatedBy = (short)source.readInt();
			objReviewMaster.linktoBusinessMasterId = (short)source.readInt();

			/// Extra
			objReviewMaster.RegisteredUser = source.readString();
			objReviewMaster.UserUpdatedBy = source.readString();
			objReviewMaster.Business = source.readString();
			return objReviewMaster;
		}

		public ReviewMaster[] newArray(int size) {
			return new ReviewMaster[size];
		}
	};

	public long getReviewMasterId() { return this.ReviewMasterId; }

	public void setReviewMasterId(long reviewMasterId) { this.ReviewMasterId = reviewMasterId; }

	public double getStarRating() { return this.StarRating; }

	public void setStarRating(double starRating) { this.StarRating = starRating; }

	public String getReview() { return this.Review; }

	public void setReview(String review) { this.Review = review; }

	public boolean getIsShow() { return this.IsShow; }

	public void setIsShow(boolean isShow) { this.IsShow = isShow; }

	public String getReviewDateTime() { return this.ReviewDateTime; }

	public void setReviewDateTime(String reviewDateTime) { this.ReviewDateTime = reviewDateTime; }

	public String getUpdateDateTime() { return this.UpdateDateTime; }

	public void setUpdateDateTime(String updateDateTime) { this.UpdateDateTime = updateDateTime; }

	public int getlinktoRegisteredUserMasterId() { return this.linktoRegisteredUserMasterId; }

	public void setlinktoRegisteredUserMasterId(int linktoRegisteredUserMasterId) { this.linktoRegisteredUserMasterId = linktoRegisteredUserMasterId; }

	public short getlinktoUserMasterIdUpdatedBy() { return this.linktoUserMasterIdUpdatedBy; }

	public void setlinktoUserMasterIdUpdatedBy(short linktoUserMasterIdUpdatedBy) { this.linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy; }

	public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

	public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

	public String getRegisteredUser() { return this.RegisteredUser; }

	public void setRegisteredUser(String registeredUser) { this.RegisteredUser = registeredUser; }

	public String getUserUpdatedBy() { return this.UserUpdatedBy; }

	public void setUserUpdatedBy(String userUpdatedBy) { this.UserUpdatedBy = userUpdatedBy; }

	public String getBusiness() { return this.Business; }

	//endregion

	public void setBusiness(String business) { this.Business = business; }

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(ReviewMasterId);
		parcel.writeDouble(StarRating);
		parcel.writeString(Review);
		parcel.writeByte((byte)(IsShow ? 1 : 0));
		parcel.writeString(ReviewDateTime);
		parcel.writeString(UpdateDateTime);
		parcel.writeInt(linktoRegisteredUserMasterId);
		parcel.writeInt(linktoUserMasterIdUpdatedBy);
		parcel.writeInt(linktoBusinessMasterId);

		/// Extra
		parcel.writeString(RegisteredUser);
		parcel.writeString(UserUpdatedBy);
		parcel.writeString(Business);
	}
}
