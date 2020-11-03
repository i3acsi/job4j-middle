//package ru.job4j.pooh_jms;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.Random;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.function.BiConsumer;
//import java.util.function.Supplier;
//
//import static ru.job4j.pooh_jms.Tools.assertJson;
//import static ru.job4j.pooh_jms.Tools.getRandomTopic;
//
//public class TopicTest {
//    private static Random random = new Random();
//    private static final AtomicInteger c1 = new AtomicInteger(0);
//    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    private final Runnable startServer = ()->{
//        AtomicInteger counter = new AtomicInteger(0);
//        Supplier<String> input = () -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return counter.getAndIncrement() >= 10 ? "stop" : "msg";
//        };
//        PoohJMS.getAndStart(input);
//    };
//    private final Runnable subscriberTask = () -> {
//        AtomicReference<String> message = new AtomicReference<>("weather");
//        Supplier<String> input = () -> {
//            while (message.get().equals("")) {
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return message.getAndSet("");
//        };
//        BiConsumer<String, SocketConnection> requestProcessor = (response, connection) -> isCorrectResponse(response);
//        TopicSubscriber.getAndStart(input, requestProcessor);
//    };
//
//    private Runnable postTask = () -> {
//        Supplier<String> input = () -> random.nextInt(100) < 90 ? getRandomTopic.get().toString() : "stop";
//        BiConsumer<String, SocketConnection> processRequest = (response, connection) -> isCorrectResponse(response);
//        TopicPublisher.getAndStart(input, processRequest);
//    };
//
//    @Test
//    public void postGetTopicTest() {
//
//        executor.execute((startServer));
//        executor.execute(subscriberTask);
////        executor.execute(postTask);
////        executor.execute(subscriberTask);
//        executor.execute(postTask);
//        while (!executor.isShutdown()) {
//        }
//    }
//
//    {
////        topicList = Stream.generate(() -> getRandomTopic.get()).limit(12).collect(Collectors.toList());
////        topics = Set.of("weather", "city");
//
////        postTask = () -> {
////            SocketConnection connection = getConnection("publisher");
////            Flow.Publisher pub = new Publisher(topicList, connection);
////            pub.getResponses().forEach(response -> {
////                String[] lines = response.split("\r\n");
////                Assert.assertEquals("POST /topic", lines[0]);
////                assertJson(response, true);
////                executor.shutdown();
////            });
////        };
////        subscribeTask = () -> {
////            SocketConnection connection = getConnection("subscriber");
////            Subscriber sub = new Subscriber(topics, connection);
////            sub.getResponses().forEach(response -> {
////                String firsLine = response.lines().findFirst().orElseThrow();
////                if ("GET /topic".equals(firsLine)) {
////                    assertJson(response, false);
////                } else if ("POST /topic".equals(firsLine)) {
////                    assertJson(response, true);
////                } else {
////                    throw new AssertionError("wrong response :\r\n" + response);
////                }
////            });
////        };
//    }
//
//    private static void isCorrectResponse(String response) {
//        Assert.assertEquals("POST /topic", response.lines().findFirst().orElseThrow());
//        assertJson(response, true);
//    }
//}
