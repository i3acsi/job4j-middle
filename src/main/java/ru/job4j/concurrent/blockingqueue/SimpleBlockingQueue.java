package ru.job4j.concurrent.blockingqueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simple implementation of  bounded blocking queue.
 * This is a blocking queue with bounded size.
 *
 * @param <T> the type of elements held in this queue
 */
@ThreadSafe
@Immutable
public class SimpleBlockingQueue<T> {
    private final SimpleBlockingQueue<T> monitor = this;
    private final int bounds;
    @GuardedBy("monitor")
    private final Queue<T> queue;

    /**
     * Constructor for bounded queue.
     *
     * @param bounds - maximum size of the queue
     */
    public SimpleBlockingQueue(final int bounds) {
        this.queue = new LinkedList<>();
        this.bounds = bounds;
    }


    /**
     * Constructor for unbounded queue.
     */
    public SimpleBlockingQueue() {
        this.queue = new LinkedList<>();
        this.bounds = -1;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do.
     * If it's not possible because transmitted value equals null, method throws an IllegalArgumentException.
     * If the queue is full, the current thread is put into a waiting state.
     * <p>
     * The wait() method causes the current thread to wait indefinitely
     * until another thread either invokes notify() for this object or notifyAll().
     * Also it releases object's monitor.
     * For this, the current thread must own the object's monitor when execute synchronized method for the given object.
     *
     * @param value the element to add
     * @throws IllegalArgumentException if element is null
     */
    public void offer(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null value is not valid");
        } else {
            synchronized (monitor) {
                if (bounds != -1) {
                    while (this.queue.size() >= bounds) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                this.queue.offer(value);
                notify();
            }
        }
    }

    /**
     * Returns an object from the internal collection.
     * If there are no objects in the collection, the current thread is put into a waiting state.
     * Note: When a thread is put in a wait state, it releases the monitor and another thread can execute this method.
     *
     * @return the head of this queue
     */
    public T poll() {
        synchronized (monitor) {
            T result;
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    Thread.currentThread().interrupt();
                }
            }
            result = queue.poll();
            notify();
            return result;
        }
    }
}


