package ru.job4j.concurrent;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread another = new Thread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(Thread.currentThread().getName());
                    }
                }
        );

        Thread second = new Thread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(Thread.currentThread().getName());
                    }
                }
        );
        another.start();
        second.start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName());

        }
    }
}
