package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PoohJMS {
    private final ForkJoinPool pool = ForkJoinPool.commonPool();
    private final int port = 3345;
    private final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    private final Map<String, Deque<String>> topics = new ConcurrentHashMap<>();
    private static AtomicBoolean flag = new AtomicBoolean(true);


    {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("server started");
            while (flag.get()) {
                try (SocketConnection connection = new SocketConnection(server)) {
                        System.out.println("client connected: " + connection.getAdders());
                        Runnable task = () -> {
                            String httpRequest = connection.readBlock();
                            System.out.println("REQUEST: \r\n" + httpRequest);
                            String response = getHttpResponse(httpRequest, connection.getAdders());
                            System.out.println("RESPONSE: " + response);
                            connection.writeLine(response);
                            System.out.println("response submit");
                        };
                        ForkJoinTask response = pool.submit(task);
                        pool.invoke(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHttpResponse(String httpRequest, String ip) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        if (HttpProcessor.isPostRequest(httpRequest)) {                             // POST
            if (HttpProcessor.isTopic(httpRequest)) {                                // POST /topic
                topics.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
                topics.get(args[0]).offer(args[1]); // push to tail
                System.out.println("OK");
                return HttpProcessor.postTopicRequest(args[0], args[1], ip);
            } else {                                                                 // POST /queue
                queues.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
                System.out.println("OK");
                queues.get(args[0]).offer(args[1]); // push to tail
                return HttpProcessor.postQueueRequest(args[0], args[1], ip);
            }
        } else {                                                                    // GET
            if (HttpProcessor.isTopic(httpRequest)) {                                // GET /topic
                String response = topics.getOrDefault(args[0], new LinkedList<>()).poll();  // remove from head
                System.out.println("OK");
                return HttpProcessor.postTopicRequest(args[0], response, ip);
            } else {                                                                 // GET /queue
                String response = queues.getOrDefault(args[0], new LinkedList<>()).poll(); // remove from head
                System.out.println("OK");
                return HttpProcessor.postQueueRequest(args[0], response, ip);
            }
        }
    }

//    private void observer() {
//        Thread t = new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("type \"stop\" to stop the server");
//            String in = scanner.nextLine();
//            while (!in.equals("stop")) {
//                in = scanner.nextLine();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("exit");
//            flag.set(false);
//            Thread.currentThread().interrupt();
//        });
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws InterruptedException {
        PoohJMS poohJMS = new PoohJMS();
    }

}