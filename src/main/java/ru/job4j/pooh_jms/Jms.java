package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

abstract class Jms extends JmsBase {
    private final String terminalMessage;
    private final Predicate<String> checkLine;
    private final Predicate<String> correctLine;
    private final BiConsumer<String, SocketConnection> messageProcessor;
    private final Supplier<String> input;
    private final BiConsumer<String, SocketConnection> processRequest;


    public Jms(String terminalMessage, Predicate<String> checkLine, BiConsumer<String, SocketConnection> messageProcessor, Supplier<String> input, BiConsumer<String, SocketConnection> processRequest) {
        this.terminalMessage = terminalMessage;
        this.checkLine = checkLine;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
        this.input = input;
        this.processRequest = processRequest;
    }

    final public void start(SocketConnection connection) {
        if (connection != null) {
            Thread daemon = new Thread(() -> {
                while (connection.isAlive()) {
                    readHttp(connection, responses).forEach(req -> processRequest.accept(req, connection));
                }
            });
            daemon.setDaemon(true);
            daemon.start();
            String line = "";
            while (!"stop".equals(line)) {
                System.out.println("type stop to terminate" + terminalMessage);
                if (correctLine.test(line)) {
                    messageProcessor.accept(line, connection);
                }
                line = input.get();
            }
        }
    }
}
