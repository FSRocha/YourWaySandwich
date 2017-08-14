package com.fastfood.yourwaysandwich.presenter;

import com.fastfood.yourwaysandwich.model.Promotion;

import java.util.List;

public interface PromotionsCallbacks {

    void onShowPromotionsList(List<Promotion> promotionsList);

    void onError(String errorTitle, String errorMsg);
}
