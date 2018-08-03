package com.curtisgetz.baking.ui.main_recipes;

        import android.app.Application;
        import android.arch.lifecycle.AndroidViewModel;
        import android.arch.lifecycle.LiveData;
        import android.util.Log;

        import com.curtisgetz.baking.model.AppDataBase;
        import com.curtisgetz.baking.model.Recipe;

        import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Recipe>> mRecipes;

    public MainViewModel(Application application) {
        super(application);
        AppDataBase dataBase = AppDataBase.getsInstance(this.getApplication());
        Log.d(TAG, "Retrieving recipes from database");
        mRecipes = dataBase.recipeDao().loadAllRecipes();
    }

    public LiveData<List<Recipe>> getmRecipes() {
        return mRecipes;
    }
}
