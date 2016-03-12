package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessHoursTran implements Parcelable {

    //region Properties

    short BusinessHoursTranId;
    public short getBusinessHoursTranId() { return this.BusinessHoursTranId; }
    public void setBusinessHoursTranId(short businessHoursTranId) { this.BusinessHoursTranId = businessHoursTranId; }

    short DayOfWeek;
    public short getDayOfWeek() { return this.DayOfWeek; }
    public void setDayOfWeek(short dayOfWeek) { this.DayOfWeek = dayOfWeek; }

    String OpeningTime;
    public String getOpeningTime() { return this.OpeningTime; }
    public void setOpeningTime(String openingTime) { this.OpeningTime = openingTime; }

    String ClosingTime;
    public String getClosingTime() { return this.ClosingTime; }
    public void setClosingTime(String closingTime) { this.ClosingTime = closingTime; }

    String BreakStartTime;
    public String getBreakStartTime() { return this.BreakStartTime; }
    public void setBreakStartTime(String breakStartTime) { this.BreakStartTime = breakStartTime; }

    String BreakEndTime;
    public String getBreakEndTime() { return this.BreakEndTime; }
    public void setBreakEndTime(String breakEndTime) { this.BreakEndTime = breakEndTime; }

    short linktoBusinessMasterId;
    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }
    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }


    /// Extra
    String Business;
    public String getBusiness() { return this.Business; }
    public void setBusiness(String business) { this.Business = business; }

    //endregion

    public static final Parcelable.Creator<BusinessHoursTran> CREATOR = new Creator<BusinessHoursTran>() {
        public BusinessHoursTran createFromParcel(Parcel source) {
            BusinessHoursTran objBusinessHoursTran = new BusinessHoursTran();
            objBusinessHoursTran.BusinessHoursTranId = (short)source.readInt();
            objBusinessHoursTran.DayOfWeek = (short)source.readInt();
            objBusinessHoursTran.OpeningTime = source.readString();
            objBusinessHoursTran.ClosingTime = source.readString();
            objBusinessHoursTran.BreakStartTime = source.readString();
            objBusinessHoursTran.BreakEndTime = source.readString();
            objBusinessHoursTran.linktoBusinessMasterId = (short)source.readInt();

            /// Extra
            objBusinessHoursTran.Business = source.readString();
            return objBusinessHoursTran;
        }

        public BusinessHoursTran[] newArray(int size) {
            return new BusinessHoursTran[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessHoursTranId);
        parcel.writeInt(DayOfWeek);
        parcel.writeString(OpeningTime);
        parcel.writeString(ClosingTime);
        parcel.writeString(BreakStartTime);
        parcel.writeString(BreakEndTime);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(Business);
    }
}
