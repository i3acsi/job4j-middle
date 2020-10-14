package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class PSearch<T> extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 10;
    private final T[] array;
    private final int from, to;
    private final T object;

    public PSearch(T[] array, T object) {
        this.array = array;
        this.from = 0;
        this.to = array.length - 1;
        this.object = object;
    }

    private PSearch(T[] array, int from, int to, T object) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.object = object;
    }

    @Override
    protected Integer compute() {
        if (to - from >= THRESHOLD) {
            int mid = (from + to) / 2;
            PSearch<T> leftSearch = new PSearch<>(array, from, mid, object);
            PSearch<T> rightSearch = new PSearch<>(array, mid + 1, to, object);
            rightSearch.fork();
            leftSearch.fork();
            int right = rightSearch.join();
            int left = leftSearch.join();
            return left != -1 ? left : right;
        } else {
            return findIndexElement(array, from, to, object);
        }
    }

    public static <T> Integer getIndex(T[] array, T obj) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new PSearch<T>(array, obj));
    }

    private static <T> Integer findIndexElement(T[] array, int from, int to, T object) {
        for (int i = from; i <= to; i++) {
            if (array[i].equals(object)) {
                return i;
            }
        }
        return -1;
    }
}