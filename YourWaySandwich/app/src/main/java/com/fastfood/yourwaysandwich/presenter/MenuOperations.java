package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.service.SandwichDetailsProvider;

public interface MenuOperations {

    void createMenu(AvailableIngredientsProvider ingredientsProvider,
                    SandwichDetailsProvider sandwichDetailsProvider,
                    MenuCallbacks callbacks, Context context) throws NullPointerException;

    void destroyMenu();

    void selectSandwich(Sandwich sandwichId);

    void selectPromotions();

    void selectCart();
}
