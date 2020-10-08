package ru.job4j.nonblocking;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Main problem of concurrency is problem of shared mutable data.
 * To solve it we can use those solutions:
 * - volatile solves the visibility problem and makes changing of a value atomic
 * - synchronized methods and Lock API. But the process of suspending and then resuming a thread is very expensive
 * and affects the overall efficiency of the system.
 * - In addition to blocking algorithms, there are non-blocking algorithms for concurrent environments.
 * These algorithms exploit low-level atomic machine instructions such as compare-and-swap (CAS), to ensure data integrity.
 *
 * @param <T>  the type of elements held in this stack
 */
@ThreadSafe
public class CASStack<T> {

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T value) {
        Node<T> temp = new Node<>(value);
        Node<T> ref;
        do {
            ref = head.get();
            temp.next = ref;
        } while (!head.compareAndSet(ref, temp));
    }

    public T poll() {
        Node<T> ref;
        Node<T> temp;
        do {
            ref = head.get();
            if (ref == null) {
                throw new IllegalStateException("Stack i,s empty");
            }
            temp = ref.next;
        } while (!head.compareAndSet(ref, temp));
        ref.next = null;
        return ref.value;
    }

    private static final class Node<T> {
        final T value;

        Node<T> next;

        public Node(final T value) {
            this.value = value;
        }
    }
}