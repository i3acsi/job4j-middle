package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
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
    private Predicate<String> isWeather = s -> s.matches("-?(\\d+)");
    private Predicate<String> isCity = s -> cities.contains(s);
    {
        cities.addAll(List.of("Moscow", "London", "Cape Town", "Canberra", "Dakar", "Hanoi", "Helsinki", "Havana", "Kingston", "Kyiv", "Lisbon", "Novosibirsk", "Saint_Petersburg", "Leningrad", "Yekaterinburg", "Chelyabinsk"));
        citiesIterator = cities.iterator();
        List<String> temperatures = new CopyOnWriteArrayList<>();
        IntStream.generate(() -> random.nextInt(100) - 50).limit(20).forEach(x -> temperatures.add(String.valueOf(x)));
        temperaturesIterator = temperatures.iterator();
    }

    @Test
    public void queueTest() throws InterruptedException {
        AtomicInteger postWeatherCounter = new AtomicInteger(0);
        List<Thread> postWeatherQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection("postWeatherQueue " + postWeatherCounter.getAndIncrement());
                    if (connection != null) {
                        postQueueWeather(connection);
                        postQueueWeather(connection);
                        connection.sendCloseRequest();
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());
        AtomicInteger getWeatherCounter = new AtomicInteger(0);
        List<Thread> getWeatherQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection("getWeatherQueue " + getWeatherCounter.getAndIncrement());
                    if (connection != null) {
                        getQueueWeather(connection);
                        getQueueWeather(connection);
                        getQueueWeather(connection);
                        connection.sendCloseRequest();
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());

        AtomicInteger postCityCounter = new AtomicInteger(0);
        List<Thread> postCityQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection("postCityQueue " + postCityCounter.getAndIncrement());
                    if (connection != null) {
                        postQueueCity(connection);
                        postQueueCity(connection);
                        connection.sendCloseRequest();
                    }
                }
        ))
                .limit(2).collect(Collectors.toList());

        AtomicInteger getCityCounter = new AtomicInteger(0);
        List<Thread> getCityQueueThread = Stream.generate(() -> new Thread(
                () -> {
                    SocketConnection connection = getConnection("getCityQueue " + getCityCounter.getAndIncrement());
                    if (connection != null) {
                        getQueueCity(connection);
                        getQueueCity(connection);
                        getQueueCity(connection);
                        connection.sendCloseRequest();
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


    private SocketConnection getConnection(String name) {
        int attempt = 10;
        SocketConnection connection = null;
        while (attempt > 0) {
            try {
                attempt--;
                connection = new SocketConnection(url, port, name);
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

    @Test
    public void postGetTopicTest() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Thread pooh = new Thread(() -> {
            new PoohJMS(executor);
        });
        pooh.start();

        Set<String> topics = Set.of("weather", "city");
        Runnable subscribeTask = () -> {
            SocketConnection connection = new SocketConnection("127.0.0.1", 3345, "subscriber");
            Subscriber sub = new Subscriber(topics, connection);
            System.out.println("subscriber is closed");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("SUB RESPONSES:\r\n");
            sub.getResponses().forEach(response -> {
                String firsLine = response.split("\r\n")[0];
                if ("GET /topic".equals(firsLine)) {
                    assertJson(response, false);
                } else if ("POST /topic".equals(firsLine)) {
                    assertJson(response, true);
                } else {
                    throw new AssertionError("wrong response :\r\n" + response);
                }
            });
        };
        Thread t = new Thread(subscribeTask);
        t.start();

        List<Topic> topicList = List.of(
                new Topic("weather", "34"),
                new Topic("weather", "-12"),
                new Topic("city", "Moscow"),
                new Topic("city", "Novosibirsk"),
                new Topic("weather", "3"),
                new Topic("weather", "-1"),
                new Topic("city", "Saint_Petersburg"),
                new Topic("city", "Leningrad"),
                new Topic("weather", "-24"),
                new Topic("weather", "21"),
                new Topic("city", "Yekaterinburg"),
                new Topic("city", "Chelyabinsk")

        );
        Runnable postTask = () -> {
            SocketConnection connection = new SocketConnection("127.0.0.1", 3345, "publisher");
            Publisher pub = new Publisher(topicList, connection);
            System.out.println("publisher is closed");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\r\n\r\n\r\nPUB RESPONSES:\r\n");
            pub.getResponses().forEach(response -> {
                String[] lines = response.split("\r\n");
                Assert.assertEquals("POST /topic", lines[0]);
                assertJson(response, true);
            });
        };
        Thread t2 = new Thread(postTask);
        t2.start();
        t2.join();
    }

    private void assertJson(String request, String response) {
        String[] requestJson = parseJson(request);
        String[] responseJson = parseJson(response);
        Assert.assertEquals(requestJson[0], responseJson[0]);
        Assert.assertEquals(requestJson[1], responseJson[1]);

    }

    private void assertJson(String response, boolean postRequest) {
        String[] json = HttpProcessor.parseJson(response);
        Assert.assertTrue("weather".equals(json[0]) || "city".equals(json[0]));
        if (postRequest) {
                Assert.assertTrue(isCity.test(json[1]) || isWeather.test(json[1]));
        }
    }
}
