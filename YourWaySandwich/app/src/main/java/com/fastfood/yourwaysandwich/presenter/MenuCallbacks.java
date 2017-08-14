package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.List;

public interface MenuCallbacks {

    void onShowSandwichList(List<Sandwich> sandwichList);

    void onError(String errorTitle, String errorMsg);

    void onSandwichSelected();

    void onPromotionsSelected();
}
