package com.fakefacebook;

import android.os.Parcel;
import android.os.Parcelable;

public class PostModel implements Parcelable {
    private String userName;
    private String textField;
    private String imageUrlField;
    private String dateTime;
    private String postID;

    public PostModel() {
    }

    public PostModel(String postID, String userName, String dateTime, String textField, String imageUrlField) {
        this.userName = userName;
        this.textField = textField;
        this.dateTime = dateTime;
        this.imageUrlField = imageUrlField;
        this.postID = postID;
    }

    protected PostModel(Parcel in) {
        userName = in.readString();
        textField = in.readString();
        imageUrlField = in.readString();
        dateTime = in.readString();
        postID = in.readString();
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public String getImageUrlField() {
        return imageUrlField;
    }

    public void setImageUrlField(String imageUrlField) {
        this.imageUrlField = imageUrlField;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(textField);
        parcel.writeString(imageUrlField);
        parcel.writeString(dateTime);
        parcel.writeString(postID);
    }
}
