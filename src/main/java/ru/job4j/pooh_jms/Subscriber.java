package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Subscriber {
    private String terminalMessage;
    private BiConsumer<String, SocketConnection> subscribe;

    public Subscriber(Supplier<String> input, BiConsumer<String, SocketConnection> processResponse, boolean queueMode) {
        String name = init(queueMode);
        new Jms(
                terminalMessage,
                s -> true,
                subscribe,
                input,
                processResponse
        ).startClient(name);
    }

    public Subscriber(boolean queueMode) {
        String name = init(queueMode);
        new Jms(
                terminalMessage,
                s -> true,
                subscribe,
                (s, c) -> {
                }
        ).startClient(name);
    }

    private String init(boolean queueMode) {
        String name = (queueMode) ? "queue_subscriber" : "topic_subscriber";
        this.terminalMessage = (queueMode) ? ", or name of queue to read from queue: \"queue_name\""
                : ", or name of topic to subscribe/unsubscribe : \"topic_name\"";
        BiFunction<String, String, String> post = (queueMode) ?
                (queue, hostUrl) -> HttpProcessor.
                        getQueueRequest(queue.toLowerCase().trim(), hostUrl)
                : (topic, hostUrl) -> HttpProcessor.
                getTopicRequest(topic.toLowerCase().trim(), hostUrl);
        subscribe = (queue, connection) ->
                connection.writeLine(post.apply(queue, connection.getName()));
        return name;
    }


    public static void main(String[] args) {
        new Subscriber(false);
    }
}
