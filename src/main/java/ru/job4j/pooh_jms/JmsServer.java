package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Config.port;


public class JmsServer extends JmsBase {
    private static final BiConsumer<String, SocketConnection> httpProcessor = ServerUtils.httpProcessor;

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
                        connections.remove(connection);
                    };
                    executorService.submit(task);
                } catch (SocketClosedException e) {
                    server.close();
                    executorService.shutdown();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JmsServer.startServer();
    }
}

