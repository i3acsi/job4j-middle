package ru.job4j.pooh_jms;

import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TopicPublisher extends Jms {
    public TopicPublisher(String terminalMessage,
                          Predicate<String> checkLine,
                          BiConsumer<String, SocketConnection> messageProcessor,
                          Supplier<String> input,
                          BiConsumer<String, SocketConnection> processRequest) {
        super(terminalMessage, checkLine, messageProcessor, input, processRequest);
    }

    public static TopicPublisher getAndStart() {
        Scanner scanner = new Scanner(System.in);
        TopicPublisher instance = new TopicPublisher(", or name_of_topic/text to post : name/text",
                line -> line.split("/").length == 2,
                (message, connection) -> {
                    String[] parts = message.split("/");
                    connection.writeLine(HttpProcessor.postTopicRequest(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim(), connection.getName()));
                },
                scanner::nextLine,
                (request, connection) -> {
                }
        );
        try (SocketConnection connection = new SocketConnection(url, port, "topic_publisher")) {
            instance.start(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static void main(String[] args) {
        TopicPublisher.getAndStart();
    }
}




