package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
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

    public void startServer() {
        AtomicReference<ServerSocket> ref;
        try (ServerSocket server = new ServerSocket(port)) {
            ref = new AtomicReference<>(server);
            new Thread(() -> {
                String line = "";
                while (!line.equals("stop")) {
                    System.out.println("type stop to terminate" + this.terminalMessage);
                    line = input.get();
                }
                System.out.println("Terminate " + Thread.currentThread().getName());
                try {
                    ref.get().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
            while (!ref.get().isClosed()) {
                 SocketConnection connection = new SocketConnection(ref.get());
                System.out.println("connected");
                    new Thread(() -> {
                        while (connection.isAlive()) {
                            readHttp(connection, responses).forEach(req -> processResponses.accept(req, connection));
                        }
                    }).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startClient(String name) {
        AtomicReference<String> line = new AtomicReference<>("");
        try (SocketConnection connection = new SocketConnection(url, port, name)) {
            new Thread(() -> {
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
            }).start();
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
