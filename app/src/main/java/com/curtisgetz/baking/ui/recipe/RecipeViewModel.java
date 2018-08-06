package com.curtisgetz.baking.ui.recipe;



import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;

public class RecipeViewModel extends ViewModel {

    private static final String TAG = RecipeViewModel.class.getSimpleName();
    private LiveData<Recipe> mRecipe;

    public RecipeViewModel(AppDataBase dataBase, int recipeId){
        Log.e(TAG, "Loading Recipe from database by ID");
        mRecipe = dataBase.recipeDao().loadRecipeById(recipeId);
    }

    public void setmRecipe(LiveData<Recipe> recipe) {
        this.mRecipe = recipe;
    }

    public LiveData<Recipe> getmRecipe() {
        Log.e(TAG, "Getting Recipe From View model");
        return mRecipe;
    }


}
