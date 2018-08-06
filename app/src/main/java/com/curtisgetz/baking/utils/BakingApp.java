package com.curtisgetz.baking.utils;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.curtisgetz.baking.model.WidgetRecipe;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;

import java.security.PublicKey;
import java.util.List;

public class BakingApp extends Application {

    private final static String TAG = BakingApp.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private final static String RECIPES_ADDRESS = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private AppDataBase mDb;

    public final static int DEFAULT_RECIPE_ID = 1;
    public final static int DEFAULT_STEP_NUM = 0;

    // Download recipes and update DB when app starts.  Consider scheduling later

    public BakingApp(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDb = AppDataBase.getsInstance(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, RECIPES_ADDRESS,
                onRecipesLoaded, onRecipesError);
        mRequestQueue.add(request);
        setUpWidgetDb();


    }


    private void setUpWidgetDb(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int widgetRows = mDb.recipeDao().getNumberOfWidgetRecipes();
                if(widgetRows != 1){
                    mDb.recipeDao().insertWidgetRecipe(new WidgetRecipe(1));
                }
            }
        });
    }


    //Volley Response listeners
    private final Response.Listener<String> onRecipesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Recipe> recipes = JsonUtils.getRecipes(response);
            if(recipes != null){
                for(final Recipe recipe : recipes) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.recipeDao().insertRecipe(recipe);
                        }
                    });

                }
            }

        }
    };

    private final Response.ErrorListener onRecipesError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };



}
