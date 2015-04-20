package com.mygdx.handlers;

import com.mygdx.handlers.net.QueueManager;

/**
 * Created by rob on 4/19/15.
 */
public interface QueueCallback
{
    public enum QueueCallbackStatus {
        CALLBACK_STATUS_NO_REQUEST,
        CALLBACK_STATUS_PENDING_REQUEST,
        CALLBACK_STATUS_RESULTS_WAITING,
        CALLBACK_STATUS_NO_OUTPUT,
        CALLBACK_STATUS_PENDING_OUTPUT,
        CALLBACK_STATUS_OUTPUT_WAITING
    }
    public void addCompleted();
    public void retrieved(Object o, QueueManager.QueueID queueID);
}
