package ru.job4j.pool;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class ParallelIndexSearchTest {
    private static final int SIZE = 100;
    private static final Integer OBJECT = 1;
    private static Random random = new Random();

    private static Set<Integer> expected;
    private static Integer[] input = new Integer[SIZE];
    private static final ForkJoinPool pool = ForkJoinPool.commonPool();

    static {
        expected = new HashSet<>();
        System.out.println("Result set of indexes: ");
        for (int i = 0; i < SIZE; i++) {
            input[i] = random.nextInt(SIZE / 4);
            if (input[i].equals(OBJECT)) {
                expected.add(i);
                System.out.println(i);
            }
        }
    }

    @Test
    public void parallelSearchTest() {
        Set<Integer> result = ParallelIndexSearch.getSetOfIndexes(input, OBJECT);
        assertEquals(expected, result);
    }

    @Test
    public void parallelSearchSimpleTest() {
        Integer result = PSearch.getIndex(input, OBJECT);
        assertEquals(OBJECT, input[result]);
        System.out.println("Result index: " + result);
    }

}