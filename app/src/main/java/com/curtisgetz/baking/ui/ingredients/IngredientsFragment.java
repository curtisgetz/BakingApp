package com.curtisgetz.baking.ui.ingredients;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.database.AppDataBase;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.ui.recipe.RecipeViewModel;
import com.curtisgetz.baking.ui.recipe.RecipeViewModelFactory;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    private final static String TAG = IngredientsFragment.class.getSimpleName();

    @BindView(R.id.ingredient_recyclerview) RecyclerView mIngredientsRecycler;


    private IngredientsAdapter mAdapter;
    private AppDataBase mDb;
    private RecipeViewModel mViewModel;

    public IngredientsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);


        if(savedInstanceState == null) {
            mDb = AppDataBase.getsInstance(getActivity().getApplicationContext());
           //set up adapter and recycler view
            mAdapter = new IngredientsAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getActivity(), LinearLayoutManager.VERTICAL, false);
            mIngredientsRecycler.setLayoutManager(layoutManager);
            mIngredientsRecycler.setHasFixedSize(true);
            mIngredientsRecycler.setAdapter(mAdapter);

            Bundle bundle = getArguments();
            final int recipeId = bundle.getInt(getString(R.string.recipe_id_extra), 0);

            RecipeViewModelFactory factory = new RecipeViewModelFactory(mDb, recipeId);
            mViewModel = ViewModelProviders.of(this, factory)
                    .get(RecipeViewModel.class);
           mViewModel.getmRecipe().observe(this, new Observer<Recipe>() {
               @Override
               public void onChanged(@Nullable Recipe recipe) {
                   populateUI();
               }
           });
        }

        return view;
    }

    private void populateUI(){
        if(mViewModel.getmRecipe() == null){
            errorLoadingIngredients();
            return;
        }
        getActivity().setTitle(mViewModel.getmRecipe().getValue().getName());
        mAdapter.setIngredients(mViewModel.getmRecipe().getValue().getIngredients());
    }

    private void errorLoadingIngredients(){
        Toast.makeText(getActivity(), R.string.error_loading_ingredients, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}




