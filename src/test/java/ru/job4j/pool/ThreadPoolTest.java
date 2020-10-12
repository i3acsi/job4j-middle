package ru.job4j.pool;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ThreadPoolTest {
    @Test
    public void threadPoolTest(){
        ThreadPool threadPool = new ThreadPool();
        AtomicInteger isRunning = new AtomicInteger(0);
        Runnable run1 = ()->{
            isRunning.incrementAndGet();
            System.out.println("task start: " + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
            System.out.println("task completed:" + Thread.currentThread().getName());
            isRunning.decrementAndGet();
        };
        for (int i = 0; i < 30; i++) {
            threadPool.work(run1);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
        threadPool.shutdown();
        assertThat(isRunning.get(), is(0));
    }

    @Test
    public void WhenIncrementInPool()  {
        ThreadPool threadPool = new ThreadPool();
        AtomicInteger num = new AtomicInteger();
        Runnable runnable = () -> System.out.println(num.getAndIncrement());
        for (int i = 0; i < 100; i++) {
            threadPool.work(runnable);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
        threadPool.shutdown();
        assertThat(num.get(), is(100));
    }

}