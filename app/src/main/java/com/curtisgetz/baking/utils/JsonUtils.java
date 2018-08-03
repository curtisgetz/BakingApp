package com.curtisgetz.baking.utils;

import com.curtisgetz.baking.model.Ingredient;
import com.curtisgetz.baking.model.Recipe;
import com.curtisgetz.baking.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {


    private final static String RECIPE_ID_KEY = "id";
    private final static String RECIPE_NAME_KEY = "name";
    private final static String RECIPE_SERVINGS_KEY = "servings";
    private final static String RECIPE_IMAGE_KEY = "image";
    private final static String RECIPE_INGREDIENTS_KEY = "ingredients";
    private final static String RECIPE_STEPS_KEY = "steps";

    private final static String INGREDIENTS_QUANTITY_KEY = "quantity";
    private final static String INGREDIENTS_MEASUREMENT_KEY = "measure";
    private final static String INGREDIENTS_NAME_KEY = "ingredient";

    private final static String STEPS_ID_KEY = RECIPE_ID_KEY;
    private final static String STEPS_SHORT_DESC_KEY = "shortDescription";
    private final static String STEPS_DESCRIPTION_KEY = "description";
    private final static String STEPS_VIDEO_URL_KEY = "videoURL";
    private final static String STEPS_THUMBNAIL_URL_KEY = "thumbnailURL";

    private final static String FALLBACK_STRING = "Unknown";
    private final static int FALLBACK_INT = 0;
    private final static double FALLBACK_DOUBLE = 0.0;



    public static List<Recipe> getRecipes(String json){
        List<Ingredient> ingredientList;
        List<Step> stepList;
        List<Recipe> recipeList = new ArrayList<>();

        String recipeName, recipeImageUrl;
        int recipeId, recipeServings;

        try {
            //get root array
            JSONArray queryArray = new JSONArray(json);
            //iterate through objects in array
            for(int i = 0; i < queryArray.length(); i++){
                JSONObject recipeObject = queryArray.getJSONObject(i);
                //get values for Recipe object
                recipeId = recipeObject.optInt(RECIPE_ID_KEY, FALLBACK_INT);
                recipeServings = recipeObject.optInt(RECIPE_SERVINGS_KEY, FALLBACK_INT);
                recipeName = recipeObject.optString(RECIPE_NAME_KEY, FALLBACK_STRING);
                recipeImageUrl = recipeObject.optString(RECIPE_IMAGE_KEY, FALLBACK_STRING);

                //get JSONArrays of ingredients and steps
                JSONArray ingredientsArray = recipeObject.getJSONArray(RECIPE_INGREDIENTS_KEY);
                JSONArray stepsArray = recipeObject.getJSONArray(RECIPE_STEPS_KEY);
                //get List of Ingredient and
                ingredientList = getIngredients(ingredientsArray);
                stepList = getSteps(stepsArray);

                //add new Recipe to List
                recipeList.add(new Recipe(recipeId, recipeName, ingredientList, stepList, recipeServings, recipeImageUrl));

            }

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return recipeList;
    }

    private static List<Ingredient> getIngredients(JSONArray ingredientsArray){
        List<Ingredient> ingredients = new ArrayList<>();

        try {
            double quantity;
            String measure, name;
            for (int i = 0; i < ingredientsArray.length(); i++) {
                //get each ingredient JSONobject
                JSONObject ingredientObject = ingredientsArray.getJSONObject(i);
                //get values for Ingredient Object
                quantity = ingredientObject.optDouble(INGREDIENTS_QUANTITY_KEY, FALLBACK_DOUBLE);
                measure = ingredientObject.optString(INGREDIENTS_MEASUREMENT_KEY, FALLBACK_STRING);
                //capitalize first letter of all ingredients
                name = getCapitalizedWords(ingredientObject.optString(INGREDIENTS_NAME_KEY, FALLBACK_STRING));

                ingredients.add(new Ingredient(quantity, measure, name));
            }
        }catch (JSONException e){
            e.printStackTrace();
            //return empty list if error
            return new ArrayList<>();
        }
        //return the list of ingredients
        return ingredients;
    }

    private static List<Step> getSteps(JSONArray stepsArray){
        List<Step> steps = new ArrayList<>();

        try {
            int id;
            String shortDesc, fullDesc, videoUrl, thumbnailUrl;
            for (int i = 0; i < stepsArray.length(); i++) {
                //get each steps JSONObject
                JSONObject stepObject = stepsArray.getJSONObject(i);
                //get values for Step Object
                id = stepObject.optInt(STEPS_ID_KEY, FALLBACK_INT);
                shortDesc = getCapitalizedWords(stepObject.optString(STEPS_SHORT_DESC_KEY, FALLBACK_STRING));

                fullDesc = stepObject.optString(STEPS_DESCRIPTION_KEY, FALLBACK_STRING);
                videoUrl = stepObject.optString(STEPS_VIDEO_URL_KEY, FALLBACK_STRING);
                thumbnailUrl = stepObject.optString(STEPS_THUMBNAIL_URL_KEY, FALLBACK_STRING);

                steps.add(new Step(id, shortDesc, fullDesc, videoUrl, thumbnailUrl));
            }
        }catch (JSONException e){
            e.printStackTrace();
            //return empty list if error
            return new ArrayList<>();
        }
        //return the list of steps
        return steps;
    }

    private static String getCapitalizedWords(String inString){
        if(inString.isEmpty()) return inString;

        StringBuilder  builder = new StringBuilder();
        //Split string at spaces
        String[] nameSplit = inString.split(" ");
        //capitalize each word.
        for(String split: nameSplit){
            split = split.substring(0, 1).toUpperCase() + split.substring(1).toLowerCase();
            //append to builder, append space
            builder.append(split).append(" ");
        }

        return builder.toString();
    }


}
