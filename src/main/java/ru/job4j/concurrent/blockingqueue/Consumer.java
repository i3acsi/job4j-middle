package ru.job4j.concurrent.blockingqueue;

import java.util.function.Supplier;

/**
 * Consumer retrieves data from the queue,
 * There is a simple thread inside it.
 * And the thread's "run" method just calls the queue's "poll" method.
 *
 * @param <T>  the type of elements held in this queue
 */
public class Consumer<T> implements Supplier<T> {
    private final SimpleBlockingQueue<T> queue;
    private volatile T value = null;
    public Consumer(SimpleBlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public T get() {
        this.value = null;
        Thread t = new Thread(()-> this.value = queue.poll());
        t.start();
        while (this.value == null) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.value;
    }
}

