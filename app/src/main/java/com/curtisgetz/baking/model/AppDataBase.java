package com.curtisgetz.baking.model;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
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
