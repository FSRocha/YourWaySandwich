package com.fastfood.yourwaysandwich.view;

import com.fastfood.yourwaysandwich.model.Ingredient;

interface ExtraIngredientListener {

    void addExtra(Ingredient ingredient);

    void removeExtra(Ingredient ingredient);
}
