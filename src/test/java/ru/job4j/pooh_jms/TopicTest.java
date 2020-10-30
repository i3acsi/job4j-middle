package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.job4j.pooh_jms.Tools.*;

public class TopicTest {
    private Set<String> topics;
    private Runnable subscribeTask;
    private Runnable postTask;
    private List<Topic> topicList;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Test
    public void postGetTopicTest() throws InterruptedException {
        executor.execute(() -> new PoohJMS(executor));
        executor.execute(subscribeTask);
        executor.execute(postTask);
        while (!executor.isShutdown()) {
        }
    }

    {
        topicList = Stream.generate(() -> getRandomTopic.get()).limit(12).collect(Collectors.toList());
        topics = Set.of("weather", "city");

        postTask = () -> {
            SocketConnection connection = getConnection("publisher");
            Publisher pub = new Publisher(topicList, connection);
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
}
