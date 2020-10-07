package ru.job4j.concurrent;

import org.junit.Test;
import ru.job4j.concurrent.blockingqueue.Consumer;
import ru.job4j.concurrent.blockingqueue.Producer;
import ru.job4j.concurrent.blockingqueue.SimpleBlockingQueue;

public class SimpleBlockingQueueTest {
    private final SimpleBlockingQueue<Runnable> queue = new SimpleBlockingQueue<>(10);

    @Test
    public void producerConsumerTest() {
        Producer<Runnable> producer = new Producer<>(queue);
        for (int i = 0; i < 10; i++) {
            producer.accept(getTask());
        }
        Consumer<Runnable> consumer = new Consumer<>(queue);
        for (int i = 0; i < 10; i++) {
            Runnable r = consumer.get();
            r.run();
        }
    }

    public static Runnable getTask() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("task started: " + this);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("task finished: " + this);
            }
        };
    }
}