package com.curtisgetz.baking.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "recipe_steps")
public class Step implements Parcelable {



   // @ColumnInfo(name = "step_name")
    private int mId;
//    @ColumnInfo(name = "short_desc")
    private String mShortDescription;
 //   @ColumnInfo(name = "description")
    private String mDescription;
 //   @ColumnInfo(name = "video_url")
    private String mVideoURL;
 //   @ColumnInfo(name = "thumbnail_url")
    private String mThumbnailURL;
 //   @ColumnInfo(name = "recipe_id")
    private int mRecipeId;

    public final static Parcelable.Creator<Step> CREATOR = new Creator<Step>() {

        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return (new Step[size]);
        }

    };

    protected Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
     //   mRecipeId = in.readInt();

    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Step() {
    }


    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        super();
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoURL = videoURL;
        this.mThumbnailURL = thumbnailURL;
        //this.mRecipeId = recipeId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public void setVideoURL(String videoURL) {
        this.mVideoURL = videoURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.mThumbnailURL = thumbnailURL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoURL);
        dest.writeString(mThumbnailURL);
        //dest.writeInt(mRecipeId);
    }

    public int getmRecipeId() {
        return mRecipeId;
    }

    public void setmRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public int describeContents() {
        return 0;
    }

}


