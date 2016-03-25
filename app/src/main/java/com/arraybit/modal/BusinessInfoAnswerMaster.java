package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessInfoAnswerMaster implements Parcelable {
    int BusinessInfoAnswerMasterId;
    int linktoBusinessInfoQuestionMasterId;
    String Answer;
    boolean IsAnswer;
    /// Extra
    String BusinessInfoQuestion;

    public static final Parcelable.Creator<BusinessInfoAnswerMaster> CREATOR = new Creator<BusinessInfoAnswerMaster>() {
        public BusinessInfoAnswerMaster createFromParcel(Parcel source) {
            BusinessInfoAnswerMaster objBusinessInfoAnswerMaster = new BusinessInfoAnswerMaster();
            objBusinessInfoAnswerMaster.BusinessInfoAnswerMasterId = source.readInt();
            objBusinessInfoAnswerMaster.linktoBusinessInfoQuestionMasterId = source.readInt();
            objBusinessInfoAnswerMaster.Answer = source.readString();
            objBusinessInfoAnswerMaster.IsAnswer = source.readByte() != 0;

            /// Extra
            objBusinessInfoAnswerMaster.BusinessInfoQuestion = source.readString();
            return objBusinessInfoAnswerMaster;
        }

        public BusinessInfoAnswerMaster[] newArray(int size) {
            return new BusinessInfoAnswerMaster[size];
        }
    };

    public int getBusinessInfoAnswerMasterId() {
        return this.BusinessInfoAnswerMasterId;
    }

    public void setBusinessInfoAnswerMasterId(int businessInfoAnswerMasterId) {
        this.BusinessInfoAnswerMasterId = businessInfoAnswerMasterId;
    }

    public int getlinktoBusinessInfoQuestionMasterId() {
        return this.linktoBusinessInfoQuestionMasterId;
    }

    public void setlinktoBusinessInfoQuestionMasterId(int linktoBusinessInfoQuestionMasterId) {
        this.linktoBusinessInfoQuestionMasterId = linktoBusinessInfoQuestionMasterId;
    }

    public String getAnswer() {
        return this.Answer;
    }

    public void setAnswer(String answer) {
        this.Answer = answer;
    }

    public boolean getIsAnswer() {
        return this.IsAnswer;
    }

    public void setIsAnswer(boolean isAnswer) {
        this.IsAnswer = isAnswer;
    }

    public String getBusinessInfoQuestion() {
        return this.BusinessInfoQuestion;
    }

    //endregion

    public void setBusinessInfoQuestion(String businessInfoQuestion) {
        this.BusinessInfoQuestion = businessInfoQuestion;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessInfoAnswerMasterId);
        parcel.writeInt(linktoBusinessInfoQuestionMasterId);
        parcel.writeString(Answer);
        parcel.writeByte((byte) (IsAnswer ? 1 : 0));

        /// Extra
        parcel.writeString(BusinessInfoQuestion);
    }
}

