package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

abstract class JmsClient extends JmsBase {
    private final String terminalMessage;
    private final Predicate<String> checkLine;
    private final Predicate<String> correctLine;
    private final Consumer<String> messageProcessor;

    public JmsClient(String terminalMessage, Predicate<String> checkLine, Consumer<String> messageProcessor) {
        this.terminalMessage = terminalMessage;
        this.checkLine = checkLine;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
    }

    protected void start(SocketConnection connection, InputStream inputStream) {
        if (connection != null) {
            Thread daemon = new Thread(() -> {
                while (connection.isAlive()) {
                    readHttp(connection, responses);
                }
            });
            daemon.setDaemon(true);
            daemon.start();
            Scanner scanner = new Scanner(inputStream);
            String line = "";
            while (!"stop".equals(line)) {
                System.out.println("type stop to terminate" + terminalMessage);
                if (correctLine.test(line)) {
//                    String[] parts = line.split("/");
//                    connection.writeLine(HttpProcessor.postTopicRequest(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim(), connection.getName()));
                }
                line = scanner.nextLine();
            }
        }
    }
}
