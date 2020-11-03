package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class TopicSubscriber {
    private String terminalMessage = ", or name of topic to subscribe/unsubscribe : \"topic_name\"";
    private BiConsumer<String, SocketConnection> subscribe = (topic, connection) -> {
        String request = HttpProcessor.getTopicRequest(topic, connection.getName());
        connection.writeLine(request);
    };

    public TopicSubscriber(Supplier<String> input, BiConsumer<String, SocketConnection> processResponse) {
        new Jms(
                terminalMessage,
                s -> true,
                subscribe,
                input,
                processResponse
        ).startClient("topic_subscriber");
    }

    public TopicSubscriber() {
        new Jms(
                terminalMessage,
                s -> true,
                subscribe,
                (s, c) -> {
                }
        ).startClient("topic_subscriber");
    }


    public static void main(String[] args) {
        new TopicSubscriber();
    }
}

