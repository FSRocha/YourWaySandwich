package com.fastfood.yourwaysandwich.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class PromotionsCalculator {

    // Number of beef portions to discount 1 portion
    private static int BEEF_PORTIONS_PER_DISCOUNT = 3;

    // Number of cheese portions to discount 1 portion
    private static int CHEESE_PORTIONS_PER_DISCOUNT = 3;

    private static String BEEF_PORTION_NAME = "Hamburguer de Carne";

    private static String CHEESE_PORTION_NAME = "Queijo";

    private static String LETTUCE_PORTION_NAME = "Alface";

    private static String BACON_PORTION_NAME = "Bacon";

    private AvailableIngredientsHolder mIngredientsHolder;

    private float IS_LIGHT_DISCOUNT = 0.9f;

    PromotionsCalculator(AvailableIngredientsHolder ingredientsHolder) {
        mIngredientsHolder = ingredientsHolder;
    }

    float calculatePrice(List<Integer> ingredients, List<Integer> extras) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        if (availableIngredients != null && !availableIngredients.isEmpty()) {
            List<Integer> allIngredients = new ArrayList<>(ingredients);
            allIngredients.addAll(extras);
            float totalPrice = 0;
            // Total sum
            for (Integer ingredientId : allIngredients) {
                totalPrice += getIngredientPrice(ingredientId);
            }

            // Apply "Lot of Beef" discount if applicable
            totalPrice -= calculateDiscountPerPortions(allIngredients,
                    BEEF_PORTION_NAME, BEEF_PORTIONS_PER_DISCOUNT);

            // Apply "Lot of Cheese" discount if applicable
            totalPrice -= calculateDiscountPerPortions(allIngredients,
                    CHEESE_PORTION_NAME, CHEESE_PORTIONS_PER_DISCOUNT);

            // Apply "Light" discount if applicable
            if (isLight(allIngredients)) {
                return totalPrice * IS_LIGHT_DISCOUNT;
            }
            return totalPrice;
        }
        return 0;
    }

    private float getIngredientPrice(int ingredientId) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        for (Ingredient ingredient : availableIngredients) {
            if (ingredient.getId() == ingredientId) {
                return ingredient.getPrice();
            }
        }
        return 0;
    }

    private String getIngredientName(int ingredientId) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        for (Ingredient ingredient : availableIngredients) {
            if (ingredient.getId() == ingredientId) {
                return ingredient.getName();
            }
        }
        return "";
    }

    String buildIngredientsList(List<Integer> ingredients, List<Integer> extras) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        if (availableIngredients != null && !availableIngredients.isEmpty()) {
            StringBuilder list = new StringBuilder();
            List<Integer> allIngredients = new ArrayList<>(ingredients);
            allIngredients.addAll(extras);
            for (int i = 0; i < allIngredients.size(); i++) {
                if (i < allIngredients.size() - 1) {
                    list.append(getIngredientName(allIngredients.get(i)) + ", ");
                } else {
                    list.append(getIngredientName(allIngredients.get(i)));
                }
            }
            return String.valueOf(list);
        }
        return "";
    }

    private boolean isLight(List<Integer> allIngredients) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        int lettuceId = -1;
        int baconId = -1;
        for (Ingredient ingredient : availableIngredients) {
            if (TextUtils.equals(ingredient.getName(), LETTUCE_PORTION_NAME)) {
                lettuceId = ingredient.getId();
            } else if (TextUtils.equals(ingredient.getName(), BACON_PORTION_NAME)) {
                baconId = ingredient.getId();
            }
            if (lettuceId != -1 && baconId != -1) {
                break;
            }
        }
        return (lettuceId != -1 && baconId != -1) &&
                allIngredients.contains(lettuceId) && !allIngredients.contains(baconId);
    }

    private float calculateDiscountPerPortions(List<Integer> allIngredients, String portionName,
                                               int portionsPerDiscount) {
        List<Ingredient> availableIngredients = mIngredientsHolder.getAvailableIngredients();
        int portionId = -1;
        float portionPrice = -1;
        for (Ingredient ingredient : availableIngredients) {
            if (TextUtils.equals(ingredient.getName(), portionName)) {
                portionId = ingredient.getId();
                portionPrice = ingredient.getPrice();
            }
        }
        if (portionId != -1 && portionPrice != -1) {
            int numberOfPortions = 0;
            for (int ingredientId : allIngredients) {
                if (ingredientId == portionId) {
                    numberOfPortions++;
                }
            }
            return (numberOfPortions / portionsPerDiscount) * portionPrice;
        }
        return 0;
    }
}
