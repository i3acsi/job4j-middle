package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Publisher {
    private String terminalMessage;
    private Predicate<String> checkLine = line -> line.split("/").length == 2;
    private BiConsumer<String, SocketConnection> doPost;

    public Publisher(Supplier<String> input, BiConsumer<String, SocketConnection> processResponse, boolean queueMode) {
        String name = init(queueMode);
        new Jms(
                terminalMessage,
                checkLine,
                doPost,
                input,
                processResponse
        ).startClient(name);
    }

    public Publisher(boolean queueMode) {
        String name = init(queueMode);
        new Jms(
                terminalMessage,
                checkLine,
                doPost,
                (s, c) -> {
                }
        ).startClient(name);
    }

    private String init(boolean queueMode) {
        String name = (queueMode) ? "queue_publisher" : "topic_publisher";
        this.terminalMessage = String.format(", or name_of_%s/text to post : name/text", queueMode ? "queue" : "topic");
        Function<String[], String> post = (queueMode) ?
                (parts) -> HttpProcessor.
                        postQueueRequest(parts[0].trim(), parts[1].trim(), parts[2])
                : (parts) -> HttpProcessor.
                postTopicRequest(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim(), parts[2]);
        doPost = (message, connection) -> {
            if (message.equals("stop")) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String[] parts = message.split("/");
                connection.writeLine(post.apply(new String[]{parts[0], parts[1], connection.getName()}));
            }
        };
        return name;
    }

    public static void main(String[] args) {
//        new Thread(()->new Publisher(false));
        new Publisher(false);
    }
}
