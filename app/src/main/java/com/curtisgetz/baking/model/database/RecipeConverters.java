package com.curtisgetz.baking.model.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;


import com.curtisgetz.baking.model.Ingredient;
import com.curtisgetz.baking.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeConverters {

    private final static String TAG = RecipeConverters.class.getSimpleName();

    @TypeConverter
    public static List<Step> stepsFromString(String value){
        Type listType = new TypeToken<List<Step>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String fromStepList(List<Step> list){ ;
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Ingredient> ingredientFromString(String value){
        Type listType = new TypeToken<List<Ingredient>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> list){
        Gson gson = new Gson();
        return gson.toJson(list);

    }


}
