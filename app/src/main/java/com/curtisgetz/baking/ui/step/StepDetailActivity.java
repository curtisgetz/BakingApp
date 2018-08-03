package com.curtisgetz.baking.ui.step;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.Step;
import com.curtisgetz.baking.ui.recipe.RecipeViewModel;
import com.curtisgetz.baking.ui.recipe.RecipeViewModelFactory;
import com.curtisgetz.baking.utils.AppExecutors;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    private final static String TAG = StepDetailActivity.class.getSimpleName();
    private final static int DEFAULT_RECIPE_ID = 0;
    private final static int DEFAULT_STEP_NUMBER = 0;


   // private List<Step> mSteps;
    private int mCurrentStepNum;
    private int mRecipeId;
   // private Recipe mRecipe;
    private boolean playerOnly;
    private AppDataBase mDb;
    //private int mRecipeId;
    private RecipeViewModel mViewModel;


    @Nullable
    @BindView(R.id.button_layout) LinearLayout mButtonLayout;
    @Nullable
    @BindView(R.id.previous_button) Button mPrevButton;
    @Nullable
    @BindView(R.id.next_button) Button mNextButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        mDb = AppDataBase.getsInstance(getApplicationContext());

        if(findViewById(R.id.button_layout) != null){
            playerOnly = false;
        }else {
            //make video full screen if phone is in landscape
            playerOnly = true;
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) actionBar.hide();
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }


        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                mRecipeId = intent.getIntExtra(getString(R.string.recipe_id_extra), DEFAULT_RECIPE_ID);
                mCurrentStepNum = intent.getIntExtra(getString(R.string.clicked_step_key), DEFAULT_STEP_NUMBER);

                getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_fragment,
                        createStepFragment()).commit();
            }
        }else {
            mRecipeId = savedInstanceState.getInt(getString(R.string.recipe_id_extra), DEFAULT_RECIPE_ID);
            mCurrentStepNum = savedInstanceState.getInt(getString(R.string.saved_step_num), DEFAULT_STEP_NUMBER);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.recipe_id_extra), mRecipeId);
        outState.putInt(getString(R.string.saved_step_num), mCurrentStepNum);
        super.onSaveInstanceState(outState);
    }

    private void setupUI(){

    }

    private StepDetailFragment createStepFragment(){
        //Send id and step number to fragment. Have fragment load recipe from DB by ID
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.recipe_id_extra), mRecipeId);
        bundle.putInt(getString(R.string.step_number_extra), mCurrentStepNum);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
