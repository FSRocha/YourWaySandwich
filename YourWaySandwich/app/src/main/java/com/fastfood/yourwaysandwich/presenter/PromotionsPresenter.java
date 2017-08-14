package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;
import android.util.Log;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Promotion;
import com.fastfood.yourwaysandwich.model.service.RequesterManager;
import com.fastfood.yourwaysandwich.model.service.ResponseListener;
import com.fastfood.yourwaysandwich.model.service.ResponseType;

import java.util.List;

/**
 * Presenter layer for promotions view
 */
public class PromotionsPresenter implements PromotionsOperations, ResponseListener {

    private String LOG_TAG = "PromotionsPresenter";

    private PromotionsCallbacks mCallbacks;

    private RequesterManager mRequester;

    private Context mContext;

    @Override
    public void createPromotions(PromotionsCallbacks callbacks, Context ctx) {
        if (callbacks == null) {
            throw new NullPointerException("Cannot be null");
        }
        mCallbacks = callbacks;
        mContext = ctx;
        mRequester = new RequesterManager(this);
        mRequester.getPromotions();
    }

    @Override
    public void destroyPromotions() {
        if (mRequester != null) {
            mRequester.tearDown();
        }
    }

    @Override
    public void onResponse(ResponseType type, Object content) {
        switch (type) {
            case PROMOTIONS:
                try {
                    mCallbacks.onShowPromotionsList((List<Promotion>) content);
                } catch (ClassCastException e) {
                    mCallbacks.onError(mContext.getString(R.string.general_error_title),
                            mContext.getString(R.string.promotions_error_msg));
                }
                break;
            case PROMOTIONS_ERROR:
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.promotions_error_msg));
                break;
            default:
                Log.e(LOG_TAG, "Unexpected response");
                mCallbacks.onError(mContext.getString(R.string.general_error_title),
                        mContext.getString(R.string.invalid_response_error_msg));
        }

    }
}
