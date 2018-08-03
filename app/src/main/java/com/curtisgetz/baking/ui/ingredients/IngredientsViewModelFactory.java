package com.curtisgetz.baking.ui.ingredients;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.curtisgetz.baking.model.AppDataBase;

public class IngredientsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDataBase mDb;
    private final int mRecipeId;

    public IngredientsViewModelFactory(AppDataBase dataBase, int recipeId){
        this.mDb = dataBase;
        this.mRecipeId = recipeId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientsViewModel(mDb, mRecipeId);
    }
}
