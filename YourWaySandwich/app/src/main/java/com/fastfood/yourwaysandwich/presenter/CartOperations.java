package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

import com.fastfood.yourwaysandwich.model.service.SandwichDetailsProvider;

public interface CartOperations {

    void createCart(SandwichDetailsProvider sandwichDetailsProvider,
                    CartCallbacks callbacks, Context ctx) throws NullPointerException;

    void destroyCart();
}
