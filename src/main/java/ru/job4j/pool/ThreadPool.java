package ru.job4j.pool;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.concurrent.blockingqueue.SimpleBlockingQueue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread pool reuses previously created threads to execute current
 * tasks and offers a solution to the problem of thread cycle overhead and resource thrashing.
 */
@Immutable
@ThreadSafe
public class ThreadPool {
    private final Thread[] threads;
    private final SimpleBlockingQueue<Runnable> tasks;
    private final int size;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ThreadPool() {
        this.size = Runtime.getRuntime().availableProcessors();
        this.threads = new Thread[size];
        this.tasks = new SimpleBlockingQueue<>();
        init();
    }

    /**
     * Reuse of thread is achieved by calling method run of Runnable object obtained inside method run of the thread.
     */
    private void init() {
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(
                    () -> {
                        while (isRunning.get()) {
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

    public void shutdown() {
        isRunning.set(false);
        for (Thread thread : threads) {
            try {
                thread.join(100);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
