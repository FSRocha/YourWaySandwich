package com.fastfood.yourwaysandwich.model.service;

import android.util.Log;

import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.OrderedItem;
import com.fastfood.yourwaysandwich.model.Promotion;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.YourWayMobileApi;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequesterManager implements YourWayApi, OnRequestQueueListener {

    private String LOG_TAG = "RequesterManager";

    private ResponseListener mListener = null;

    private Request mCurrentRequest = null;

    private RequestQueue mRequestQueue = null;

    public RequesterManager(ResponseListener responseListener) {
        mRequestQueue = new RequestQueue(this);
        mListener = responseListener;
    }

    public void tearDown() {
        mRequestQueue.clearQueue();
        mRequestQueue = null;
        mListener = null;
    }

    @Override
    public void onQueueIncreased() {
        Log.d(LOG_TAG, "onQueueIncreased");
        processQueue();
    }

    /**
     * Consume and process the next request on the queue
     */
    private void processQueue() {
        if (mCurrentRequest != null) {
            Log.d(LOG_TAG, "Service busy processing another request ");
            return;
        }

        if (mRequestQueue != null) {
            if (mRequestQueue.isQueueEmpty()) {
                Log.d(LOG_TAG, "No request to process");
                return;
            }

            mCurrentRequest = mRequestQueue.getRequestFromQueue();
            int sandwichId;
            int[] extras;

            switch (mCurrentRequest.getType()) {
                case GET_MENU:
                    getMenuImpl();
                    break;
                case GET_SANDWICH_INGREDIENTS:
                    sandwichId = ((SandwichIngredientsRequest)
                            mCurrentRequest).getSandwichId();
                    getSandwichIngredientsImpl(sandwichId);
                    break;
                case GET_SANDWICH_DETAILS:
                    sandwichId = ((SandwichDetailsRequest)
                            mCurrentRequest).getSandwichId();
                    getSandwichDetailsImpl(sandwichId);
                    break;
                case GET_AVAILABLE_INGREDIENTS:
                    getAvailableIngredientsImpl();
                    break;
                case ORDER_SANDWICH:
                    sandwichId = ((OrderSandwichRequest)
                            mCurrentRequest).getSandwichId();
                    orderSandwichImpl(sandwichId);
                    break;
                case ORDER_CUSTOM_SANDWICH:
                    sandwichId = ((OrderCustomSandwichRequest)
                            mCurrentRequest).getSandwichId();
                    extras = ((OrderCustomSandwichRequest)
                            mCurrentRequest).getExtras();
                    orderCustomSandwichImpl(sandwichId, extras);
                    break;
                case GET_PROMOTIONS:
                    getPromotionsImpl();
                    break;
                case GET_CART:
                    getCartImpl();
                    break;
                default:
                    Log.d(LOG_TAG, "Unknown request type");
            }
        }
    }

    @Override
    public void getMenu() {
        Log.d(LOG_TAG, "getMenu called from client");
        mRequestQueue.queueRequest(new Request(RequestType.GET_MENU));
    }

    @Override
    public void getSandwichIngredients(int sandwichId) {
        Log.d(LOG_TAG, "getSandwichDetails called from client");
        mRequestQueue.queueRequest(new SandwichIngredientsRequest(sandwichId));
    }

    @Override
    public void getSandwichDetails(int sandwichId) {
        Log.d(LOG_TAG, "getSandwichDetails called from client");
        mRequestQueue.queueRequest(new SandwichDetailsRequest(sandwichId));
    }

    @Override
    public void getAvailableIngredients() {
        Log.d(LOG_TAG, "getAvailableIngredients called from client");
        mRequestQueue.queueRequest(new Request(RequestType.GET_AVAILABLE_INGREDIENTS));
    }

    @Override
    public void orderSandwich(int sandwichId) {
        Log.d(LOG_TAG, "orderSandwich called from client");
        mRequestQueue.queueRequest(new OrderSandwichRequest(sandwichId));
    }

    @Override
    public void orderCustomSandwich(int sandwichId, int[] extras) {
        Log.d(LOG_TAG, "orderCustomSandwich called from client");
        mRequestQueue.queueRequest(new OrderCustomSandwichRequest(sandwichId, extras));
    }

    @Override
    public void getPromotions() {
        Log.d(LOG_TAG, "getPromotions called from client");
        mRequestQueue.queueRequest(new Request(RequestType.GET_PROMOTIONS));
    }

    @Override
    public void getCart() {
        Log.d(LOG_TAG, "getCart called from client");
        mRequestQueue.queueRequest(new Request(RequestType.GET_CART));
    }

    /**
     * Notify all registered listener about server responses.
     *
     * @param type    The type of the response, used by the listener to identify to which request
     *                this response is related to.
     * @param content An object containing the payload content of the response, it should be casted
     *                by the listener to the proper Object kind, depending on the response type.
     */
    private void notifyListener(ResponseType type, Object content) {
        Log.d(LOG_TAG, "notifyListener");
        mCurrentRequest = null;

        if (mListener != null) {
            mListener.onResponse(type, content);
        }
        processQueue();
    }

    private YourWayMobileApi getMobileApiAccess() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YourWayMobileApi.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(YourWayMobileApi.class);
    }

    private void getMenuImpl() {
        Call<List<Sandwich>> call = getMobileApiAccess().getMenu();
        call.enqueue(new Callback<List<Sandwich>>() {
            @Override
            public void onResponse(Call<List<Sandwich>> call, Response<List<Sandwich>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<Sandwich> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListener(ResponseType.MENU, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListener(ResponseType.MENU_ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListener(ResponseType.MENU_ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.MENU_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Sandwich>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.MENU_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getSandwichIngredientsImpl(int sandwichId) {
        Call<List<Ingredient>> call = getMobileApiAccess().getSandwichIngredients(sandwichId);
        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<Ingredient> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListener(ResponseType.SANDWICH_INGREDIENTS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListener(ResponseType.SANDWICH_INGREDIENTS_ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListener(ResponseType.SANDWICH_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.SANDWICH_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.SANDWICH_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getSandwichDetailsImpl(int sandwichId) {
        Call<Sandwich> call = getMobileApiAccess().getSandwichDetails(sandwichId);
        call.enqueue(new Callback<Sandwich>() {
            @Override
            public void onResponse(Call<Sandwich> call, Response<Sandwich> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        Sandwich resp = response.body();
                        if (resp != null) {
                            notifyListener(ResponseType.SANDWICH_DETAILS, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListener(ResponseType.SANDWICH_DETAILS_ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.SANDWICH_DETAILS_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Sandwich> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.SANDWICH_DETAILS_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getAvailableIngredientsImpl() {
        Call<List<Ingredient>> call = getMobileApiAccess().getAvailableIngredients();
        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<Ingredient> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListener(ResponseType.AVAILABLE_INGREDIENTS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListener(ResponseType.AVAILABLE_INGREDIENTS_ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListener(ResponseType.AVAILABLE_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.AVAILABLE_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.AVAILABLE_INGREDIENTS_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void orderSandwichImpl(int sandwichId) {
        Call<OrderedItem> call = getMobileApiAccess().orderSandwich(sandwichId);
        call.enqueue(new Callback<OrderedItem>() {
            @Override
            public void onResponse(Call<OrderedItem> call, Response<OrderedItem> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        OrderedItem resp = response.body();
                        if (resp != null) {
                            notifyListener(ResponseType.ORDERED_SANDWICH, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListener(ResponseType.ORDERED_SANDWICH_ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.ORDERED_SANDWICH_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<OrderedItem> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.ORDERED_SANDWICH_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void orderCustomSandwichImpl(int sandwichId, int[] extras) {
        Call<OrderedItem> call = getMobileApiAccess().orderCustomSandwich(sandwichId, extras);
        call.enqueue(new Callback<OrderedItem>() {
            @Override
            public void onResponse(Call<OrderedItem> call, Response<OrderedItem> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        OrderedItem resp = response.body();
                        if (resp != null) {
                            notifyListener(ResponseType.ORDERED_CUSTOM_SANDWICH, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListener(ResponseType.ORDERED_CUSTOM_SANDWICH_ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.ORDERED_CUSTOM_SANDWICH_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<OrderedItem> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.ORDERED_CUSTOM_SANDWICH_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getPromotionsImpl() {
        Call<List<Promotion>> call = getMobileApiAccess().getPromotions();
        call.enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<Promotion> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListener(ResponseType.PROMOTIONS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListener(ResponseType.PROMOTIONS_ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListener(ResponseType.PROMOTIONS_ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.PROMOTIONS_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.PROMOTIONS_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getCartImpl() {
        Call<List<OrderedItem>> call = getMobileApiAccess().getCart();
        call.enqueue(new Callback<List<OrderedItem>>() {
            @Override
            public void onResponse(Call<List<OrderedItem>> call, Response<List<OrderedItem>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<OrderedItem> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListener(ResponseType.CART, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListener(ResponseType.CART_ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListener(ResponseType.CART_ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListener(ResponseType.CART_ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<OrderedItem>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListener(ResponseType.CART_ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }
}