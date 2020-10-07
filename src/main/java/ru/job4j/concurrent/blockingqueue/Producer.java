package ru.job4j.concurrent.blockingqueue;

import java.util.function.Consumer;

// Producer помещает данные в очередь
public class Producer<T> implements Consumer<T> {
    private final SimpleBlockingQueue<T> queue;

    public Producer(final SimpleBlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void accept(T t) {
        new Thread(() -> queue.offer(t)).start();
    }
}
