package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;

public class PoohJMS extends Jms {
    private static final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private static final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();

    PoohJMS(String terminalMessage,
            Predicate<String> checkLine,
            BiConsumer<String, SocketConnection> messageProcessor,
            Supplier<String> input,
            BiConsumer<String, SocketConnection> processRequest) {
        super(terminalMessage, checkLine, messageProcessor, input, processRequest);
    }


    protected static PoohJMS getAndStart() {
        Scanner scanner = new Scanner(System.in);
        PoohJMS instance = new PoohJMS(".", s -> true, (s, c) -> {},
                scanner::nextLine, PoohJMS::processHttpRequest);
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                SocketConnection connection = new SocketConnection(server);
                executorService.execute(() ->
                    instance.start(connection)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private static void processHttpRequest(String httpRequest, SocketConnection connection) {
        if (isCloseConnectionRequest(httpRequest)) {
            try {
                connection.close();
                topics.forEach((k, v) -> v.remove(connection));
                log(connection, "client disconnected: " + httpRequest.split("\r\n")[1].toLowerCase()); //todo!!! del?
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
        PoohJMS.getAndStart();
    }

}