package ru.job4j.concurrent.blockingqueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final SimpleBlockingQueue<T> monitor = this;
    private final int bounds;
    @GuardedBy("monitor")
    private Queue<T> queue;

    public SimpleBlockingQueue(final int bounds) {
        this.queue = new LinkedList<>();
        this.bounds = bounds;
    }

    public void offer(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null value is not valid");
        } else {
            synchronized (monitor) {
                while (this.queue.size() >= bounds) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.queue.offer(value);
                notify();
            }
        }
    }

    /*
    Метод poll() должен вернуть объект из внутренней коллекции. Если в коллекции объектов нет, то нужно перевести текущую нить в состояние ожидания.
    Важный момент, когда нить переводить в состояние ожидания, то она отпускает объект монитор и другая нить тоже может выполнить этот метод.
     */

    public T poll() {
        synchronized (monitor) {
            T result;
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            result = queue.poll();
            notify();
            return result;
        }
    }

// метод wait свобождает монитор. В нутри sync метода поток может выполняться только если имеет монитор (если захватил монитор),
// если поток освобождает монитор, то он блокируется. Поток, который вызвал wait сам не разблокируется,
// пока на том же самом мониторе в другом потоке не будет вызван метод notify
//
}


