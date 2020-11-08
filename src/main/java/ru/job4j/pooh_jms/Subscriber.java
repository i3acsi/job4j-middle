package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Subscriber {

    public static void startSubscriber(Supplier<String> input, BiConsumer<String, SocketConnection> processResponse, boolean queueMode) {
        Object[] args = init(queueMode);
        String name = args[0].toString();
        new JmsClient(
                args[1].toString(),
                s -> true,
                (BiConsumer<String, SocketConnection>) args[2],
                input,
                processResponse
        ).start(name);
    }

    public static void startSubscriber(boolean queueMode) {
        Object[] args = init(queueMode);
        String name = args[0].toString();
        new JmsClient(
                args[1].toString(),
                s -> true,
                (BiConsumer<String, SocketConnection>) args[2]
        ).start(name);
    }

    private static Object[] init(boolean queueMode) {
        Object[] result = new Object[3];
        String name = (queueMode) ? "queue_subscriber" : "topic_subscriber";
        result[0] = name;
        result[1] = (queueMode) ? ", or name of queue to read from queue: \"queue_name\""
                : ", or name of topic to subscribe/unsubscribe : \"topic_name\"";
        BiFunction<String, String, String>
                post = (queueMode) ?
                (queue, hostUrl) -> HttpProcessor.
                        getQueueRequest(queue.trim(), hostUrl)
                : (topic, hostUrl) -> HttpProcessor.
                getTopicRequest(topic.trim(), hostUrl);
        BiConsumer<String, SocketConnection> subscribe = (queue, connection) ->
                connection.writeLine(post.apply(queue, connection.getName()));
        result[2] = subscribe;
        return result;
    }


    public static void main(String[] args) {
        Subscriber.startSubscriber(false);
    }
}
