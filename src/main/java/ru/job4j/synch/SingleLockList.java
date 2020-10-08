package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.list.DynamicList;

import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private volatile DynamicList<T> list;

    public SingleLockList() {
        this.list = new DynamicList<T>();
    }

    public synchronized void add(T value) {
            list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        int size;
        final DynamicList snapshot = new DynamicList(size = list.size());
        for (int i = 0; i < size; i++) {
            snapshot.add(list.get(i));
        }
        return snapshot.iterator();
    }

    public synchronized Iterator<T> iterator(boolean failFast) {
        int size;
        final DynamicList snapshot = new DynamicList(size = list.size());
        for (int i = 0; i < size; i++) {
            snapshot.add(list.get(i));
        }
        return snapshot.iterator();
    }
}
