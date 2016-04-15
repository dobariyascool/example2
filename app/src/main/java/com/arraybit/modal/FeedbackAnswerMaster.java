package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackAnswerMaster implements Parcelable {
    //region Properties

    int FeedbackAnswerMasterId;
    int linktoFeedbackQuestionMasterId;
    String Answer;
    boolean IsDeleted;
    /// Extra
    String FeedbackQuestion;
    public static final Creator<FeedbackAnswerMaster> CREATOR = new Creator<FeedbackAnswerMaster>() {
        public FeedbackAnswerMaster createFromParcel(Parcel source) {
            FeedbackAnswerMaster objFeedbackAnswerMaster = new FeedbackAnswerMaster();
            objFeedbackAnswerMaster.FeedbackAnswerMasterId = source.readInt();
            objFeedbackAnswerMaster.linktoFeedbackQuestionMasterId = source.readInt();
            objFeedbackAnswerMaster.Answer = source.readString();
            objFeedbackAnswerMaster.IsDeleted = source.readByte() != 0;

            /// Extra
            objFeedbackAnswerMaster.FeedbackQuestion = source.readString();
            return objFeedbackAnswerMaster;
        }

        public FeedbackAnswerMaster[] newArray(int size) {
            return new FeedbackAnswerMaster[size];
        }
    };

    public int getFeedbackAnswerMasterId() { return this.FeedbackAnswerMasterId; }

    public void setFeedbackAnswerMasterId(int feedbackAnswerMasterId) { this.FeedbackAnswerMasterId = feedbackAnswerMasterId; }

    public int getlinktoFeedbackQuestionMasterId() { return this.linktoFeedbackQuestionMasterId; }

    public void setlinktoFeedbackQuestionMasterId(int linktoFeedbackQuestionMasterId) { this.linktoFeedbackQuestionMasterId = linktoFeedbackQuestionMasterId; }

    public String getAnswer() { return this.Answer; }

    public void setAnswer(String answer) { this.Answer = answer; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getFeedbackQuestion() { return this.FeedbackQuestion; }

    //endregion

    public void setFeedbackQuestion(String feedbackQuestion) { this.FeedbackQuestion = feedbackQuestion; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackAnswerMasterId);
        parcel.writeInt(linktoFeedbackQuestionMasterId);
        parcel.writeString(Answer);
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(FeedbackQuestion);
    }
}
