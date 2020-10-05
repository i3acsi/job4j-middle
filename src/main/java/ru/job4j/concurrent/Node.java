package ru.job4j.concurrent;

import net.jcip.annotations.Immutable;

@Immutable
public class Node<T> {
    private final Node next;
    private final T value;

    public Node getNext() {
        return next;
    }
    public T getValue() {
        return value;
    }

    public Node(final Node next, final T value) {
        this.next = next;
        this.value = value;
    }

}
