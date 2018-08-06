package com.curtisgetz.baking.ui.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.WidgetRecipe;


public class RecipeLoadService extends IntentService {
    private final static String TAG = RecipeWidgetProvider.class.getSimpleName();


    public static final String ACTION_UPDATE_WIDGETS = "com.curtisgetz.baking.ui.widget.action.update_widgets";
    public static final String ACTION_NEXT_RECIPE = "com.curtisgetz.baking.ui.widget.action.next_recipe";
    public static final String ACTION_PREV_RECIPE = "com.curtisgetz.baking.ui.widget.action.prev_recipe";

    public static final String EXTRA_RECIPE_ID = "com.curtisgetz.baking.ui.widget.extra.RECIPE_ID";
    public static final int DEFAULT_RECIPE_ID = 1;
    public static final int DEFAULT_WIDGET_RECIPE_KEY = 0;



    public RecipeLoadService() {
        super("RecipeLoadService");
        Log.e(TAG, "RecipeLoadService Constructor");

    }


    @Override
    public void onCreate() {

        super.onCreate();


        //from https://stackoverflow.com/questions/44425584/context-startforegroundservice-did-not-then-call-service-startforeground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "recipe_load_service";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Baking AppWidget Service. Recipe Load Service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

    }


    public static void startActionNextRecipe(Context context, int recipeId){
        Intent intent = new Intent(context, RecipeLoadService.class);
        intent.setAction(ACTION_NEXT_RECIPE);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        }else {
            context.startService(intent);
        }
    }

    public static void startActionPrevRecipe(Context context, int recipeId){
        Intent intent = new Intent(context, RecipeLoadService.class);
        intent.setAction(ACTION_PREV_RECIPE);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        }else {
            context.startService(intent);
        }
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, RecipeLoadService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        }else {
            context.startService(intent);
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if(ACTION_UPDATE_WIDGETS.equals(action)) {
                handleActionUpdateWidgets();
            }else if(ACTION_NEXT_RECIPE.equals(action)){
                final int currentRecipeId = intent.getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
                handleActionNextRecipe(currentRecipeId);
            }else if(ACTION_PREV_RECIPE.equals(action)){
                final int currentRecipeId = intent.getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);
                handleActionPrevRecipe(currentRecipeId);
            }
        }

    }


    private void handleActionPrevRecipe(int currentRecipeId) {
        AppDataBase dataBase = AppDataBase.getsInstance(getApplicationContext());
        //check if previous recipe will be valid or need to loop around.
        int numOfRecipes = dataBase.recipeDao().getNumberOfRecipes();
        if(currentRecipeId <= 1){
            currentRecipeId = numOfRecipes;
        }else {
            currentRecipeId--;
        }

        Recipe recipe = dataBase.recipeDao().widgetLoadRecipeById(currentRecipeId);
        String recipeName = recipe.getName();

        //update database with current recipe to use in widget.
        WidgetRecipe widgetRecipe = new WidgetRecipe(currentRecipeId);
        widgetRecipe.setWidget_key(DEFAULT_WIDGET_RECIPE_KEY);
        dataBase.recipeDao().insertWidgetRecipe(widgetRecipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //trigger data update to handle listview widgets and force refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        //update all widgets
        RecipeWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, recipeName, currentRecipeId);
    }

    private void handleActionNextRecipe(int currentRecipeId) {
        AppDataBase dataBase = AppDataBase.getsInstance(getApplicationContext());
        //check if next recipe will be valid or need to start at the beginning.
        int numOfRecipes = dataBase.recipeDao().getNumberOfRecipes();
        if(currentRecipeId >= numOfRecipes){
            currentRecipeId = DEFAULT_RECIPE_ID;
        }else {
            currentRecipeId++;
        }

        Recipe recipe = dataBase.recipeDao().widgetLoadRecipeById(currentRecipeId);
        String recipeName = recipe.getName();

        //update database with current recipe to use in widget.
        WidgetRecipe widgetRecipe = new WidgetRecipe(currentRecipeId);
        widgetRecipe.setWidget_key(DEFAULT_WIDGET_RECIPE_KEY);
        dataBase.recipeDao().insertWidgetRecipe(widgetRecipe);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //trigger data update to handle listview widgets and force refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        //update all widgets
        RecipeWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, recipeName, currentRecipeId);

    }

    private void handleActionUpdateWidgets(){
        AppDataBase database = AppDataBase.getsInstance(getApplicationContext());
        WidgetRecipe widgetRecipe = database.recipeDao().widgetGetCurrentRecipeId(DEFAULT_WIDGET_RECIPE_KEY);
        int recipeId = DEFAULT_RECIPE_ID;
        if(widgetRecipe != null){
            recipeId = widgetRecipe.getRecipeId();
        }
        Recipe recipe = database.recipeDao().widgetLoadRecipeById(recipeId);
        String recipeName = recipe.getName();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //trigger data update to handle listview widgets and force refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        //update all widgets
        RecipeWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, recipeName, recipeId);
    }



}