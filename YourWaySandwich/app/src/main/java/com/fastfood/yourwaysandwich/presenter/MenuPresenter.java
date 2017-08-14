package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;
import android.util.Log;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.service.RequesterManager;
import com.fastfood.yourwaysandwich.model.service.ResponseListener;
import com.fastfood.yourwaysandwich.model.service.ResponseType;

import java.util.List;

/**
 * Presenter layer for menu view
 */
public class MenuPresenter implements MenuOperations, ResponseListener {

    private String LOG_TAG = "MenuPresenter";

    private MenuCallbacks mCallbacks;

    private RequesterManager mRequester;
    private Context mContext;

    @Override
    public void createMenu(MenuCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mCallbacks = callbacks;
        mContext = ctx;
        mRequester = new RequesterManager(this);
        mRequester.getAvailableIngredients();
        mRequester.getMenu();
    }

    @Override
    public void destroyMenu() {
        if (mRequester != null) {
            mRequester.tearDown();
        }
    }

    @Override
    public void onResponse(ResponseType type, Object content) {
        switch (type) {
            case MENU:
                try {
                    mCallbacks.onShowSandwichList((List<Sandwich>) content);
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.menu_error_msg));
                }
                break;
            case AVAILABLE_INGREDIENTS:
                try {
                    ApplicationGlobal app = ApplicationGlobal.getInstance();
                    app.setAvailableIngredients((List<Ingredient>) content);
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.available_ingredients_error_msg));
                }
                break;
            case MENU_ERROR:
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.menu_error_msg));
                break;
            case AVAILABLE_INGREDIENTS_ERROR:
                Log.e(LOG_TAG, "Failed getting ingredients");
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.available_ingredients_error_msg));
                break;
            default:
                Log.e(LOG_TAG, "Unexpected response");
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.invalid_response_error_msg));
        }

    }
}
