package com.curtisgetz.baking.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.WidgetRecipe;

@Database(entities = {Recipe.class, WidgetRecipe.class}, version = 1, exportSchema = false)
@TypeConverters(RecipeConverters.class)
public abstract class AppDataBase extends RoomDatabase {

    private static final String TAG = AppDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipedb";
    private static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context){
        if(sInstance == null) {
            synchronized (LOCK){
                Log.d(TAG, "Creating New Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, AppDataBase.DATABASE_NAME).build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }


    public abstract RecipeDao recipeDao();

}
