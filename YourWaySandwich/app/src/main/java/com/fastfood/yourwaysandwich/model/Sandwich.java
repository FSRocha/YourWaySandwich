package com.fastfood.yourwaysandwich.model;

import com.fastfood.yourwaysandwich.ApplicationGlobal;

import java.util.ArrayList;
import java.util.List;

public class Sandwich {
    public int id;
    public String name;
    public List<Integer> ingredients;
    public String image;
    private List<Integer> extras = new ArrayList<>();
    private float mPrice = 0;
    private String mIngredientsText = null;

    public void cloneSandwich(Sandwich sandwich) {
        id = sandwich.getId();
        name = sandwich.getName();
        ingredients = new ArrayList<>(sandwich.getIngredients());
        image = sandwich.getImageUrl();
    }

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

    public void setExtras(List<Integer> extras) {
        this.extras = extras;
        refresh();
    }

    public void addExtraIngredient(Ingredient ingredient) {
        extras.add(ingredient.getId());
        refresh();
    }

    public void removeExtraIngredient(Ingredient ingredient) {
        extras.remove(ingredient.getId());
        refresh();
    }

    public boolean hasExtras() {
        return !extras.isEmpty();
    }

    public float getPrice() {
        if (mPrice == 0) {
            refresh();
        }
        return mPrice;
    }

    public String getIngredientsText() {
        if (mIngredientsText == null) {
            refresh();
        }
        return mIngredientsText;
    }

    private void refresh() {
        PromotionsCalculator calculator = new PromotionsCalculator(ApplicationGlobal.getInstance());
        mIngredientsText = calculator.buildIngredientsList(ingredients, extras);
        mPrice = calculator.calculatePrice(ingredients, extras);
    }

    public int[] getExtrasAsArray() {
        int[] extrasArray = new int[extras.size()];
        for (int i = 0; i < extras.size(); i++) {
            extrasArray[i] = extras.get(i);
        }
        return extrasArray;
    }
}