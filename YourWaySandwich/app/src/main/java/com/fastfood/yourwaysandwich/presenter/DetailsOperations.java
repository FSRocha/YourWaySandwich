package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

public interface DetailsOperations {

    void createDetails(SelectedSandwichProvider selectedSandwichProvider,
                       DetailsCallbacks callbacks, Context ctx) throws NullPointerException;

    void destroyDetails();

    void orderSandwich();

    void customizeSandwich();
}
