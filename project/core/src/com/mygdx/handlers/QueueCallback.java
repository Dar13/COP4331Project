package com.mygdx.handlers;

/**
 * Created by rob on 4/19/15.
 */
public interface QueueCallback
{
    public enum QueueCallbackStatus {
        CALLBACK_STATUS_NO_REQUEST,
        CALLBACK_STATUS_PENDING_REQUEST,
        CALLBACK_STATUS_RESULTS_WAITING
    }
    public void addCompleted();
    public void retrieved(Object o);
}
