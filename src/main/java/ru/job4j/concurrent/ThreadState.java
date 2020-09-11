package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );

        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        System.out.println("first thread state: " + first.getState());
        System.out.println("second thread state: " + second.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.println("first thread state: " + first.getState());
            System.out.println("second thread state: " + second.getState());
        }
        // условие в цикле while означает, что main не продвинется дальше этого цикла,
        // пока оба потока не будут завершены
        System.out.println("first thread state: " + first.getState());
        System.out.println("second thread state: " + second.getState());
        System.out.println("All threads are terminated");
    }
}
