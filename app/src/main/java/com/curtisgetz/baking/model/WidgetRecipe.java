package com.curtisgetz.baking.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "widget_recipe")
public class WidgetRecipe {

    @PrimaryKey
    private int widget_key;

    @ColumnInfo(name = "widget_recipe_id")
    private int mRecipeId;


    public WidgetRecipe() {
    }

    @Ignore
    public WidgetRecipe(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }



    public int getWidget_key() {
        return widget_key;
    }

    public void setWidget_key(int widget_key) {
        this.widget_key = widget_key;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }
}
