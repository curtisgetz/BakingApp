package com.curtisgetz.baking.ui.ingredients;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.curtisgetz.baking.R;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);


        Intent intent = getIntent();
        if(intent != null){
            int recipeId = intent.getIntExtra(getString(R.string.recipe_id_extra), 0);
            populateUI(recipeId);
        }





    }


    public void populateUI(int recipeId){

        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.recipe_id_extra),recipeId);

        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingrediens_fragment_container, fragment).commit();

    }





}
