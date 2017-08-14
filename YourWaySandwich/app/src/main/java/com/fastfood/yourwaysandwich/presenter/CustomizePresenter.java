package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Presenter layer for customize view
 */
public class CustomizePresenter implements CustomizeOperations {

    private String LOG_TAG = "CustomizePresenter";

    private CustomizeCallbacks mCallbacks;

    private SelectedSandwichProvider mSelectedSandwichProvider;

    private Map<Ingredient, Integer> mIngredientsMap;

    @Override
    public void createCustomize(AvailableIngredientsProvider availableIngredientsProvider,
                                SelectedSandwichProvider selectedSandwichProvider,
                                CustomizeCallbacks callbacks) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mSelectedSandwichProvider = selectedSandwichProvider;
        mCallbacks = callbacks;
        List<Ingredient> ingredients = availableIngredientsProvider.getAvailableIngredients();
        mIngredientsMap = initializeIngredientsMap(ingredients);
        mCallbacks.onShowIngredientsList(mIngredientsMap);
    }

    private Map<Ingredient, Integer> initializeIngredientsMap(List<Ingredient> ingredients) {
        Map<Ingredient, Integer> map = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            map.put(ingredient, 0);
        }
        return map;
    }

    @Override
    public void addExtraIngredient(Ingredient extra) {
        int value = mIngredientsMap.get(extra);
        mIngredientsMap.put(extra, value + 1);
        mCallbacks.onShowIngredientsList(mIngredientsMap);
    }

    @Override
    public void removeExtraIngredient(Ingredient extra) {
        int value = mIngredientsMap.get(extra);
        if (value > 0) {
            mIngredientsMap.put(extra, value - 1);
            mCallbacks.onShowIngredientsList(mIngredientsMap);
        }
    }

    @Override
    public void addExtras() {
        Sandwich sandwich = mSelectedSandwichProvider.getSelectedSandwich();
        for (Map.Entry<Ingredient, Integer> entry : mIngredientsMap.entrySet()) {
            for (int i = entry.getValue(); i > 0; i--) {
                sandwich.addExtraIngredient(entry.getKey());
            }
        }
        mSelectedSandwichProvider.setSelectedSandwich(sandwich);
        mCallbacks.onExtrasAdded();
    }
}
