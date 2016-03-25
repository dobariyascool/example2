package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/// <summary>
/// Model for BusinessInfoQuestionMaster
/// </summary>
public class BusinessInfoQuestionMaster implements Parcelable {
    //region Properties

    int BusinessInfoQuestionMasterId;
    short linktoBusinessTypeMasterId;
    String Question;
    short QuestionType;
    String Answer;
    boolean IsAnswer;
    public static final Parcelable.Creator<BusinessInfoQuestionMaster> CREATOR = new Creator<BusinessInfoQuestionMaster>() {
        public BusinessInfoQuestionMaster createFromParcel(Parcel source) {
            BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = new BusinessInfoQuestionMaster();
            objBusinessInfoQuestionMaster.BusinessInfoQuestionMasterId = source.readInt();
            objBusinessInfoQuestionMaster.linktoBusinessTypeMasterId = (short) source.readInt();
            objBusinessInfoQuestionMaster.Question = source.readString();
            objBusinessInfoQuestionMaster.QuestionType = (short) source.readInt();
            objBusinessInfoQuestionMaster.Answer = source.readString();
            objBusinessInfoQuestionMaster.IsAnswer = source.readByte() != 0;


            /// Extra

            return objBusinessInfoQuestionMaster;
        }

        public BusinessInfoQuestionMaster[] newArray(int size) {
            return new BusinessInfoQuestionMaster[size];
        }
    };
    /// Extra
    ArrayList<BusinessInfoAnswerMaster> alBusinessInfoAnswerMaster;

    public int getBusinessInfoQuestionMasterId() {
        return this.BusinessInfoQuestionMasterId;
    }

    public void setBusinessInfoQuestionMasterId(int businessInfoQuestionMasterId) {
        this.BusinessInfoQuestionMasterId = businessInfoQuestionMasterId;
    }

    public short getlinktoBusinessTypeMasterId() {
        return this.linktoBusinessTypeMasterId;
    }

    public void setlinktoBusinessTypeMasterId(short linktoBusinessTypeMasterId) {
        this.linktoBusinessTypeMasterId = linktoBusinessTypeMasterId;
    }

    public String getQuestion() {
        return this.Question;
    }

    public void setQuestion(String question) {
        this.Question = question;
    }

    public short getQuestionType() {
        return this.QuestionType;
    }

    public void setQuestionType(short questionType) {
        this.QuestionType = questionType;
    }
    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public boolean getIsAnswer() {
        return IsAnswer;
    }

    public void setIsAnswer(boolean isAnswer) {
        IsAnswer = isAnswer;
    }
    public ArrayList<BusinessInfoAnswerMaster> getAlBusinessInfoAnswerMaster() {
        return alBusinessInfoAnswerMaster;
    }

    public void setAlBusinessInfoAnswerMaster(ArrayList<BusinessInfoAnswerMaster> alBusinessInfoAnswerMaster) {
        this.alBusinessInfoAnswerMaster = alBusinessInfoAnswerMaster;
    }
    //endregion


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(BusinessInfoQuestionMasterId);
        parcel.writeInt(linktoBusinessTypeMasterId);
        parcel.writeString(Question);
        parcel.writeInt(QuestionType);
        parcel.writeString(Answer);
        parcel.writeByte((byte) (IsAnswer ? 1 : 0));
        /// Extra
    }
}
