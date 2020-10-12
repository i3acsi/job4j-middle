package ru.job4j.pool;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.concurrent.blockingqueue.SimpleBlockingQueue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread pool reuses previously created threads to execute current
 * tasks and offers a solution to the problem of thread cycle overhead and resource thrashing.
 * Reuse of thread is achieved by calling method run of Runnable object obtained inside method run of the thread.
 */
@Immutable
@ThreadSafe
public class ThreadPool {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final Thread[] threads = new Thread[size];
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    {
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(
                    () -> {
                        while (isRunning.get() || this.tasks.size() > 0) {
                            tasks.poll().run();
                        }
                        Thread.currentThread().interrupt();
                    }
            );
            if (isRunning.get()) {
                threads[i].start();
            }
        }
    }


    /**
     * Puts the Runnable job to the tasks queue.
     *
     * @param job Runnable task
     */
    public void work(Runnable job) {
        tasks.offer(job);
    }

    public void shutdown()  {
        isRunning.set(false);
        while (this.tasks.size() != 0) {
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }
}
