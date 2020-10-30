package ru.job4j.pooh_jms;

import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TopicSubscriber extends Jms {
    static String terminalMessage = ", or name of topic to subscribe/unsubscribe : \"topic_name\"";
    public TopicSubscriber(String terminalMessage,
                           Predicate<String> checkLine,
                           BiConsumer<String, SocketConnection> messageProcessor,
                           Supplier<String> input,
                           BiConsumer<String, SocketConnection> processRequest) {
        super(terminalMessage, checkLine, messageProcessor, input, processRequest);
    }

    public static TopicSubscriber getAndStart() {
        Scanner scanner = new Scanner(System.in);
        TopicSubscriber instance = new TopicSubscriber(
                terminalMessage,
                s -> true,
                (line, connection) -> subscribe(line.toLowerCase(), connection),
                scanner::nextLine,
                (request, connection) -> {
                }
        );
        try (SocketConnection connection = new SocketConnection(url, port, "topic_subscriber")) {
            instance.start(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;

    }

    static void subscribe(String topic, SocketConnection connection) {
        String request = HttpProcessor.getTopicRequest(topic, connection.getName());
        connection.writeLine(request);
    }

    public static void main(String[] args) {
        getAndStart();
    }
}

