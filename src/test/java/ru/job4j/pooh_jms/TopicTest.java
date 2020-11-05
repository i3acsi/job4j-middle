package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Tools.assertJson;
import static ru.job4j.pooh_jms.Tools.getRandomTopic;

public class TopicTest {
    private boolean inProcess = true;
    private static Random random = new Random();

    private final Runnable startServer = () -> {
        Iterator<String> iteratorServer = List.of("", "", "", "", "", "", "", "", "", "", "", "").iterator();
        Supplier<String> inputServer = () -> {
            if (iteratorServer.hasNext()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return iteratorServer.next();
            }
            inProcess = false;
            return "stop";
        };
        new PoohJMS(inputServer);
    };

    private final Runnable subscriberTask = () -> {
        Iterator<String> iteratorSub = List.of("weather", "city", "", "", "", "", "", "", "", "", "", "", "weather", "", "", "").iterator();
        Supplier<String> inputSub = () -> {
            String res = "";
            while (res.equals("")) {
                if (iteratorSub.hasNext()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res = iteratorSub.next();
                } else {
                    res = "stop";
                }
            }
            return res;
        };
        BiConsumer<String, SocketConnection> requestProcessor = (response, connection) -> isCorrectResponse(response);
        new Subscriber(inputSub, requestProcessor, false);
    };

    private final Runnable postTask = () -> {
        Supplier<String> inputPub = () -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (random.nextInt(100) < 95) {
                return getRandomTopic.get().toString();
            } else {
                return "stop";
            }
        };
        BiConsumer<String, SocketConnection> processRequest = (response, connection) -> isCorrectResponse(response);
        new Publisher(inputPub, processRequest, false);
    };

    @Test
    public void postGetTopicTest() throws InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        executor.submit((startServer));
//        executor.submit(subscriberTask);
//        Thread.sleep(100);
//        executor.submit(postTask);
        Thread server = new Thread(startServer);
        Thread sub = new Thread(subscriberTask);
        Thread pub = new Thread(postTask);

        server.start();

        sub.start();
        pub.start();


        while (inProcess) {
            Thread.sleep(1000);
        }
    }

    private static void isCorrectResponse(String response) {
        Assert.assertEquals("POST /topic", response.lines().findFirst().orElseThrow());
        assertJson(response, true);
    }
}
