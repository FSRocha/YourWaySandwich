package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;
import android.util.Log;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.service.RequesterManager;
import com.fastfood.yourwaysandwich.model.service.ResponseListener;
import com.fastfood.yourwaysandwich.model.service.ResponseType;
import com.fastfood.yourwaysandwich.model.service.SandwichDetailsProvider;

import java.util.List;

/**
 * Presenter layer for menu view
 */
public class MenuPresenter implements MenuOperations, ResponseListener {

    private String LOG_TAG = "MenuPresenter";

    private MenuCallbacks mCallbacks;

    private RequesterManager mRequester;
    private Context mContext;
    private AvailableIngredientsProvider mIngredientsProvider;
    private SandwichDetailsProvider mSandwichDetailsProvider;

    @Override
    public void createMenu(AvailableIngredientsProvider ingredientsProvider,
                           SandwichDetailsProvider sandwichDetailsProvider,
                           MenuCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mIngredientsProvider = ingredientsProvider;
        mSandwichDetailsProvider = sandwichDetailsProvider;
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
    public void selectSandwich(Sandwich selectedSandwich) {
        ApplicationGlobal app = ApplicationGlobal.getInstance();
        app.setSelectedSandwich(selectedSandwich);
        mCallbacks.onSandwichSelected();
    }

    @Override
    public void selectPromotions() {
        mCallbacks.onPromotionsSelected();
    }

    @Override
    public void selectCart() {
        mCallbacks.onCartSelected();
    }

    @Override
    public void onResponse(ResponseType type, Object content) {
        switch (type) {
            case MENU:
                try {
                    List<Sandwich> sandwichList = (List<Sandwich>) content;
                    mSandwichDetailsProvider.addSandwichDetails(sandwichList);
                    mCallbacks.onShowSandwichList(sandwichList);
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.menu_error_msg));
                }
                break;
            case AVAILABLE_INGREDIENTS:
                try {
                    mIngredientsProvider.setAvailableIngredients((List<Ingredient>) content);
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
