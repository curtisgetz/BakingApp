package com.curtisgetz.baking.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.WidgetRecipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT COUNT(*) FROM recipes")
    int getNumberOfRecipes();

    @Query("SELECT COUNT(*) FROM widget_recipe")
    int getNumberOfWidgetRecipes();

    @Query("SELECT * FROM recipes ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> loadRecipeById(int id);

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe widgetLoadRecipeById(int id);

    @Query("SELECT * FROM widget_recipe WHERE widget_key = :id")
    WidgetRecipe widgetGetCurrentRecipeId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWidgetRecipe(WidgetRecipe widgetRecipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWidgetRecipe(WidgetRecipe widgetRecipe);


}
