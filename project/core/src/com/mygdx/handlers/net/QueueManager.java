package com.mygdx.handlers.net;

import com.mygdx.handlers.QueueCallback;
import com.mygdx.handlers.ThreadSafeList;
import com.mygdx.handlers.action.Action;

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

    private static ThreadSafeList<Action> toSend = new ThreadSafeList<>();
    private static ThreadSafeList<Action> received = new ThreadSafeList<>();

    private NetworkManager networkManager;

    public QueueManager(NetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }

    public void addToReceivedQueue(Action action, QueueCallback callback)
    {
        new ReceivedObjectHandler(action, callback).start();
    }

    public void fetchQueue(QueueCallback callback, QueueID id)
    {
        new FetchQueue(callback, id).start();
    }

    public void fetchWhenNotEmpty(QueueCallback callback, QueueID id)
    {
        new FetchWhenNotEmpty(callback, id);
    }

    protected class ReceivedObjectHandler extends Thread implements Runnable
    {
        Object obj;
        QueueCallback callback;

        public ReceivedObjectHandler(Object obj, QueueCallback callback)
        {
            this.obj = obj;
            this.callback = callback;
        }

        @Override
        public void run()
        {
            received.add((Action) obj);
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
                    callback.retrieved(received.getAsyncCopy());
                    break;
                case QUEUE_ID_SEND:
                    callback.retrieved(toSend.getAsyncCopy());
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
                    callback.retrieved(received.getAsyncCopy());
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
                    callback.retrieved(toSend.getAsyncCopy());
            }
        }
    }
}
