package ru.job4j.pooh_jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.job4j.pooh_jms.HttpProcessor.*;

public class PoohJMS {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final int port = 3345;
    private final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private final Map<String, Deque<String>> topics = new ConcurrentHashMap<>();
    private final Map<String, SocketConnection> connectionMap = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(PoohJMS.class);
    private static final String TAB = "\t\t\t\t\t\t\t\t\t";
    private static final String LN = System.lineSeparator();


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
            log.info("server started");
            while (!Thread.currentThread().isInterrupted()) {
                SocketConnection connection = new SocketConnection(server);
                Runnable task = () -> {
                    String httpRequest = connection.readBlock();
                    log.info("client connected: " + connection.getAdders());
                    String httpResponse = "";
                    if (isPostRequest(httpRequest)) {            // POST
                        if (isTopic(httpRequest)) {             //POST /topic
                            httpResponse = getHttpResponseOnPostTopic(httpRequest, queues, connection.getAdders());
                        } else {                                 //POST /queue
                            httpResponse = getHttpResponseOnPostQueue(httpRequest, queues, connection.getAdders());
                        }
                    } else {
                        if (isTopic(httpRequest)) {             //GET /topic

                        } else {                                 //GET /queue
                            httpResponse = getHttpResponseOnGetQueue(httpRequest, queues, connection.getAdders());
                        }
                    }
                    log.info("HTTP REQUEST:\r\n" + httpRequest + addTab("RESPONSE:\r\n" + httpResponse) + LN + LN + LN);
                    connection.writeLine(httpResponse);
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                executorService.submit(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String addTab(String string) {
        return string.lines().reduce("", (x, y) -> TAB + x + LN + TAB + y);
    }


}