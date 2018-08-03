package com.curtisgetz.baking.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

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

    //@Query("SELECT ingredients FROM recipes WHERE id = :recipeId")
   // LiveData<List<Ingredient>> loadIngredientsByRecipeID(int recipeId);


}
