package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Config.port;


public class JmsServer extends JmsBase {
    private static final BiConsumer<String, SocketConnection> httpProcessor = ServerUtils.httpProcessor;
    static final Map<String, Deque<String>> queues = new ConcurrentHashMap<>();
    static final Map<String, CopyOnWriteArraySet<SocketConnection>> topics = new ConcurrentHashMap<>();

    public JmsServer(String terminalMessage,
                     Predicate<String> checkLine,
                     BiConsumer<String, SocketConnection> messageProcessor,
                     Supplier<String> input) {
        super(terminalMessage, checkLine, messageProcessor, input);
    }

    public JmsServer(String terminalMessage,
                     Predicate<String> checkLine,
                     BiConsumer<String, SocketConnection> messageProcessor) {
        super(terminalMessage, checkLine, messageProcessor);
    }

    public static void startServer(Supplier<String> input) {
        JmsServer instance = new JmsServer(
                ".",
                s -> true,
                httpProcessor,
                input
        );
        instance.start();
    }

    public static void startServer() {
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
            Runnable console = () -> {
                String line = "";
                while (!line.equals("stop")) {
                    System.out.println("type stop to terminate server" + this.terminalMessage);
                    line = this.input.get();
                }
                System.out.println("Terminate server");
                try {
                    for (SocketConnection connection : connections) {
                        connection.close();
                    }
                    server.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            };
            executorService.submit(console);
            while (!server.isClosed()) {
                SocketConnection connection;
                try {
                    connection = new SocketConnection(server);
                    connections.add(connection);
                    System.out.println("connected");
                    Runnable task = () -> {
                        while (connection.isAlive()) {
                            readHttp(connection).forEach(req -> messageProcessor.accept(req, connection));
                        }
                        System.out.println("disconnected");
                        connections.remove(connection);
                    };
//                    executorService.execute(task);
                    new Thread(task).start();
                } catch (SocketClosedException e) {
                    server.close();
                    executorService.shutdown();
                    break;
                }
            }
            executorService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JmsServer.startServer();
    }
}

