package com.fastfood.yourwaysandwich.model;

import java.util.ArrayList;
import java.util.List;

public class Sandwich {
    public int id;
    public String name;
    public List<Integer> ingredients;
    public String image;
    private boolean mHasExtras = false;
    private List<Integer> extras = new ArrayList<>();
    private float mPrice =  0;
    private String mIngredientsText = null;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getIngredients() {
        return ingredients;
    }

    public String getImageUrl() {
        return image;
    }

    public void addExtraIngredient(Ingredient ingredient) {
        extras.add(ingredient.getId());
        refreshIngredientsList();
        refreshPrice();
    }

    public void removeExtraIngredient(Ingredient ingredient) {
        extras.remove(ingredient.getId());
        refreshIngredientsList();
        refreshPrice();
    }

    public boolean hasExtras() {
        return extras.isEmpty();
    }

    public float getPrice() {
        if (mPrice == 0) {
            refreshPrice();
        }
        return mPrice;
    }

    public String getIngredientsText() {
        if (mIngredientsText == null) {
            refreshIngredientsList();
        }
        return mIngredientsText;
    }

    private void refreshIngredientsList() {
        // TODO Implement based on pre loaded data (requested at the app initialization)
    }

    private void refreshPrice(){
        // TODO Implement based on pre loaded data (requested at the app initialization)
    }
}