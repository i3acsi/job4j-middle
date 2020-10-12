package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService pool;

    public EmailNotification() {
        this.pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }

    public void emailTo(User user) {
        String sbj = String.format("Notification %s to email %s", user.getUserName(), user.getEmail());
        String body = String.format("Add a new event to %s", user.getUserName());
        pool.submit(() -> send(sbj, body, user.getEmail()));
    }

    public void send(String subject, String body, String email) {
        System.out.println("#############");
        System.out.println(subject);
        System.out.println(body);
        System.out.println(email);
        System.out.println("#############");
    }

    public void close() {
        this.pool.shutdown();
        while (!this.pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
