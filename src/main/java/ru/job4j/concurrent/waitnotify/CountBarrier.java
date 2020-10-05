package ru.job4j.concurrent.waitnotify;

/**
 * Переменная total содержит количество вызовом метода count().
 * <p>
 * Нити, которые выполняют метод await, могут начать работу если поле count == total.
 * Если оно не равно, то нужно перевести нить в состояние wait.
 */
public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            this.count++;
            if (this.count >= total) {
                monitor.notifyAll();
            }
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
