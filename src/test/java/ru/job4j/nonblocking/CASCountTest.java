package ru.job4j.nonblocking;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CASCountTest {

    @Test
    public void casCountTest() throws InterruptedException {
        int size = 100; //
        CASCount casCount = new CASCount();
        Runnable runnable = casCount::increment;
        List<Thread> threads = Stream.generate(
                ()->new Thread(runnable))
                .limit(size).peek(Thread::start)
                .collect(Collectors.toList());
        for (Thread t : threads) {
            t.join();
        }
        assertEquals(casCount.get(), size);
    }
}