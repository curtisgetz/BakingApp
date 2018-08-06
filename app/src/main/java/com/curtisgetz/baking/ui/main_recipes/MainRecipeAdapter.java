package com.curtisgetz.baking.ui.main_recipes;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecipeAdapter extends RecyclerView.Adapter {
    private final static String TAG = MainRecipeAdapter.class.getSimpleName();


    private List<Recipe> mRecipes;
    private RecipeClickListener mClickListener;


    MainRecipeAdapter(RecipeClickListener clickListener){
        this.mClickListener = clickListener;
    }

    public interface RecipeClickListener{
        void onRecipeClick(int clickedPos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecipeViewHolder) holder).recipeNameTv.setText(mRecipes.get(position).getName());
        ((RecipeViewHolder) holder).recipeServesTv.setText(String.valueOf(mRecipes.get(position).getServings()));

    }

    @Override
    public int getItemCount() {
        if(mRecipes == null){
            return 0;
        }
        return mRecipes.size();
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_recipe_name) TextView recipeNameTv;
        @BindView(R.id.tv_recipe_servings) TextView recipeServesTv;
        @BindView(R.id.main_recipe_cardview) CardView mCardView;

        RecipeViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Call onRecipeClick(), pass in the clicked position.
            mClickListener.onRecipeClick(getAdapterPosition());
        }
    }


    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(List<Recipe> recipeArrayList){
        mRecipes = new ArrayList<>();
        mRecipes.addAll(recipeArrayList);
        notifyDataSetChanged();
    }

}
