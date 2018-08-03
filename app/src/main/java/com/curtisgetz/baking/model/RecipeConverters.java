package com.curtisgetz.baking.model;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.net.sip.SipSession;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeConverters {

    private final static String TAG = RecipeConverters.class.getSimpleName();

    @TypeConverter
    public static List<Step> stepsFromString(String value){
        Log.e(TAG, "stepsFromString");
        Type listType = new TypeToken<List<Step>>(){}.getType();

        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String fromStepList(List<Step> list){
        Log.e(TAG, "fromStepList");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;

    }

    @TypeConverter
    public static List<Ingredient> ingredientFromString(String value){
        Log.e(TAG, "ingredFromString");
        Type listType = new TypeToken<List<Ingredient>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> list){
        Log.e(TAG, "fromIngredList");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
