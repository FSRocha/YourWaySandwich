package com.fastfood.yourwaysandwich.model.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.OrderedItem;
import com.fastfood.yourwaysandwich.model.Promotion;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.model.YourWayMobileApi;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequesterService extends Service implements OnRequestQueueListener {

    private String LOG_TAG = "RequesterService";

    private Binder mYourWayBinder = new YourWayBinder();

    private List<ResponseListener> mListeners = new ArrayList<>();

    private Request mCurrentRequest = null;

    private RequestQueue mRequestQueue = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "OnBind");
        mRequestQueue = new RequestQueue(this);
        return mYourWayBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "OnUnbind");
        mRequestQueue.clearQueue();
        mRequestQueue = null;
        return super.onUnbind(intent);
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
                    getMenu();
                    break;
                case GET_SANDWICH_INGREDIENTS:
                    sandwichId = ((SandwichIngredientsRequest)
                            mCurrentRequest).getSandwichId();
                    getSandwichIngredients(sandwichId);
                    break;
                case GET_SANDWICH_DETAILS:
                    sandwichId = ((SandwichDetailsRequest)
                            mCurrentRequest).getSandwichId();
                    getSandwichDetails(sandwichId);
                    break;
                case GET_AVAILABLE_INGREDIENTS:
                    getAvailableIngredients();
                    break;
                case ORDER_SANDWICH:
                    sandwichId = ((OrderSandwichRequest)
                            mCurrentRequest).getSandwichId();
                    orderSandwich(sandwichId);
                    break;
                case ORDER_CUSTOM_SANDWICH:
                    sandwichId = ((OrderCustomSandwichRequest)
                            mCurrentRequest).getSandwichId();
                    extras = ((OrderCustomSandwichRequest)
                            mCurrentRequest).getExtras();
                    orderCustomSandwich(sandwichId, extras);
                    break;
                case GET_PROMOTIONS:
                    getPromotions();
                    break;
                case GET_CART:
                    getCart();
                    break;
                default:
                    Log.d(LOG_TAG, "Unknown request type");
            }
        }
    }

    /**
     * Binder responsible for exposing the Your Way API to the client bound to it.
     */
    public class YourWayBinder extends Binder implements YourWayApi {

        @Override
        public void getMenu() {
            Log.d(LOG_TAG, "getMenu called from client");
            mRequestQueue.queueRequest(new Request(RequestType.GET_MENU));
        }

        @Override
        public void getSandwichIngredients(int sandwichId) {
            Log.d(LOG_TAG, "getSandwichIngredients called from client");
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

        @Override
        public void registerResponseListener(ResponseListener listener) {
            Log.d(LOG_TAG, "registerResponseListener called from client");
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }

        @Override
        public void unregisterResponseListener(ResponseListener listener) {
            Log.d(LOG_TAG, "unregisterResponseListener called from client");
            mListeners.remove(listener);
        }
    }

    /**
     * Notify all registered listener about server responses.
     *
     * @param type    The type of the response, used by the listener to identify to which request
     *                this response is related to.
     * @param content An object containing the payload content of the response, it should be casted
     *                by the listener to the proper Object kind, depending on the response type.
     */
    void notifyListeners(ResponseType type, Object content) {
        Log.d(LOG_TAG, "notifyListeners");
        mCurrentRequest = null;

        if (!mListeners.isEmpty()) {
            for (ResponseListener listener : mListeners) {
                listener.onResponse(type, content);
            }
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

    private void getMenu() {
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
                                notifyListeners(ResponseType.MENU, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Sandwich>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getSandwichIngredients(int sandwichId) {
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
                                notifyListeners(ResponseType.SANDWICH_INGREDIENTS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getSandwichDetails(int sandwichId) {
        Call<Sandwich> call = getMobileApiAccess().getSandwichDetails(sandwichId);
        call.enqueue(new Callback<Sandwich>() {
            @Override
            public void onResponse(Call<Sandwich> call, Response<Sandwich> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        Sandwich resp = response.body();
                        if (resp != null) {
                            notifyListeners(ResponseType.SANDWICH_DETAILS, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<Sandwich> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getAvailableIngredients() {
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
                                notifyListeners(ResponseType.AVAILABLE_INGREDIENTS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void orderSandwich(int sandwichId) {
        Call<OrderedItem> call = getMobileApiAccess().orderSandwich(sandwichId);
        call.enqueue(new Callback<OrderedItem>() {
            @Override
            public void onResponse(Call<OrderedItem> call, Response<OrderedItem> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        OrderedItem resp = response.body();
                        if (resp != null) {
                            notifyListeners(ResponseType.ORDERED_SANDWICH, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<OrderedItem> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void orderCustomSandwich(int sandwichId, int[] extras) {
        Call<OrderedItem> call = getMobileApiAccess().orderCustomSandwich(sandwichId, extras);
        call.enqueue(new Callback<OrderedItem>() {
            @Override
            public void onResponse(Call<OrderedItem> call, Response<OrderedItem> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        OrderedItem resp = response.body();
                        if (resp != null) {
                            notifyListeners(ResponseType.ORDERED_CUSTOM_SANDWICH, resp);
                        } else {
                            Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                            notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<OrderedItem> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getPromotions() {
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
                                notifyListeners(ResponseType.PROMOTIONS, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }

    private void getCart() {
        Call<List<Sandwich>> call = getMobileApiAccess().getCart();
        call.enqueue(new Callback<List<Sandwich>>() {
            @Override
            public void onResponse(Call<List<Sandwich>> call, Response<List<Sandwich>> response) {
                int code = response.code();
                switch (code) {
                    case HttpsURLConnection.HTTP_OK:
                        List<Sandwich> resp = response.body();
                        if (resp != null) {
                            if (!resp.isEmpty()) {
                                notifyListeners(ResponseType.CART, resp);
                            } else {
                                Log.d(LOG_TAG, "Failed EMPTY_RESPONSE");
                                notifyListeners(ResponseType.ERROR, ErrorCode.EMPTY_RESPONSE);
                            }
                        } else {
                            Log.d(LOG_TAG, "Failed NULL response");
                            notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                        }
                        break;
                    default:
                        Log.d(LOG_TAG, "Response code: " + response.code());
                        Log.d(LOG_TAG, "Response message: " + response.message());
                        notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Sandwich>> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
                Log.d(LOG_TAG, "Throwable " + t.getMessage());
                notifyListeners(ResponseType.ERROR, ErrorCode.GENERAL_ERROR);
            }
        });
    }
}