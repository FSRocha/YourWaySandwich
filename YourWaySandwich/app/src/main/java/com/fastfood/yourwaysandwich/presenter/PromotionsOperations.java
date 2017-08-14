package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;

public interface PromotionsOperations {

    void createPromotions(PromotionsCallbacks callbacks, Context ctx) throws NullPointerException;

    void destroyPromotions();
}
