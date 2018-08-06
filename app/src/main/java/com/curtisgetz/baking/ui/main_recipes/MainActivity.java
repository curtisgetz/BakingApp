package com.curtisgetz.baking.ui.main_recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.WidgetRecipe;
import com.curtisgetz.baking.utils.AppExecutors;
import com.curtisgetz.baking.ui.recipe.RecipeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainRecipeAdapter.RecipeClickListener {


    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recipe_recyclerview)
    RecyclerView mRecyclerView;


    private int spanCount = 1;
    private MainRecipeAdapter mAdapter;

    private Recipe mRecipe;
    private AppDataBase mDb;


    //App downloads recipes once per application start via BakingApp class. Consider other scheduling later
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set number of columns for grid view based on screen and orientation
        if(findViewById(R.id.main_600sw) != null) {
            spanCount = 2;
        }else if(findViewById(R.id.main_600sw_land) != null){
            spanCount = 4;
        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainRecipeAdapter(this );
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }


    @Override
    public void onRecipeClick(int clickedPos) {
        //send selected recipeId to RecipeActivity. RecipeActivity will load recipe from DB by id
        // unsure if it's better to send a parcelable with the intent, or to just send the ID
        // and have the next Activity pull from DB by id.
        int recipeId = mAdapter.getRecipes().get(clickedPos).getId();
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID,recipeId);
        startActivity(intent);

    }

    public void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getmRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(TAG, "Updating list of recipes from LiveData in ViewModel");
                mAdapter.setRecipes(recipes);
            }
        });

    }

    private void checkWidgetDB(){
        final WidgetRecipe[] widgetRecipe = {null};
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDataBase db = AppDataBase.getsInstance(getApplicationContext());
                widgetRecipe[0] = db.recipeDao().widgetGetCurrentRecipeId(0);
            }
        });
        if(widgetRecipe[0] == null) Toast.makeText(
                this, "WIDGET RECIPE IS NULL", Toast.LENGTH_SHORT).show();
    }


}