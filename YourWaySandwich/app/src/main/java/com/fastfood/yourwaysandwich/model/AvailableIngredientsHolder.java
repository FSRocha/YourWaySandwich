package com.fastfood.yourwaysandwich.model;

import java.util.List;

public interface AvailableIngredientsHolder {

    List<Ingredient> getAvailableIngredients();

    void setAvailableIngredients(List<Ingredient> ingredients);
}
