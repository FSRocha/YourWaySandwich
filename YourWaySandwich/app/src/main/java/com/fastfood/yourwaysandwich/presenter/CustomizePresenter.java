package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

import java.util.List;
import java.util.Map;

/**
 * Presenter layer for customize view
 */
public class CustomizePresenter implements CustomizeOperations {

    private String LOG_TAG = "CustomizePresenter";

    private CustomizeCallbacks mCallbacks;

    private AvailableIngredientsProvider mAvailableIngredientsProvider;
    private SelectedSandwichProvider mSelectedSandwichProvider;

    private Context mContext;

    @Override
    public void createCustomize(AvailableIngredientsProvider availableIngredientsProvider,
                                SelectedSandwichProvider selectedSandwichProvider,
                                CustomizeCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mAvailableIngredientsProvider = availableIngredientsProvider;
        mSelectedSandwichProvider = selectedSandwichProvider;
        mCallbacks = callbacks;
        mContext = ctx;
        List<Ingredient> ingredients = mAvailableIngredientsProvider.getAvailableIngredients();
        mCallbacks.onShowIngredientsList(ingredients);
    }

    @Override
    public void addExtras(Map<Integer, Ingredient> extras) {
        Sandwich sandwich = mSelectedSandwichProvider.getSelectedSandwich();
        for (Map.Entry<Integer, Ingredient> entry : extras.entrySet()) {
            for (int i = entry.getKey(); i > 0; i--) {
                sandwich.addExtraIngredient(entry.getValue());
            }
        }
    }
}
