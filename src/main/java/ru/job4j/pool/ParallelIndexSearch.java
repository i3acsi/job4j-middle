package ru.job4j.pool;


import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearch<T> extends RecursiveTask<Set<Integer>> {
    private static final int THRESHOLD = 10;
    private final T[] array;
    private final int from, to;
    private final T object;
    private final Set<Integer> indexes;

    public ParallelIndexSearch(T[] array, T object) {
        this.array = array;
        this.from = 0;
        this.to = array.length - 1;
        this.object = object;
        this.indexes = new CopyOnWriteArraySet<>();
    }

    private ParallelIndexSearch(T[] array, int from, int to, T object, Set<Integer> indexes) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.object = object;
        this.indexes = indexes;
    }

    @Override
    protected Set<Integer> compute() {
        if (to - from >= THRESHOLD) {
            int mid = (from + to) / 2;
            ParallelIndexSearch<T> leftSearch = new ParallelIndexSearch<>(array, from, mid, object, indexes);
            ParallelIndexSearch<T> rightSearch = new ParallelIndexSearch<>(array, mid + 1, to, object, indexes);
            rightSearch.fork();
            leftSearch.fork();
            leftSearch.join();
            rightSearch.join();
            return indexes;
        } else {
            sequentialSearch();
            return indexes;
        }
    }

    public static <T> Set<Integer> getSetOfIndexes(T[] array, T obj) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new ParallelIndexSearch<>(array, obj));
    }

    private void sequentialSearch() {
        for (int i = from; i <= to; i++) {
            if (object.equals(array[i])) {
                indexes.add(i);
            }
        }
    }
}
