package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;

public class PoohJMS extends JmsCli {
    private final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();

    public PoohJMS(Executor executor) {
        executor.execute(interruptServer);
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                SocketConnection connection = new SocketConnection(server);
                executor.execute(() -> {
                    while (connection.isAlive()) {
                        String httpRequest = connection.readBlockChecked();
                        processHttpRequest(httpRequest, connection);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void processHttpRequest(String httpRequest, SocketConnection connection) {
        if (isCloseConnectionRequest(httpRequest)) {
            try {
                connection.close();
                topics.forEach((k,v)->{
                    v.remove(connection);
                });
                log("client disconnected: " + httpRequest.split("\r\n")[1].toLowerCase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
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

    public static void main(String[] args) {
        new PoohJMS(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    private Runnable interruptServer = () -> {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!"stop".equals(line)) {
            System.out.println("type stop to terminate the server");
            line = scanner.nextLine();
        }
        System.exit(0);
    };
}