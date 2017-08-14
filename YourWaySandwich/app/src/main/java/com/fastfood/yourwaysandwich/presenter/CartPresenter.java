package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;
import android.util.Log;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.OrderedItem;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.service.RequesterManager;
import com.fastfood.yourwaysandwich.model.service.ResponseListener;
import com.fastfood.yourwaysandwich.model.service.ResponseType;
import com.fastfood.yourwaysandwich.model.service.SandwichDetailsProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter layer for cart view
 */
public class CartPresenter implements CartOperations, ResponseListener {

    private String LOG_TAG = "CartPresenter";

    private CartCallbacks mCallbacks;

    private RequesterManager mRequester;
    private SandwichDetailsProvider mSandwichDetailsProvider;

    private Context mContext;

    @Override
    public void createCart(SandwichDetailsProvider sandwichDetailsProvider,
                           CartCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mSandwichDetailsProvider = sandwichDetailsProvider;
        mCallbacks = callbacks;
        mContext = ctx;
        mRequester = new RequesterManager(this);
        mRequester.getCart();
    }

    @Override
    public void destroyCart() {
        if (mRequester != null) {
            mRequester.tearDown();
        }
    }

    @Override
    public void onResponse(ResponseType type, Object content) {
        switch (type) {
            case CART:
                try {
                    List<OrderedItem> orderedItemsList = (List<OrderedItem>) content;
                    List<Sandwich> sandwichList = getSandwichList(orderedItemsList);
                    float totalPrice = getTotalPrice(sandwichList);
                    mCallbacks.onShowSandwichList(getSandwichList(orderedItemsList), totalPrice);
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.cart_error_msg));
                }
                break;
            case CART_ERROR:
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.cart_error_msg));
                break;
            default:
                Log.e(LOG_TAG, "Unexpected response");
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.invalid_response_error_msg));
        }

    }

    private float getTotalPrice(List<Sandwich> sandwichList) {
        float total_price = 0;
        for (Sandwich sandwich : sandwichList) {
            total_price += sandwich.getPrice();
        }
        return total_price;
    }

    private List<Sandwich> getSandwichList(List<OrderedItem> orderedItemsList) {
        List<Sandwich> sandwichList = new ArrayList<>();
        for (OrderedItem item : orderedItemsList) {
            Sandwich sandwich = new Sandwich();
            sandwich.cloneSandwich(mSandwichDetailsProvider
                    .getSandwichDetails(item.getId_sandwich()));
            sandwich.setExtras(item.getExtras());
            sandwichList.add(sandwich);
        }
        return sandwichList;
    }
}
