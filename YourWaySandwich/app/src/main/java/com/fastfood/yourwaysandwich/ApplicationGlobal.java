package com.fastfood.yourwaysandwich;

import android.app.Application;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

import java.util.List;


/**
 * Class used to keep global state variables
 */
public class ApplicationGlobal extends Application implements AvailableIngredientsProvider,
        SelectedSandwichProvider {

    private static ApplicationGlobal singleton;

    public static ApplicationGlobal getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = ApplicationGlobal.this;
    }

    private List<Ingredient> mAvailableIngredients = null;

    private Sandwich mSelectedSandwich = null;

    @Override
    public List<Ingredient> getAvailableIngredients() {
        return mAvailableIngredients;
    }

    @Override
    public void setAvailableIngredients(List<Ingredient> ingredients) {
        this.mAvailableIngredients = ingredients;
    }

    @Override
    public Sandwich getSelectedSandwich() {
        return mSelectedSandwich;
    }

    @Override
    public void setSelectedSandwich(Sandwich sandwich) {
        mSelectedSandwich = sandwich;
    }
}