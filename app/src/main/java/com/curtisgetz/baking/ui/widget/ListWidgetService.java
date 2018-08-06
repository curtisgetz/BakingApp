package com.curtisgetz.baking.ui.widget;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Ingredient;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.WidgetRecipe;

public class ListWidgetService extends RemoteViewsService {
private final static String TAG = ListWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }



    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private final String TAG = ListRemoteViewsFactory.class.getSimpleName();


        Context mContext;
        AppDataBase mDb;
        Recipe mRecipe;
        int WIDGET_RECIPE_KEY = 0;
        int DEFAULT_RECIPE_ID = 1;

        public ListRemoteViewsFactory(Context applicationContext) {
            this.mContext = applicationContext;
            mDb = AppDataBase.getsInstance(applicationContext);


        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            //get current recipeid saved for widget.  probably not the best way to do this
            WidgetRecipe widgetRecipe = mDb.recipeDao().widgetGetCurrentRecipeId(WIDGET_RECIPE_KEY);

            int recipeId = DEFAULT_RECIPE_ID;
            if(widgetRecipe != null) {
                recipeId = widgetRecipe.getRecipeId();
            }
            mRecipe = mDb.recipeDao().widgetLoadRecipeById(recipeId);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(mRecipe == null) return 0;
            return mRecipe.getIngredients().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if(mRecipe == null) {
                return null;
            }

            Ingredient ingredient = mRecipe.getIngredients().get(position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
            //Get ingredient details and populate TextViews
            String ingredientUnit = String.valueOf(ingredient.getQuantity());
            String ingredientMeasure = String.valueOf(ingredient.getMeasure());
            views.setTextViewText(R.id.widget_ingredient_unit, ingredientUnit);
            views.setTextViewText(R.id.widget_ingredient_measure, ingredientMeasure);
            views.setTextViewText(R.id.widget_ingredient_name, ingredient.getIngredientName());


            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }


}
