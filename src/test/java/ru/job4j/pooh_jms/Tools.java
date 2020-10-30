package ru.job4j.pooh_jms;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.MyLogger.log;
import static ru.job4j.pooh_jms.MyLogger.warn;

class Tools {
    private static Random random = new Random();
    private static Supplier<String> getRandomCity;
    private static Supplier<String> getRandomTemperature;
    static Supplier<Topic> getRandomTopic;
    private static Predicate<String> isWeather = s -> s.matches("-?(\\d+)");
    private static Predicate<String> isCity;
    private static BiFunction<String, String, Boolean> isSubscribeMsg = (name, message) -> String.format("you have successfully subscribed on topic: \"%s\"", name).equals(message);
    private static BiFunction<String, String, Boolean> isUnsubscribeMsg = (name, message) -> String.format("you have successfully unsubscribed on topic: \"%s\"", name).equals(message);
    private static String url = "127.0.0.1";
    private static int port = 3345;

    static {
        List<String> cities = new ArrayList<>(List.of("Moscow",
                "London",
                "Cape Town",
                "Canberra",
                "Dakar",
                "Hanoi",
                "Helsinki",
                "Havana",
                "Kingston",
                "Kyiv",
                "Lisbon",
                "Novosibirsk",
                "Saint_Petersburg",
                "Leningrad",
                "Yekaterinburg",
                "Chelyabinsk"));
        getRandomCity = () -> cities.get(random.nextInt(cities.size() - 1));
        getRandomTemperature = () -> String.valueOf(random.nextInt(100) - 50);
        isCity = cities::contains;
        getRandomTopic = () -> random.nextInt(5) - 3 > 0 ? new Topic("weather", getRandomTemperature.get())
                : new Topic("city", getRandomCity.get());

    }

    static SocketConnection getConnection(String name) {
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

    static void postQueueWeather(SocketConnection connection) {
        postQueue(connection, "weather", getRandomTemperature);
    }

    static void postQueueCity(SocketConnection connection) {
        postQueue(connection, "city", getRandomCity);
    }

    static void getQueueWeather(SocketConnection connection) {
        getQueue(connection, "weather", isWeather);
    }

    static void getQueueCity(SocketConnection connection) {
        getQueue(connection, "city", isCity);
    }

    private static void postQueue(SocketConnection connection, String name, Supplier<String> text) {
        String request = postQueueRequest(name, text.get(), connection.getName());
        connection.writeLine(request);
        String response = HttpProcessor.removeDelimiter(connection.readBlock());
        log(connection, request, response);
        assertJson(request, response);
    }

    private static void getQueue(SocketConnection connection, String name, Predicate<String> predicate) {
        String request = getQueueRequest(name, connection.getName());
        connection.writeLine(request);
        String response = HttpProcessor.removeDelimiter(connection.readBlock());
        log(connection, request, response);
        String[] json = parseJson(response);
        Assert.assertEquals(json[0], name);
        Assert.assertTrue(predicate.test(json[1]) || json[1].equals("no data"));
    }

    static void assertJson(String response, boolean postRequest) {
        String[] json = HttpProcessor.parseJson(response);
        Assert.assertTrue("weather".equals(json[0]) || "city".equals(json[0]));
        if (postRequest) {
            try {
                Assert.assertTrue(
                        isCity.test(json[1])
                                || isWeather.test(json[1])
                                || isSubscribeMsg.apply(json[0], json[1])
                                || isUnsubscribeMsg.apply(json[0], json[1])
                );

            } catch (AssertionError e) {
                System.out.println(json[1]);
            }
        }
    }

    static void assertJson(String request, String response) {
        String[] requestJson = parseJson(request);
        String[] responseJson = parseJson(response);
        Assert.assertEquals(requestJson[0], responseJson[0]);
        Assert.assertEquals(requestJson[1], responseJson[1]);
    }
}
