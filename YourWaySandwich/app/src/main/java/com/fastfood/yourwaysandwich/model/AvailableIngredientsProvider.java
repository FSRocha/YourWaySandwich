package com.fastfood.yourwaysandwich.model;

import java.util.List;

public interface AvailableIngredientsProvider {

    List<Ingredient> getAvailableIngredients();

    void setAvailableIngredients(List<Ingredient> ingredients);
}
