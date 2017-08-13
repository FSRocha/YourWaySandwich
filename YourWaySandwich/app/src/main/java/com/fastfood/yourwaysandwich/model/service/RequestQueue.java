package com.fastfood.yourwaysandwich.model.service;

import java.util.concurrent.ConcurrentLinkedQueue;

class RequestQueue {

    private ConcurrentLinkedQueue<Request> mQueue = new ConcurrentLinkedQueue<>();

    private OnRequestQueueListener mListener;

    RequestQueue(final OnRequestQueueListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("RequestQueue must have an OnRequestQueueListener");
        }
        mListener = listener;
    }

    void queueRequest(final Request request) {
        mQueue.add(request);
        mListener.onQueueIncreased();
    }

    void clearQueue() {
        mQueue.clear();
    }

    Request getRequestFromQueue() {
        return mQueue.poll();
    }

    boolean isQueueEmpty() {
        return mQueue.isEmpty();
    }
}
