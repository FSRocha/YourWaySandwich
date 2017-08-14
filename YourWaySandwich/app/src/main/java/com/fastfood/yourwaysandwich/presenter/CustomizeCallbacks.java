package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.List;

public interface CustomizeCallbacks {

    void onShowIngredientsList(List<Ingredient> ingredientList);

    void onError(String errorTitle, String errorMsg);

    void onExtrasAdded();
}
