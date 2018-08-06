package com.curtisgetz.baking.model;

import java.util.ArrayList;
import java.util.List;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.curtisgetz.baking.model.database.RecipeConverters;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "servings")
    private int mServings;
    @ColumnInfo(name = "image")
    private String mImage;

    @ColumnInfo(name = "ingredients")
    @TypeConverters(RecipeConverters.class)
    private List<Ingredient> mIngredients = new ArrayList<>();
    @ColumnInfo(name = "steps")
    @TypeConverters(RecipeConverters.class)
    private List<Step> mSteps = new ArrayList<>();





    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    @Ignore
    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredients = new ArrayList<>();
        in.readList(mIngredients, Ingredient.class.getClassLoader());
        mSteps = new ArrayList<>();
        in.readList(mSteps, Step.class.getClassLoader());
        mServings = in.readInt();
        mImage = in.readString();

    }

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Recipe() {
    }



    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        super();
        this.mId = id;
        this.mName = name;
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mServings = servings;
        this.mImage = image;
    }

    @Ignore
    public Recipe(int id, String name, int servings, String image){
        this.mId = id;
        this.mName = name;
        this.mServings = servings;
        this.mImage = image;
    }




    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeList(mIngredients);
        dest.writeList(mSteps);
        dest.writeDouble(mServings);
        dest.writeString(mImage);
    }


    public int getNumberOfIngredients() {
        if(mIngredients == null){
            return 0;
        }
        return mIngredients.size();
    }


    public int getNumberOfSteps(){
        if(mSteps == null){
            return 0;
        }
        return mSteps.size();
    }


    @Override
    public int describeContents() {
        return 0;
    }
}