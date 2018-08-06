package com.curtisgetz.baking.ui.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.database.DatabaseUtils;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;

import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.ui.ingredients.IngredientsActivity;
import com.curtisgetz.baking.ui.ingredients.IngredientsFragment;
import com.curtisgetz.baking.ui.step.StepDetailActivity;
import com.curtisgetz.baking.ui.step.StepDetailFragment;
import com.curtisgetz.baking.utils.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeActivity extends AppCompatActivity implements StepsAdapter.StepClickListener {
    private final static String TAG = RecipeActivity.class.getSimpleName();
    public final static String EXTRA_RECIPE_ID = "recipe_id_extra";

    private AppDataBase mDb;
    private boolean twoPane;
    private RecipeViewModel mViewModel;

    @BindView(R.id.step_desc_recycler) RecyclerView mStepsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        //get instance of database
        mDb = AppDataBase.getsInstance(getApplicationContext());
        //check if twoPane
        twoPane = (findViewById(R.id.recipe_activity_sw600_land)!= null);


        Intent intent  = getIntent();
        if(intent != null){
            int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 1);
            RecipeViewModelFactory factory = new RecipeViewModelFactory(mDb, recipeId);

            mViewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

            mViewModel.getmRecipe().observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable Recipe recipe) {
                    populateUI();
                }
            });

        }else {
            Toast.makeText(this, R.string.recipe_load_error, Toast.LENGTH_SHORT).show();
            finish();
        }

    }



    private void populateUI(){
        Recipe recipe = mViewModel.getmRecipe().getValue();
        if(recipe == null) return;
        setTitle(recipe.getName());

        StepsAdapter adapter = new StepsAdapter(this);
        adapter.setSteps(recipe.getSteps());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mStepsRecycler.setLayoutManager(layoutManager);
        mStepsRecycler.setAdapter(adapter);

    }

    private void viewModelErrorToast(){
        Toast.makeText(this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ingredients_label)
    public void onIngredientsLabelClick(View view){
        if(mViewModel.getmRecipe().getValue() == null) {
            viewModelErrorToast();
            return;
        }
        if(twoPane){
            //Show ingredients in detail pane if twoPane
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.recipe_id_extra), mViewModel.getmRecipe().getValue().getId());
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.recipe_detail_container, ingredientsFragment).commit();
        }else {
            //if not twoPane, start Ingredients Activity, passing recipeID
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(getString(R.string.recipe_id_extra), mViewModel.getmRecipe().getValue().getId());
            startActivity(intent);
        }
    }


    @Override
    public void onStepClickListener(int clickedPos) {
        if(mViewModel.getmRecipe().getValue() == null) {
            viewModelErrorToast();
            return;
        }
        if(twoPane){
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.recipe_id_extra), mViewModel.getmRecipe().getValue().getId());
            bundle.putInt(getString(R.string.step_number_extra), clickedPos);
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
             getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, stepDetailFragment).commit();
        }else {

            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(getString(R.string.recipe_id_extra), mViewModel.getmRecipe().getValue().getId());
            intent.putExtra(getString(R.string.clicked_step_key), clickedPos);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
    }
}






