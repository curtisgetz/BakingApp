package com.curtisgetz.baking.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.curtisgetz.baking.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private final static String TAG = RecipeWidgetProvider.class.getSimpleName();




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, int recipeId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        //set textview with recipe name
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        //intent for ListView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        //Intent for next recipe
        Intent nextRecipeIntent = new Intent(context, RecipeLoadService.class);
        nextRecipeIntent.setAction(RecipeLoadService.ACTION_NEXT_RECIPE);
        //int nextRecipeId = recipeId + 1;
        nextRecipeIntent.putExtra(RecipeLoadService.EXTRA_RECIPE_ID, recipeId);
        PendingIntent nextRecipePending = PendingIntent.getService(context, 0,
                nextRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //set click listener on next button
        views.setOnClickPendingIntent(R.id.widget_next_button, nextRecipePending);

        //Intent for prev recipe
        Intent prevRecipeIntent = new Intent(context, RecipeLoadService.class);
       // int prevRecipeId = recipeId - 1;
        prevRecipeIntent.setAction(RecipeLoadService.ACTION_PREV_RECIPE);
        prevRecipeIntent.putExtra(RecipeLoadService.EXTRA_RECIPE_ID, recipeId);
        PendingIntent prevRecipePending = PendingIntent.getService(context, 0,
                prevRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //set click listener on prev button
        views.setOnClickPendingIntent(R.id.widget_prev_button, prevRecipePending);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,
                                     int[] appWidgetIds, String recipeName, int recipeId){

        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, recipeId);
        }
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeLoadService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeLoadService.startActionUpdateWidget(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

