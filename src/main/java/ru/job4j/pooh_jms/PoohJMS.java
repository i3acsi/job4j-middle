package ru.job4j.pooh_jms;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;

public class PoohJMS {
    private static final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private static final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();
    private static final BiConsumer<String, SocketConnection> httpProcessor = (httpRequest, connection) -> {
        if (isCloseConnectionRequest(httpRequest)) {
            try {
                connection.close();
                topics.forEach((k, v) -> v.remove(connection));
                log(connection, "client disconnected: " + (httpRequest.substring(httpRequest.lastIndexOf(":"))));
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
    };
    public PoohJMS(Supplier<String> input) {
        Jms instance = new Jms(
                ".",
                s -> true,
                (s, c) -> {
                },
                input,
                httpProcessor
        );
        instance.startServer();
    }

    public PoohJMS() {
        Jms instance = new Jms(
                ".",
                s -> true,
                (s, c) -> {
                },
                httpProcessor
        );
        instance.startServer();
    }

    public static void main(String[] args) {
        new PoohJMS();
    }
}