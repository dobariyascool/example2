package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessServiceTran implements Parcelable {
    //region Properties

    short BusinessServiceTranId;
    short linktoServiceMasterId;
    short linktoBusinessMasterId;
    /// Extra
    String ServiceName;
    String ImageName;
    public static final Parcelable.Creator<BusinessServiceTran> CREATOR = new Creator<BusinessServiceTran>() {
        public BusinessServiceTran createFromParcel(Parcel source) {
            BusinessServiceTran objBusinessServiceTran = new BusinessServiceTran();
            objBusinessServiceTran.BusinessServiceTranId = (short) source.readInt();
            objBusinessServiceTran.linktoServiceMasterId = (short) source.readInt();
            objBusinessServiceTran.linktoBusinessMasterId = (short) source.readInt();

            /// Extra
            objBusinessServiceTran.ServiceName = source.readString();
            objBusinessServiceTran.ImageName = source.readString();
            return objBusinessServiceTran;
        }

        public BusinessServiceTran[] newArray(int size) {
            return new BusinessServiceTran[size];
        }
    };
    String XSImagePhysicalName;

    public short getBusinessServiceTran() {
        return this.BusinessServiceTranId;
    }

    public void setBusinessServiceTran(short businessServiceTranId) {
        this.BusinessServiceTranId = businessServiceTranId;
    }

    public short getlinktoServiceMasterId() {
        return this.linktoServiceMasterId;
    }

    public void setlinktoServiceMasterId(short linktoServiceMasterId) {
        this.linktoServiceMasterId = linktoServiceMasterId;
    }

    public short getlinktoBusinessMasterId() {
        return this.linktoBusinessMasterId;
    }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) {
        this.linktoBusinessMasterId = linktoBusinessMasterId;
    }

    public String getService() {
        return this.ServiceName;
    }

    public void setService(String serviceName) {
        this.ServiceName = serviceName;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }


    public String getXSImagePhysicalName() {
        return this.XSImagePhysicalName;
    }

    public void setXSImagePhysicalName(String xsImagePhysicalName) {
        this.XSImagePhysicalName = xsImagePhysicalName;
    }

    //endregion


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessServiceTranId);
        parcel.writeInt(linktoServiceMasterId);
        parcel.writeInt(linktoBusinessMasterId);

        /// Extra
        parcel.writeString(ServiceName);
        parcel.writeString(ImageName);
        parcel.writeString(XSImagePhysicalName);
    }
}
