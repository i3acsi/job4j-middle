package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.job4j.pooh_jms.JmsBase.*;
import static ru.job4j.pooh_jms.Tools.*;
import static ru.job4j.pooh_jms.TopicSubscriber.subscribe;
import static ru.job4j.pooh_jms.TopicSubscriber.terminalMessage;

public class TopicTest {
    private Set<String> topics;
    private Runnable subscribeTask;
    private Runnable postTask;
    private List<Topic> topicList;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Test
    public void postGetTopicTest() {
        executor.execute(PoohJMS::getAndStart);
        executor.execute(() -> {
            Iterator<String> iterator = List.of("weather", "city", "weather").iterator();
            TopicSubscriber subscriber = new TopicSubscriber(
                    terminalMessage,
                    s -> true,
                    (line, connection) -> subscribe(line.toLowerCase(), connection),
                    () -> {
                        // нуно условие для задержки
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return iterator.next();
                    },
                    (response, connection) -> {
                        isCorrectResponseOnSubscribeTopic(response);
                    };
            try (SocketConnection connection = new SocketConnection(url, port, "topic_subscriber")) {
                subscriber.start(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            );
        });
        executor.execute(postTask);
        while (!executor.isShutdown()) {
        }
    }

    {
        topicList = Stream.generate(() -> getRandomTopic.get()).limit(12).collect(Collectors.toList());
        topics = Set.of("weather", "city");

        postTask = () -> {
            SocketConnection connection = getConnection("publisher");
            Flow.Publisher pub = new Publisher(topicList, connection);
            pub.getResponses().forEach(response -> {
                String[] lines = response.split("\r\n");
                Assert.assertEquals("POST /topic", lines[0]);
                assertJson(response, true);
                executor.shutdown();
            });
        };
        subscribeTask = () -> {
            SocketConnection connection = getConnection("subscriber");
            Subscriber sub = new Subscriber(topics, connection);
            sub.getResponses().forEach(response -> {
                String firsLine = response.lines().findFirst().orElseThrow();
                if ("GET /topic".equals(firsLine)) {
                    assertJson(response, false);
                } else if ("POST /topic".equals(firsLine)) {
                    assertJson(response, true);
                } else {
                    throw new AssertionError("wrong response :\r\n" + response);
                }
            });
        };
    }

    private static void isCorrectResponseOnSubscribeTopic(String response) {
        Assert.assertEquals("POST /topic", response.lines().findFirst().orElseThrow());
        assertJson(response, true);
    }
}
