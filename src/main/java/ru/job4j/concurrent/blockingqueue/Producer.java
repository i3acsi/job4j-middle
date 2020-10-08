package ru.job4j.concurrent.blockingqueue;

import net.jcip.annotations.Immutable;

import java.util.function.Consumer;

/**
 * Producer puts the data into the queue.
 * There is a simple thread inside it.
 * And the thread's "run" method just calls the queue's "offer" method.
 *
 * @param <T>  the type of elements held in this queue
 */
@Immutable
public class Producer<T> implements Consumer<T> {
    private final SimpleBlockingQueue<T> queue;

    public Producer(final SimpleBlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void accept(T t) {
        new Thread(() -> queue.offer(t)).start();
    }
}
