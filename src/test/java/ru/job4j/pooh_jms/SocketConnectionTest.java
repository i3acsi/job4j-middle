package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;
import static ru.job4j.pooh_jms.MyLogger.warn;


public class SocketConnectionTest {
    private static Random random = new Random();
    private String url = "127.0.0.1";
    private int port = 3345;
    private Iterator<String> citiesIterator;
    private Iterator<String> temperaturesIterator;
    private List<String> cities = new CopyOnWriteArrayList<>();
    private String closeRequest = new String("POST /exit");
    private Predicate<String> isWeather = s -> s.matches("-?(\\d+)");
    private Predicate<String> isCity = s -> cities.contains(s);
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    {
        cities.addAll(List.of("Moscow", "London", "Cape Town", "Canberra", "Dakar", "Hanoi", "Helsinki", "Havana", "Kingston", "Kyiv", "Lisbon"));
        citiesIterator = cities.iterator();
        List<String> temperatures = new CopyOnWriteArrayList<>();
        IntStream.generate(() -> random.nextInt(100) - 50).limit(20).forEach(x -> temperatures.add(String.valueOf(x)));
        temperaturesIterator = temperatures.iterator();
    }

    private SocketConnection getConnection() {
        int attempt = 10;
        SocketConnection connection = null;
        while (attempt > 0) {
            try {
                attempt--;
                connection = new SocketConnection(url, port);
                break;
            } catch (RuntimeException e) {
                warn("Server is busy");
            }
        }
        return connection;
    }

    private void postQueueWeather(SocketConnection connection) {
        postQueue(connection, "weather", temperaturesIterator);
    }

    private void postQueueCity(SocketConnection connection) {
        postQueue(connection, "city", citiesIterator);
    }

    private void postQueue(SocketConnection connection, String name, Iterator<String> iterator) {
        String request = postQueueRequest(name, iterator.next(), url);
        connection.writeLine(request);
        String response = connection.readBlockChecked();
        log(request, response);
        assertJson(request, response);
    }

    private void getQueueWeather(SocketConnection connection) {
        getQueue(connection, "weather", isWeather);
    }

    private void getQueueCity(SocketConnection connection) {
        getQueue(connection, "city", isCity);
    }

    private void getQueue(SocketConnection connection, String name, Predicate<String> predicate) {
        String request = getQueueRequest(name, url);
        connection.writeLine(request);
        String response = connection.readBlockChecked();
        log(request, response);
        String[] json = parseJson(response);
        Assert.assertEquals(json[0], name);
        Assert.assertTrue(predicate.test(json[1]) || json[1].equals("no data"));
    }

    private void closeConnection(SocketConnection connection) {
        connection.writeLine(closeRequest);
    }

    @Test
    public void queueTest() throws InterruptedException {
        new Thread(() -> {
            new PoohJMS(executor);
        }).start();
        List<Thread> postWeatherQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection();
                    if (connection != null) {
                        postQueueWeather(connection);
                        postQueueWeather(connection);
                        closeConnection(connection);
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());
        List<Thread> getWeatherQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection();
                    if (connection != null) {
                        getQueueWeather(connection);
                        getQueueWeather(connection);
                        getQueueWeather(connection);
                        closeConnection(connection);
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());

        List<Thread> postCityQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection();
                    if (connection != null) {
                        postQueueCity(connection);
                        postQueueCity(connection);
                        closeConnection(connection);
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());
        List<Thread> getCityQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection();
                    if (connection != null) {
                        getQueueCity(connection);
                        getQueueCity(connection);
                        getQueueCity(connection);
                        closeConnection(connection);
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());


        for (Thread thread : postWeatherQueueThread) {
            thread.start();
            thread.join();
        }
        for (Thread thread : getWeatherQueueThread) {
            thread.start();
            thread.join();
        }
        for (Thread thread : postCityQueueThread) {
            thread.start();
            thread.join();
        }
        for (Thread thread : getCityQueueThread) {
            thread.start();
            thread.join();
        }

    }

    private void subscribe(SocketConnection connection, String topic) {
        String request = HttpProcessor.getTopicRequest(topic, url);
        connection.writeLine(request);
        String response = connection.readBlockChecked();
        System.out.println(request + "%%%" + response);
    }

    private void subscribeWeather(SocketConnection connection) {
        subscribe(connection, "weather");
    }

    private void subscribeCity(SocketConnection connection) {
        subscribe(connection, "city");
    }

    private void postTopic(SocketConnection connection, String name, Iterator<String> iterator) {
        synchronized (connection) {
            String request = HttpProcessor.postTopicRequest(name, iterator.next(), url);
            connection.writeLine(request);
            String response = connection.readBlockChecked();
//        log(request, response);
            assertJson(request, response);
        }
    }

    private void postTopicWeather(SocketConnection connection) {
        postTopic(connection, "weather", temperaturesIterator);
    }

    private void postTopicCity(SocketConnection connection) {
        postTopic(connection, "city", citiesIterator);
    }

    private void readResponse(SocketConnection connection) {
        String response = connection.readBlockChecked();
        log("Subscriber receive response: <response>\r\n", response + "</response>");
        List<String> responses = HttpProcessor.splitResponse(response);
        responses.forEach(res -> {
            String[] json = parseJson(res);
            System.out.println(" !!! " + json[0] + "  !!!!!  " + json[1]);
            Assert.assertTrue(json[0].equals("weather") || json[0].equals("city"));
            try {
                Assert.assertTrue(isCity.test(json[1]) || isWeather.test(json[1]));

            } catch (AssertionError e) {
                System.out.println("ERROR!! " + json[1]);
            }
        });
    }

    @Test
    public void postGetTopicTest() throws InterruptedException {
        Thread main = new Thread(() -> {
            new PoohJMS(executor);
        });
        main.start();
        Set<String> topics = Set.of("weather", "city");
        List<Topic> topicList = List.of(
                new Topic("weather", temperaturesIterator.next()),
                new Topic("weather", temperaturesIterator.next()),
                new Topic("city", citiesIterator.next()),
                new Topic("city", citiesIterator.next())

        );

        Subscriber subscriber = new Subscriber(topics);
        Publisher publisher = new Publisher(topicList);

        List<Thread> subscribeTopic = Stream.generate(() -> new Thread(
                subscriber
        ))
                .limit(1).peek(Thread::start).collect(Collectors.toList());

        List<Thread> postTopic = Stream.generate(() -> new Thread(
                publisher
        ))
                .limit(2).collect(Collectors.toList());
        Thread.sleep(1000);
        for (Thread thread : postTopic) {
            thread.start();
        }
//        for (Thread thread : subscribeTopic) {
//            thread.join();
//        }
        System.out.println("SUB RESPONSES:\r\n");
        subscriber.getResponses().forEach(System.out::println);
        System.out.println("\r\n\r\n\r\nPUB RESPONSES:\r\n");
        publisher.getResponses().forEach(System.out::println);

//        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        service.execute(subscriber);
//        Publisher publisher = new Publisher(List.of(
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("city", citiesIterator.next()),
//                new Topic("city", citiesIterator.next()),
//                new Topic("city", citiesIterator.next())
//        ));
//        Thread pub = new Thread(publisher);
//        pub.start();
//        subscriber.getResponses().forEach(System.out::println);
//        executor.submit(new PoohJMS());
//        executor.submit(new Subscriber(Set.of("weather", "city")));
//        executor.submit(new Publisher(List.of(
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("weather", temperaturesIterator.next()),
//                new Topic("city", citiesIterator.next()),
//                new Topic("city", citiesIterator.next()),
//                new Topic("city", citiesIterator.next())
//        )));


    }

    private void assertJson(String request, String response) {
        String[] requestJson = parseJson(request);
        String[] responseJson = parseJson(response);
        Assert.assertEquals(requestJson[0], responseJson[0]);
        Assert.assertEquals(requestJson[1], responseJson[1]);

    }
}
