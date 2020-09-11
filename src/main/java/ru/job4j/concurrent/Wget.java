package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        for (int index = 0; index <= 100; index++) {
            try {
                System.out.print("\rLoading : " + index  + "%");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
