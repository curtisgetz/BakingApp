package com.curtisgetz.baking.ui.ingredients;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.curtisgetz.baking.model.AppDataBase;
import com.curtisgetz.baking.model.Ingredient;
import com.curtisgetz.baking.model.Recipe;

import java.util.List;

public class IngredientsViewModel extends ViewModel {

    private final static String TAG = IngredientsViewModel.class.getSimpleName();

    private LiveData<List<Ingredient>> mIngredients;
    private LiveData<String> mRecipeName;

    public IngredientsViewModel(AppDataBase dataBase, int recipeId) {
        Log.d(TAG, "Loading ingredients by recipeId from database");
        //mIngredients = dataBase.recipeDao().loadIngredientsByRecipeID(recipeId);
        mIngredients = null;
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return mIngredients;
    }
}
