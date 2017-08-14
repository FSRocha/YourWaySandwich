package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Ingredient;

import java.util.Map;

public interface CustomizeCallbacks {

    void onShowIngredientsList(Map<Ingredient, Integer> ingredientList);

    void onError(String errorTitle, String errorMsg);

    void onExtrasAdded();

}
