package ru.job4j.switcher;

public class Switcher {
    public static void main(String[] args) throws InterruptedException {
        MasterSlaveBarrier barrier = new MasterSlaveBarrier();

        Thread first = new Thread(
                () -> {
                    while (true) {
                        try {
                            barrier.tryMaster();
                            System.out.println("Thread A");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            barrier.doneMaster();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
        );
        Thread second = new Thread(
                () -> {
                    while (true) {
                        try {
                            barrier.trySlave();
                            System.out.println("Thread B");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            barrier.doneSlave();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
        );
        first.start();
        second.start();
        first.join();
        second.join();
    }
}
