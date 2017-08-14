package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

public interface CustomizeOperations {

    void createCustomize(AvailableIngredientsProvider availableIngredientsProvider,
                         SelectedSandwichProvider selectedSandwichProvider,
                         CustomizeCallbacks callbacks) throws NullPointerException;

    void addExtras();

    void addExtraIngredient(Ingredient extra);

    void removeExtraIngredient(Ingredient extra);
}
