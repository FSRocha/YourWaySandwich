package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.List;

public interface CartCallbacks {

    void onShowSandwichList(List<Sandwich> sandwichList, float totalPrice);

    void onError(String errorTitle, String errorMsg);
}
