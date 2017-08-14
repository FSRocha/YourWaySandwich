package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Sandwich;

public interface MenuOperations {

    void createMenu(AvailableIngredientsProvider ingredientsProvider,
                    MenuCallbacks callbacks, Context context) throws NullPointerException;

    void destroyMenu();

    void selectSandwich(Sandwich sandwichId);

    void selectPromotions();
}
