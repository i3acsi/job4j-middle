package ru.job4j.nonblocking;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount<T> {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public int get() {
        return count.get();
    }

    public void increment() {
        int t;
        do {
            t = count.get();
        }
        while (!count.compareAndSet(t, t + 1));
    }
}

