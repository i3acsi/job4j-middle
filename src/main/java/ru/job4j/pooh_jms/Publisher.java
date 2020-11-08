package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Publisher {
    private static Predicate<String> checkLine = line -> line.split("/").length == 2;

    public static void startPublisher(Supplier<String> input,
                                      BiConsumer<String, SocketConnection> processResponse,
                                      boolean queueMode) {
        Object[] args = init(queueMode);
        String name = args[0].toString();
        new JmsClient(
                args[1].toString(),
                checkLine,
                (BiConsumer<String, SocketConnection>) args[2],
                input,
                processResponse
        ).start(name);
    }

    public static void startPublisher(boolean queueMode) {
        Object[] args = init(queueMode);
        String name = args[0].toString();
        new JmsClient(
                args[1].toString(),
                checkLine,
                (BiConsumer<String, SocketConnection>) args[2]
        ).start(name);
    }

    private static Object[] init(boolean queueMode) {
        Object[] result = new Object[3];
        String name = (queueMode) ? "queue_publisher" : "topic_publisher";
        result[0] = name;
        result[1] = String.format(", or name_of_%s/text to post : name/text", queueMode ? "queue" : "topic");
        Function<String[], String> buildRequest = (queueMode) ?
                (args) -> HttpProcessor.
                        postQueueRequest(args[0], args[1], args[2])
                : (args) -> HttpProcessor.
                postTopicRequest(args[0], args[1], args[2]);
        BiConsumer<String, SocketConnection> doPost = (message, connection) -> {
            String[] parts = message.split("/");
            connection.writeLine(buildRequest.apply(new String[]{parts[0], parts[1], connection.getName()}));
        };
        result[2] = doPost;
        return result;
    }

    public static void main(String[] args) {
        Publisher.startPublisher(false);
    }
}
