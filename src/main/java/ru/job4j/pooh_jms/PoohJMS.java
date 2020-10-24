package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;

public class PoohJMS {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final int port = 3345;
    private final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private final Map<String,Topic> topics = new ConcurrentHashMap<>();
    {
        Thread serverProcessor = new Thread(this::run);
        serverProcessor.setDaemon(true);
        serverProcessor.start();
        interruptOnKey();
        executorService.shutdown();
        System.out.println("exit");
    }

    private void interruptOnKey() {
        Scanner scanner = new Scanner(System.in);
        String msg = ("type \"stop\" to stop the server");
        String in = "";
        while (!in.equals("stop")) {
            System.out.println(msg);
            in = scanner.nextLine();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new PoohJMS();
    }

    private void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            log("server started");
            while (!Thread.currentThread().isInterrupted()) {
                SocketConnection connection = new SocketConnection(server);
                Runnable task = () -> {
                    String httpRequest = connection.readBlock();
                    log("client connected: " + connection.getAdders());
                    processHttpRequest(httpRequest, connection);
                };
                executorService.submit(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processHttpRequest(String httpRequest, SocketConnection connection) {
        if (isPostRequest(httpRequest)) {            // POST
            if (isTopic(httpRequest)) {             //POST /topic
                processPostTopic(httpRequest, topics, connection);
            } else {                                 //POST /queue
                processPostQueue(httpRequest, queues, connection);
            }
        } else {
            if (isTopic(httpRequest)) {             //GET /topic
                processGetTopic(httpRequest, topics, connection);
            } else {                                 //GET /queue
                processGetQueue(httpRequest, queues, connection);
            }
        }
    }
}