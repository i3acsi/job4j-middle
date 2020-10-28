package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;

public class PoohJMS extends JmsCli {

    private final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();

    public PoohJMS(Executor executor) {
        try(ServerSocket socket = new ServerSocket(port)) {
            System.out.println("server started");
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("wait fo client");
                SocketConnection connection = new SocketConnection(socket);
                log("\r\nclient connected: " + connection.getAdders());
                executor.execute(()->{
                    while (connection.isAlive()) {
                        String httpRequest = "";
                        try {
                            httpRequest = connection.readBlock();
                        } catch (IOException e) {
                            break;
                        }
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
                String address = connection.getAdders();
                connection.close();
                log("client disconnected: " + address);
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
}