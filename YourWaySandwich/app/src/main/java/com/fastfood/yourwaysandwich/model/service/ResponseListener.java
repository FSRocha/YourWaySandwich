package com.fastfood.yourwaysandwich.model.service;

public interface ResponseListener {

    /**
     * Callback to deliver the request response from Your Way server to the listener.
     *
     * @param type The type of the response used by the listener to identify to which request this
     *             response is related to.
     * @param content An object containing the payload content of the response, it should be casted
     *                by the listener to the proper Object kind, depending on the response type.
     **/
    void onResponse(ResponseType type, Object content);
}
