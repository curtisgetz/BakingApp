package com.curtisgetz.baking.ui.main_recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.utils.AppExecutors;
import com.curtisgetz.baking.ui.recipe.RecipeActivity;
import com.curtisgetz.baking.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainRecipeAdapter.RecipeClickListener {


    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String RECIPES_ADDRESS = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final static int DEFAULT_RECIPE_ID = -1;

    @BindView(R.id.recipe_recyclerview)
    RecyclerView mRecyclerView;


    private boolean mIsTwoPane;
    private int spanCount = 1;
    private MainRecipeAdapter mAdapter;
    private RequestQueue mRequestQueue;

    private List<Recipe> mRecipeList;
    private AppDataBase mDb;
    //TODO  espresso testing
    //TODO Widget
    //todo  move dimens to dimens.xml


    //App downloads recipes once per application start via BakingApp class. Consider other scheduling later
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set number of columns for grid view based on screen and orientation
        if(findViewById(R.id.main_600sw) != null) {
            Log.d(TAG, "is two pane");
            mIsTwoPane = true;
            spanCount = 2;
        }else if(findViewById(R.id.main_600sw_land) != null){
            mIsTwoPane = true;
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
        intent.putExtra(getString(R.string.recipe_id_extra),recipeId);
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

}