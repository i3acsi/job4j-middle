package ru.job4j.concurrent.sync;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CountTest {

    /**
     * Класс описывает нить со счетчиком.
     */
    private class ThreadCount extends Thread {
        private final Count count;

        private ThreadCount(final Count count) {
            this.count = count;
        }

        @Override
        public void run() {
            this.count.increment();
        }
    }

    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        //Создаем счетчик.
        final Count count = new Count();
        //Создаем нити.
        Thread first = new ThreadCount(count);
        Thread second = new ThreadCount(count);
        //Запускаем нити.
        first.start();
        second.start();
        //Заставляем главную нить дождаться выполнения наших нитей.
        first.join();
        second.join();
        //Проверяем результат.
        assertThat(count.get(), is(2));

    }
}
/*
Как мы видим все тестирование с нитями сводиться к тестирования последовательных операций.
Этого мы достигаем за счет использования метода join.

Давайте теперь посмотрим на класс Count.

Оценим, какие он имеет проблемы:

1. Проблема гонок. Общий ресурс не синхронизирован.
2. Проблема видимости общего ресурса. Одна нить считывает данные из кеша, другая из регистра.

Чтобы решить эти проблемы, мы можем воспользоваться ключевым словом synchronized.

Для того, чтобы правильно определить, какие методы нам нужно синхронизировать, нам нужно определить общие ресурсы.

В наборе Java программиста есть удобный инструмент, который позволяет понять, где у нас общие ресурсы и как мы их синхронизируем.

Для этого нам нужно подключить к нашему проекту библиотеку
 */