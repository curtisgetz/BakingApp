package com.curtisgetz.baking.ui.main_recipes;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.curtisgetz.baking.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    public static final String INGREDIENTS_LABEL = "Recipe Ingredients";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickedGridItem_OpensRecipeActivity(){

        //click on 2nd item in recyclerview
        onView(withId(R.id.recipe_recyclerview))
            .perform(RecyclerViewActions
            .actionOnItemAtPosition(1, click()));

        //check that RecipeActivity opens by looking for ingredients label.
        onView(withId(R.id.ingredients_label)).check(matches(withText(INGREDIENTS_LABEL)));
    }


}
