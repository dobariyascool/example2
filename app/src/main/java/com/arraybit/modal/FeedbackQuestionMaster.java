package com.arraybit.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class FeedbackQuestionMaster implements Parcelable {

    //region Properties

    int FeedbackQuestionMasterId;
    int FeedbackRowPosition;
    short linktoBusinessMasterId;
    short linktoFeedbackQuestionGroupMasterId;
    String FeedbackQuestion;
    short QuestionType;
    int SortOrder;
    boolean IsEnabled;
    boolean IsDeleted;
    /// Extra
    String Business;
    String FeedbackQuestionGroup;
    String FeedbackAnswer;
    int linktoFeedbackAnswerMasterId;
    public static final Parcelable.Creator<FeedbackQuestionMaster> CREATOR = new Creator<FeedbackQuestionMaster>() {
        public FeedbackQuestionMaster createFromParcel(Parcel source) {
            FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
            objFeedbackQuestionMaster.FeedbackQuestionMasterId = source.readInt();
            objFeedbackQuestionMaster.linktoBusinessMasterId = (short)source.readInt();
            objFeedbackQuestionMaster.linktoFeedbackQuestionGroupMasterId = (short)source.readInt();
            objFeedbackQuestionMaster.FeedbackQuestion = source.readString();
            objFeedbackQuestionMaster.QuestionType = (short)source.readInt();
            objFeedbackQuestionMaster.SortOrder = source.readInt();
            objFeedbackQuestionMaster.IsEnabled = source.readByte() != 0;
            objFeedbackQuestionMaster.IsDeleted = source.readByte() != 0;

            /// Extra
            objFeedbackQuestionMaster.Business = source.readString();
            objFeedbackQuestionMaster.FeedbackQuestionGroup = source.readString();
            objFeedbackQuestionMaster.FeedbackAnswer = source.readString();
            objFeedbackQuestionMaster.linktoFeedbackAnswerMasterId = source.readInt();
            return objFeedbackQuestionMaster;
        }

        public FeedbackQuestionMaster[] newArray(int size) {
            return new FeedbackQuestionMaster[size];
        }
    };
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster;

    public int getFeedbackQuestionMasterId() { return this.FeedbackQuestionMasterId; }

    public void setFeedbackQuestionMasterId(int feedbackQuestionMasterId) { this.FeedbackQuestionMasterId = feedbackQuestionMasterId; }

    public int getFeedbackRowPosition() { return this.FeedbackRowPosition; }

    public void setFeedbackRowPosition(int feedbackRowPosition) { this.FeedbackRowPosition = feedbackRowPosition; }

    public short getlinktoBusinessMasterId() { return this.linktoBusinessMasterId; }

    public void setlinktoBusinessMasterId(short linktoBusinessMasterId) { this.linktoBusinessMasterId = linktoBusinessMasterId; }

    public short getlinktoFeedbackQuestionGroupMasterId() { return this.linktoFeedbackQuestionGroupMasterId; }

    public void setlinktoFeedbackQuestionGroupMasterId(short linktoFeedbackQuestionGroupMasterId) { this.linktoFeedbackQuestionGroupMasterId = linktoFeedbackQuestionGroupMasterId; }

    public String getFeedbackQuestion() { return this.FeedbackQuestion; }

    public void setFeedbackQuestion(String feedbackQuestion) { this.FeedbackQuestion = feedbackQuestion; }

    public short getQuestionType() { return this.QuestionType; }

    public void setQuestionType(short questionType) { this.QuestionType = questionType; }

    public int getSortOrder() { return this.SortOrder; }

    public void setSortOrder(int sortOrder) { this.SortOrder = sortOrder; }

    public boolean getIsEnabled() { return this.IsEnabled; }

    public void setIsEnabled(boolean isEnabled) { this.IsEnabled = isEnabled; }

    public boolean getIsDeleted() { return this.IsDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.IsDeleted = isDeleted; }

    public String getBusiness() { return this.Business; }

    public void setBusiness(String business) { this.Business = business; }

    public String getFeedbackQuestionGroup() { return this.FeedbackQuestionGroup; }

    public void setFeedbackQuestionGroup(String feedbackQuestionGroup) { this.FeedbackQuestionGroup = feedbackQuestionGroup; }

    public String getFeedbackAnswer() { return this.FeedbackAnswer; }

    public void setFeedbackAnswer(String feedbackAnswer) { this.FeedbackAnswer = feedbackAnswer; }

    public int getlinktoFeedbackAnswerMasterId() { return this.linktoFeedbackAnswerMasterId; }

    public void setlinktoFeedbackAnswerMasterId(int linktoFeedbackAnswerMasterId) { this.linktoFeedbackAnswerMasterId = linktoFeedbackAnswerMasterId; }

    public ArrayList<FeedbackAnswerMaster> getAlFeedbackAnswerMaster() { return alFeedbackAnswerMaster; }

    //endregion

    public void setAlFeedbackAnswerMaster(ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster) { this.alFeedbackAnswerMaster = alFeedbackAnswerMaster; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(FeedbackQuestionMasterId);
        parcel.writeInt(linktoBusinessMasterId);
        parcel.writeInt(linktoFeedbackQuestionGroupMasterId);
        parcel.writeString(FeedbackQuestion);
        parcel.writeInt(QuestionType);
        parcel.writeInt(SortOrder);
        parcel.writeByte((byte)(IsEnabled ? 1 : 0));
        parcel.writeByte((byte)(IsDeleted ? 1 : 0));

        /// Extra
        parcel.writeString(Business);
        parcel.writeString(FeedbackQuestionGroup);
        parcel.writeString(FeedbackAnswer);
        parcel.writeInt(linktoFeedbackAnswerMasterId);
    }


}
