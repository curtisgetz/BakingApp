package com.curtisgetz.baking.ui.main_recipes;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.ui.recipe.RecipeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    public static final String INGREDIENTS_LABEL = "Recipe Ingredients";
    public static final String STEP_ONE_LABEL = "Recipe Introduction";
    public static final int RECIPE_ID_EXTRA = 2;
    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), RecipeActivity.class);

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class, false, false);


    @Before
    public void setup(){
      //start RecipeActivity with an intExtra for recipe ID
        MY_ACTIVITY_INTENT.putExtra("recipe_id_extra", RECIPE_ID_EXTRA);
        mActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void clickIngredients_OpensIngredients(){
        //Get and click on 'Recipe Ingredients' to open IngredientsActivity
        onView((withId(R.id.ingredients_label))).perform(click());

        //verify Ingredients opens and check title label
        onView(withId(R.id.ingredients_title_label)).check(matches(withText(INGREDIENTS_LABEL)));

    }

    @Test
    public void clickStep_OpensStepDetail(){
        //click 2nd step
        onView(withId(R.id.step_desc_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //check description textview in step detail activity
        onView(withId(R.id.step_detail_description)).check(matches(withText(STEP_ONE_LABEL)));

    }
}
