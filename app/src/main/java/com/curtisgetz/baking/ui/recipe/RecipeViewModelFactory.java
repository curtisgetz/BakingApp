package com.curtisgetz.baking.ui.recipe;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.curtisgetz.baking.model.AppDataBase;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDataBase mDb;
    private final int mRecipeId;

    public RecipeViewModelFactory(AppDataBase mDb, int mRecipeId) {
        this.mDb = mDb;
        this.mRecipeId = mRecipeId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(mDb, mRecipeId);
    }
}
