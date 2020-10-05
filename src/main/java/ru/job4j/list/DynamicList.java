package ru.job4j.list;

import java.util.*;

public class DynamicList<E> implements Iterable<E> {
    private E[] container;
    private static final int DEFAULT_SIZE = 10;
    private int index = 0;
    private int modCount = 0;

    public DynamicList(int size) {
        container = (E[]) new Object[size];
    }

    public DynamicList() {
        container = (E[]) new Object[DEFAULT_SIZE];
    }

    public boolean add(E item) {
        checkSize();
        boolean result = false;
        if (item == null) {
            container[index++] = null;
            result = true;
        } else {
            container[index] = item;
            result = container[index++].equals(item);
        }
        if (result) {
            modCount++;
        }
        return result;
    }

    public E get(int position) {
        nseCheck(position);
        return container[position];
    }

    public int size() {
        return index;
    }

    private void checkSize() {
        int oldLength = container.length;
        E[] newContainer;
        if (index == oldLength) {
            newContainer = ((E[]) new Object[(oldLength * 3) / 2 + 1]);
            System.arraycopy(container, 0, newContainer, 0, oldLength);
            container = newContainer;
            modCount++;
        }
    }

    private void nseCheck(int position) {
        if (position >= index) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Iterator<E> iterator() {
        int expectedModCount = this.modCount;
        return new Iterator<>() {
            public int iterIndex = 0;


            @Override
            public boolean hasNext() {
                return (iterIndex < index);
            }

            @Override
            public E next() {
                modCheck();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return container[iterIndex++];
            }

            private void modCheck() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
}
