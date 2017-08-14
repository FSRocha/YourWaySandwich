package com.fastfood.yourwaysandwich;

import android.app.Application;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;
import com.fastfood.yourwaysandwich.model.service.SandwichDetailsProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class used to keep global state variables
 */
public class ApplicationGlobal extends Application implements AvailableIngredientsProvider,
        SelectedSandwichProvider, SandwichDetailsProvider {

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

    private Map<Integer, Sandwich> mSandwichIngredients = new HashMap<>();

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

    @Override
    public Sandwich getSandwichDetails(int sandwichId) {
        return mSandwichIngredients.get(sandwichId);
    }

    @Override
    public void addSandwichDetails(List<Sandwich> sandwichList) {
        for (Sandwich sandwich : sandwichList) {
            mSandwichIngredients.put(sandwich.getId(), sandwich);
        }
    }

}