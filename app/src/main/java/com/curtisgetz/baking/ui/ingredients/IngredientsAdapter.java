package com.curtisgetz.baking.ui.ingredients;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter {

    private List<Ingredient> mIngredients;



    public IngredientsAdapter( ) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);


        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((IngredientViewHolder) holder).quantityTV.setText(String.valueOf(mIngredients.get(position).getQuantity()));
        ((IngredientViewHolder) holder).nameTV.setText(mIngredients.get(position).getIngredientName());
        ((IngredientViewHolder) holder).measurementTV.setText(mIngredients.get(position).getMeasure());

    }

    @Override
    public int getItemCount() {
        if(mIngredients == null) return 0;
        return mIngredients.size();
    }


    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = new ArrayList<>();
        mIngredients.addAll(ingredients);
        notifyDataSetChanged();
    }




    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name) TextView nameTV;
        @BindView(R.id.ingredient_quantity) TextView quantityTV;
        @BindView(R.id.ingredient_measurement) TextView measurementTV;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
