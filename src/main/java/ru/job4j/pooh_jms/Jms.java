package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
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
        AtomicReference<ServerSocket> ref;
        try (ServerSocket server = new ServerSocket(port)) {
            ref = new AtomicReference<>(server);
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
                    ref.get().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(0);
                }
            };
            executorService.submit(console);
            while (!ref.get().isClosed()) {
                SocketConnection connection;
                try {
                    connection = new SocketConnection(ref.get());
                    connections.add(connection);
                    System.out.println("connected");
                    Runnable task = () -> {
                        while (connection.isAlive()) {
                            readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
                        }
                        connections.remove(connection);
                    };
                    executorService.submit(task);
                }catch (SocketClosedException e) {
                    System.out.println("2"); //todo de
                    ref.get().close();
                    executorService.shutdown();
                    break;
                }
            }
            System.out.println("3"); //todo del
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("4"); //todo del
    }

    public void startClient(String name) {
        AtomicReference<String> line = new AtomicReference<>("");
        try (SocketConnection connection = new SocketConnection(url, port, name)) {
            Runnable console =() -> {
                String s = "";
                while (true) {
                    System.out.println("type stop to terminate" + this.terminalMessage);
                    s = input.get();
                    if (s.equals("stop")) {
                        break;
                    } else {
                        line.set(s);
                    }
                }
                System.out.println("Terminate " + Thread.currentThread().getName());
                try {
                    connection.sendCloseRequest();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            executorService.submit(console);
            new Thread(() -> { //Thread?
                while (connection.isAlive()) {
                    readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
                }
            }).start();
            new Thread(() -> {
                while (connection.isAlive()) {
                    String tmp = line.getAndSet("");
                    if (correctLine.test(tmp)) {
                        messageProcessor.accept(tmp, connection);
                    }
                }
            }).start();
            while (connection.isAlive()){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void startServer() {
//        Set<SocketConnection> connections = new CopyOnWriteArraySet<>();
//        AtomicReference<ServerSocket> ref;
//        try (ServerSocket server = new ServerSocket(port)) {
//            ref = new AtomicReference<>(server);
//            new Thread(() -> {
//                String line = "";
//                while (!line.equals("stop")) {
//                    System.out.println("type stop to terminate" + this.terminalMessage);
//                    line = input.get();
//                }
//                System.out.println("Terminate " + Thread.currentThread().getName());
//                try {
//                    for (SocketConnection connection : connections) {
//                        System.out.println("close one"); //todo del
//                        connection.close();
//                    }
//                    ref.get().close();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }).start();
//            while (!ref.get().isClosed()) {
//                SocketConnection connection;
//                try {
//                    connection = new SocketConnection(ref.get());
//                    connections.add(connection);
//                    System.out.println("connected");
//                    Runnable task = () -> {
//                        while (connection.isAlive()) {
//                            readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
//                        }
//                        System.out.println("closed"); //todo del
//                        connections.remove(connection);
//                    };
//                    executorService.submit(task);
//                } catch (RuntimeException e) {
//                    MyLogger.warn("socket closed");
//                    ref.get().close();
//                    executorService.shutdown();
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void startClient(String name) {
////        AtomicReference<String> line = new AtomicReference<>("");
//        try (SocketConnection connection = new SocketConnection(url, port, name)) {
//            Runnable inputTask = () -> {
//                String s = "";
//                while (true) {
//                    System.out.println("type stop to terminate" + this.terminalMessage);
//                    s = input.get();
//                    if (s.equals("stop")) {
//                        break;
//                    } else {
//                        if (correctLine.test(s)) {
//                            messageProcessor.accept(s, connection);
//                        }
//                    }
//                }
//                MyLogger.warn("Terminate " + name);
//                try {
//                    connection.sendCloseRequest();
//                    connection.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            };
//            executorService.submit(inputTask);
//            Runnable httpTask = () -> { //Thread?
//                while (connection.isAlive()) {
//                    System.out.println(connection.isAlive());
//                    readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
//                }
//                System.out.println("cloded!"); //todo del
//            };
//            executorService.submit(httpTask);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @param terminalMessage
     * @param checkLine
     * @param messageProcessor
     * @param input
     * @param processResponses
     */
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
