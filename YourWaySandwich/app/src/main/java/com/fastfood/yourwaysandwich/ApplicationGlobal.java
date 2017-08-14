package com.fastfood.yourwaysandwich;

import android.app.Application;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsHolder;
import com.fastfood.yourwaysandwich.model.Ingredient;

import java.util.List;


/**
 * Class used to keep global state variables
 */
public class ApplicationGlobal extends Application implements AvailableIngredientsHolder {

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

    @Override
    public List<Ingredient> getAvailableIngredients() {
        return mAvailableIngredients;
    }

    @Override
    public void setAvailableIngredients(List<Ingredient> ingredients) {
        this.mAvailableIngredients = ingredients;
    }
}