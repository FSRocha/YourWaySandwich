package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;
import android.util.Log;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.SelectedSandwichProvider;
import com.fastfood.yourwaysandwich.model.service.RequesterManager;
import com.fastfood.yourwaysandwich.model.service.ResponseListener;
import com.fastfood.yourwaysandwich.model.service.ResponseType;

import java.util.List;

/**
 * Presenter layer for details view
 */
public class DetailsPresenter implements DetailsOperations, ResponseListener {

    private String LOG_TAG = "DetailsPresenter";

    private DetailsCallbacks mCallbacks;

    private RequesterManager mRequester;

    private Context mContext;

    private SelectedSandwichProvider mSelectedSandwichProvider;

    @Override
    public void createDetails(SelectedSandwichProvider selectedSandwichProvider,
                              DetailsCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mSelectedSandwichProvider = selectedSandwichProvider;
        mCallbacks = callbacks;
        mContext = ctx;
        mRequester = new RequesterManager(this);
        mCallbacks.onSandwichUpdated(mSelectedSandwichProvider.getSelectedSandwich());
    }

    @Override
    public void destroyDetails() {
        if (mRequester != null) {
            mRequester.tearDown();
        }
    }

    @Override
    public void orderSandwich() {
        Sandwich sandwich = mSelectedSandwichProvider.getSelectedSandwich();
        if (sandwich.hasExtras()) {
            mRequester.orderCustomSandwich(sandwich.getId(), sandwich.getExtrasAsArray());
        } else {
            mRequester.orderSandwich(sandwich.getId());
        }
        mRequester.orderSandwich(mSelectedSandwichProvider.getSelectedSandwich().getId());
    }

    @Override
    public void addExtraIngredients(int[] extras) {

    }

    @Override
    public void removeExtraIngredients(int[] extras) {

    }

    @Override
    public void onResponse(ResponseType type, Object content) {
        switch (type) {
            case ORDERED_SANDWICH:
            case ORDERED_CUSTOM_SANDWICH:
                try {
                    mCallbacks.onSandwichOrdered();
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.order_sandwich_error_message));
                }
                break;
            case ORDERED_SANDWICH_ERROR:
            case ORDERED_CUSTOM_SANDWICH_ERROR:
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.order_sandwich_error_message));
                break;
            default:
                Log.e(LOG_TAG, "Unexpected response");
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.invalid_response_error_msg));
        }

    }
}
