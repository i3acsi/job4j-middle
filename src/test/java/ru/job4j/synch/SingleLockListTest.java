package ru.job4j.synch;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SingleLockListTest {

    @Test
    public void add() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        second.start();
        first.join();
        second.join();
        Set<Integer> rsl = new TreeSet<>();
        list.iterator().forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2)));
    }

    @Test
    public void get() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        first.join();
        assertThat(list.get(0), is(1));
        boolean flag = false;
        try {
            list.get(1);
        } catch (NoSuchElementException e) {
            flag = true;
        }
        assertTrue(flag);
        second.start();
        second.join();
        assertThat(list.get(1), is(2));
    }

    @Test
    public void iteratorTest() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>();
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        Thread third = new Thread(() -> list.add(3));
        first.start();
        second.start();
        first.join();
        second.join();
        Iterator<Integer> iterator = list.iterator();
        third.start();
        third.join();

        Set<Integer> rsl = new TreeSet<>();
        iterator.forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2)));

        rsl = new TreeSet<>();
        list.iterator().forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2, 3)));
    }
}