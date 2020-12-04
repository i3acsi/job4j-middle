package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Config.port;


public class JmsServer extends JmsBase {
    static final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    static final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    private JmsServer(String terminalMessage,
                      Predicate<String> checkLine,
                      BiConsumer<String, SocketConnection> messageProcessor,
                      Supplier<String> input) {
        super(terminalMessage, checkLine, messageProcessor, input);
    }

    private JmsServer(String terminalMessage,
                      Predicate<String> checkLine,
                      BiConsumer<String, SocketConnection> messageProcessor) {
        super(terminalMessage, checkLine, messageProcessor);
    }

    public static void startServer(Supplier<String> input) {
        BiConsumer<String, SocketConnection> httpProcessor = ServerUtils.httpProcessor;
        JmsServer instance = new JmsServer(
                ".",
                s -> true,
                httpProcessor,
                input
        );
        instance.start();
    }

    public static void startServer() {
        BiConsumer<String, SocketConnection> httpProcessor = ServerUtils.httpProcessor;
        JmsServer instance = new JmsServer(
                ".",
                s -> true,
                httpProcessor
        );
        instance.start();
    }


    public void start() {
        List<SocketConnection> connections = new CopyOnWriteArrayList<>();
        try (ServerSocket server = new ServerSocket(port)) {
            executorService.submit(() -> {
                String line = "";
                while (!line.equals("stop")) {
                    System.out.println("type stop to terminate server" + this.terminalMessage);
                    line = this.input.get();
                }
                System.out.println("Terminate server");
                terminate(connections, server);
            });
            while (!server.isClosed()) {
                SocketConnection connection;
                try {
                    connection = new SocketConnection(server);
                    connections.add(connection);
                    System.out.println("connected");
                    executorService.submit(() -> {
                        while (connection.isAlive()) {
                            readHttp(connection).forEach(req -> messageProcessor.accept(req, connection));
                        }
                        connections.remove(connection);
                    });
                } catch (SocketClosedException e) {
                    terminate(connections, server);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void terminate(List<SocketConnection> connections, ServerSocket server) {
        for (SocketConnection connection : connections) {
            try {
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        executorService.shutdown();
        boolean terminated = false;
        try {
            terminated = executorService.awaitTermination(11, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!terminated) {
            executorService.shutdownNow();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-sq")) {
                Subscriber.startSubscriber(true);
            } else if (args[0].equals("-st")) {
                Subscriber.startSubscriber(false);
            } else if (args[0].equals("-pq")) {
                Publisher.startPublisher(true);
            } else if (args[0].equals("-pt")) {
                Publisher.startPublisher(false);
            } else {
                System.out.println("-sq to start subscriber in queue mode\r\n" +
                        "-st to start subscriber in topic mode\r\n" +
                        "-pq to start publisher in queue mode\r\n" +
                        "-pt to start publisher in topic mode\r\n" +
                        "no args to start server");
            }
        } else
            JmsServer.startServer();
    }
}

