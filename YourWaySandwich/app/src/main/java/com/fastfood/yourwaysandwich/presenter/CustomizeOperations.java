package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

import java.util.Map;

public interface CustomizeOperations {

    void createCustomize(AvailableIngredientsProvider availableIngredientsProvider,
                         SelectedSandwichProvider selectedSandwichProvider,
                         CustomizeCallbacks callbacks, Context ctx) throws NullPointerException;

    void addExtras(Map<Integer, Ingredient> extras);
}
