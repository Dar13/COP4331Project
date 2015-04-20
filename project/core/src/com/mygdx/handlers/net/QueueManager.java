package com.mygdx.handlers.net;

import com.mygdx.handlers.QueueCallback;
import com.mygdx.handlers.ThreadSafeList;
import com.mygdx.handlers.action.Action;

import java.util.ArrayList;

/**
 * Created by rob on 4/19/15.
 */
public class QueueManager
{
    public enum QueueID
    {
        QUEUE_ID_SEND,
        QUEUE_ID_RECEIVE
    }
    private static final String LOG_TAG = "QueueManager";

    private static ThreadSafeList<Action> toSend = new ThreadSafeList<>();
    private static ThreadSafeList<Action> received = new ThreadSafeList<>();

    private NetworkManager networkManager;

    public QueueManager(NetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }

    public void addAllSendToReceived()
    {
        new SendQueueLooper().start();
    }

    public void addToSendQueue(Action action, QueueCallback callback)
    {
        new ReceivedObjectHandler(action, callback, QueueID.QUEUE_ID_SEND);
    }

    public void addToReceivedQueue(Action action, QueueCallback callback)
    {
        new ReceivedObjectHandler(action, callback, QueueID.QUEUE_ID_RECEIVE).start();
    }

    public void fetchQueue(QueueCallback callback, QueueID id)
    {
        new FetchQueue(callback, id).start();
    }

    public void fetchWhenNotEmpty(QueueCallback callback, QueueID id)
    {
        new FetchWhenNotEmpty(callback, id);
    }

    public void clear(QueueID queueID)
    {
        switch (queueID)
        {
            case QUEUE_ID_RECEIVE:
                received.clear();
                break;
            case QUEUE_ID_SEND:
                toSend.clear();
                break;
        }
    }

    protected class ReceivedObjectHandler extends Thread implements Runnable
    {
        Object obj;
        QueueCallback callback;
        QueueID queueID;

        public ReceivedObjectHandler(Object obj, QueueCallback callback, QueueID queueID)
        {
            this.obj = obj;
            this.callback = callback;
            this.queueID = queueID;
        }

        @Override
        public void run()
        {
            switch(queueID)
            {
                case QUEUE_ID_RECEIVE:
                    received.add((Action) obj);
                    break;
                case QUEUE_ID_SEND:
                    toSend.add((Action) obj);
                    break;
            }

            if(callback != null)
                callback.addCompleted();
        }
    }

    protected class FetchQueue extends Thread implements Runnable
    {
        QueueCallback callback;
        QueueID id;

        public FetchQueue(QueueCallback callback, QueueID id)
        {
            this.callback = callback;
            this.id = id;
        }

        @Override
        public void run()
        {
            switch(id)
            {
                case QUEUE_ID_RECEIVE:
                    callback.retrieved(received.getAsyncCopy(), QueueID.QUEUE_ID_RECEIVE);
                    break;
                case QUEUE_ID_SEND:
                    callback.retrieved(toSend.getAsyncCopy(), QueueID.QUEUE_ID_SEND);
                    break;
            }
        }
    }

    protected class FetchWhenNotEmpty extends Thread implements Runnable
    {
        QueueCallback callback;
        QueueID id;

        public FetchWhenNotEmpty(QueueCallback callback, QueueID id)
        {
            this.callback = callback;
            this.id = id;
        }

        @Override
        public void run()
        {
            switch(id)
            {
                case QUEUE_ID_RECEIVE:
                    while(received.isEmpty())
                    {
                        try
                        {
                            Thread.sleep(50);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            System.out.println("Queue Manager: Retrieval Thread was Interrupted");
                        }
                    }
                    callback.retrieved(received.getAsyncCopy(), QueueID.QUEUE_ID_RECEIVE);
                    break;

                case QUEUE_ID_SEND:
                    while(toSend.isEmpty())
                    {
                        try
                        {
                            Thread.sleep(50);

                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            System.out.println("Queue Manager: Retrieval Thread was Interrupted");
                        }
                    }
                    callback.retrieved(toSend.getAsyncCopy(), QueueID.QUEUE_ID_SEND);
                    break;
            }
        }
    }

    protected class SendQueueLooper extends Thread implements Runnable
    {
        @Override
        public void run()
        {
            ArrayList<Action> temp = toSend.getAsyncCopy();
            for(Action action : temp)
            {
                received.add(action);
            }
        }
    }
}
