package com.curtisgetz.baking.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {


    private double mQuantity;
    private String mMeasure;
    private String mIngredientName;
    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {



        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };

    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mIngredientName = in.readString();

    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Ingredient() {
    }


    public Ingredient(double quantity, String measure, String ingredient) {
        super();
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mIngredientName = ingredient;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        this.mMeasure = measure;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public void setIngredientName(String ingredient) {
        this.mIngredientName = ingredient;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredientName);
    }

    public int describeContents() {
        return 0;
    }

}