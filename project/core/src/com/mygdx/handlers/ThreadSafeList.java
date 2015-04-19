package com.mygdx.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by rob on 4/19/15.
 */
public class ThreadSafeList<E>
{
    private List<E> list;
    private boolean writing;

    public ThreadSafeList()
    {
        list = Collections.synchronizedList(new ArrayList<E>());
    }

    public void add(E add)
    {
        list.add(add);
    }

    public E get(int index)
    {
        return list.get(index);
    }

    public int size()
    {
        return list.size();
    }

    public void clear()
    {
        list.clear();
    }

    public ArrayList<E> getAsyncCopy()
    {
        Iterator<E> iterator = list.iterator();
        ArrayList<E> async = new ArrayList<>();
        while(iterator.hasNext())
            async.add(iterator.next());
        return async;
    }
}
