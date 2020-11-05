package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Jms extends JmsBase {
    private final String terminalMessage;
    private final Supplier<String> input;
    private final Predicate<String> correctLine;
    private final BiConsumer<String, SocketConnection> messageProcessor;
    private final BiConsumer<String, SocketConnection> processResponses;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());



    public void startServer() {
        List<SocketConnection> connections = new CopyOnWriteArrayList<>();
        try (ServerSocket server = new ServerSocket(port)) {
            Runnable console = () -> {
                String line = "";
                while (!line.equals("stop")) {
                    System.out.println("type stop to terminate server" + this.terminalMessage);
                    line = input.get();
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
                            readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
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

    public void startClient(String name) {
        try (SocketConnection connection = new SocketConnection(url, port, name)) {
            Runnable console = () -> {
                String tmp;
                while (connection.isAlive()) {
                    System.out.println("type stop to terminate" + this.terminalMessage);
                    tmp = input.get();
                    if (tmp.equals("stop")) {
                        break;
                    } else {
                        if (correctLine.test(tmp)) {
                            messageProcessor.accept(tmp, connection);
                        }
                    }
                }
                System.out.println("Terminate " + name);
                try {
                    connection.sendCloseRequest();
                    connection.close();
                } catch (Exception e) {
                    throw new SocketClosedException(e.getMessage());
                }
            };
            executorService.submit(console);
            Runnable task = () -> { //Thread?
                while (connection.isAlive()) {
                    readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
                }
            };
            executorService.submit(task);
            while (connection.isAlive()) {
                if (!connection.checkConnection()) {
                    executorService.shutdown();
                    break;
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jms(String terminalMessage,
               Predicate<String> checkLine,
               BiConsumer<String, SocketConnection> messageProcessor,
               Supplier<String> input,
               BiConsumer<String, SocketConnection> processResponses) {
        this.terminalMessage = terminalMessage;
        this.input = input;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
        this.processResponses = processResponses;
    }

    public Jms(String terminalMessage,
               Predicate<String> checkLine,
               BiConsumer<String, SocketConnection> messageProcessor,
               BiConsumer<String, SocketConnection> processResponses) {
        this.terminalMessage = terminalMessage;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
        this.input = () -> { //default
            Scanner scanner = new Scanner(System.in);
            while (!scanner.hasNext()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return scanner.nextLine();
        };
        this.processResponses = processResponses;
    }
}
