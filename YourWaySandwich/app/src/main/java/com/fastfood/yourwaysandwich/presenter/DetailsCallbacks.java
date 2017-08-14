package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.List;

public interface DetailsCallbacks {

    void onSandwichUpdated(Sandwich sandwich);

    void onSandwichOrdered();

    void onCustomizeSelected();

    void onError(String errorTitle, String errorMsg);
}
