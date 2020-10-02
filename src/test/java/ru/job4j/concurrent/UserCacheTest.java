package ru.job4j.concurrent;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserCacheTest {

    @Test
    public void addUserTest() throws InterruptedException {
        UserCache userCache = new UserCache();
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> userCache.add("User: " + Thread.currentThread().getName()));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        assertThat(userCache.findAll().size(), is(1000));
    }
}