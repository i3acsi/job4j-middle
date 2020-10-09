package ru.job4j.nonblocking;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;

public class NonBlockingCacheTest {

    @Test
    public void addModelsTest() throws InterruptedException {
        NonBlockingCache cache = new NonBlockingCache();
        List<Integer> expectedIds = fillCache(cache);

        List<Integer> resultIds = new ArrayList<>();
        cache.findAll().forEach(x -> resultIds.add(((Base) x).getId()));
        Collections.sort(resultIds);
        Assert.assertThat(resultIds, is(expectedIds));
        Base base = new Base(1, "name");
        Assert.assertFalse(cache.add(base));
    }

    @Test
    public void updateCacheTest() throws InterruptedException {
        NonBlockingCache cache = new NonBlockingCache();
        List<Integer> expectedIds = fillCache(cache);
        List<Integer> resultIds = new ArrayList<>();
        cache.findAll().forEach(x -> resultIds.add(((Base) x).getId()));
        Collections.sort(resultIds);

        Assert.assertThat(resultIds, is(expectedIds));
        Base base = new Base(1, "name");
        boolean flag = false;
        try {
            cache.update(base);
        } catch (OptimisticException e) {
            flag = true;
        }
        cache.findAll().forEach(x -> {
            Base b = (Base) x;
            if (b.getId() == 1) {
                Assert.assertThat(b.getName(), is("firstName"));
            }
        });
        Assert.assertTrue(flag);
        Base newBase = Base.changeName(base, "newName");
        try {
            cache.update(newBase);
        } catch (OptimisticException e) {
            flag = false;
        }
        Assert.assertTrue(flag);
        cache.findAll().forEach(x -> {
            Base b = (Base) x;
            if (b.getId() == 1) {
                Assert.assertThat(b.getName(), is("newName"));
            }
        });
    }

    @Test
    public void deleteFromCacheTest() throws InterruptedException {
        NonBlockingCache cache = new NonBlockingCache();
        List<Integer> expectedIds = fillCache(cache);
        List<Integer> resultIds = new ArrayList<>();
        cache.findAll().forEach(x -> resultIds.add(((Base) x).getId()));
        Collections.sort(resultIds);

        Assert.assertThat(resultIds, is(expectedIds));
        Base base = new Base(50, "firstName");
        Assert.assertFalse(cache.delete(base));
        Assert.assertThat(cache.findAll().count(), is(20L));
        base = new Base(1, "firstName");
        Assert.assertTrue(cache.delete(base));
        Assert.assertThat(cache.findAll().count(), is(19L));
    }

    private List<Integer> fillCache(NonBlockingCache cache) throws InterruptedException {
        int size = 20;
        Integer[] ints = new Integer[size];
        for (int i = 0; i < size; i++) {
            ints[i] = i;
        }
        AtomicInteger index = new AtomicInteger(0);
        List<Integer> expectedIds = List.of(ints);
        Thread[] threads = new Thread[size];
        Runnable fillCache = () -> {
            Base tmp = new Base(index.getAndIncrement(), "firstName");
            cache.add(tmp);
        };
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(fillCache);
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        return expectedIds;
    }

}