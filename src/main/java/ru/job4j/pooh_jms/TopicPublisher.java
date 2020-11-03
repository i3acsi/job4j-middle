package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TopicPublisher {
    private String terminalMessage = ", or name_of_topic/text to post : name/text";
    private Predicate<String> checkLine = line -> line.split("/").length == 2;
    private BiConsumer<String, SocketConnection> doPost = (message, connection) -> {
        String[] parts = message.split("/");
        connection.writeLine(HttpProcessor.postTopicRequest(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim(), connection.getName()));
    };

    public TopicPublisher(Supplier<String> input, BiConsumer<String, SocketConnection> processResponse) {
        new Jms(
                terminalMessage,
                checkLine,
                doPost,
                input,
                processResponse
        ).startClient("topic_publisher");
    }

    public TopicPublisher() {
        new Jms(
                terminalMessage,
                checkLine,
                doPost,
                (s, c) -> {
                }
        ).startClient("topic_publisher");
    }


    public static void main(String[] args) {
        new TopicPublisher();
    }
}




