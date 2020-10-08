package ru.job4j.concurrent;

import org.junit.Test;
import ru.job4j.concurrent.blockingqueue.Consumer;
import ru.job4j.concurrent.blockingqueue.Producer;
import ru.job4j.concurrent.blockingqueue.SimpleBlockingQueue;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleBlockingQueueTest {

    @Test
    public void blockingQueueTest() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> IntStream.range(0, 5).forEach(
                        queue::offer
                )
        );

        producer.start();
        /* Здесь мы проверяем, что очередь пустая или нить прервали.
        Если производитель закончил свою работу и сразу подаст сигнал об отключении потребителя, то мы не сможет прочитать все данные.
        С другой стороны, если мы успели прочитать все данные и находимся в режиме wait пришедший сигнал запустит нить и
        проверит состояние очереди и завершит цикл. Потребитель закончит свою работу. */
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        buffer.add(queue.poll());
                    }
                }
        );
        consumer.start();
        /* Если мы запустим наш тест несколько раз, то мы увидим. что наш тест иногда работает, а иногда нет.
        Это связано с тем. что главная нить не дожидается выполнения потребителя и производителя. */
        producer.join();
        consumer.interrupt();
        consumer.join();
        /*  Сначала дожидаемся завершения работы производителя.
        Далее посылаем сигнал, что потребителю можно остановиться.
        Ждем пока потребитель прочитает все данные и завершит свою работу.*/

        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }

    @Test
    public void producerConsumerTest() {
        final SimpleBlockingQueue<Runnable> queue = new SimpleBlockingQueue<>(10);
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

    private static Runnable getTask() {
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