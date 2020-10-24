package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.job4j.pooh_jms.MyLogger.log;
import static ru.job4j.pooh_jms.MyLogger.warn;


public class SocketConnectionTest {
    private static Random random = new Random();
    private String url = "127.0.0.1";
    private int port = 3345;
    private Iterator<String> citiesIterator;
    private Iterator<String> temperaturesIterator;
    private List<String> cities = new CopyOnWriteArrayList<>();

    {
        cities.addAll(List.of("Moscow", "London", "Cape Town", "Canberra", "Dakar", "Hanoi", "Helsinki", "Havana", "Kingston", "Kyiv", "Lisbon"));
        citiesIterator = cities.iterator();
        List<String> temperatures = new CopyOnWriteArrayList<>();
        IntStream.generate(() -> random.nextInt(100) - 50).limit(20).forEach(x -> temperatures.add(String.valueOf(x)));
        temperaturesIterator = temperatures.iterator();
    }

    @Test
    public void connectionTest() throws InterruptedException {
        List<Thread> postWeather = Stream.generate(() -> new Thread(getRunnable(
                true, true, "weather", temperaturesIterator.next(), x -> x.matches("-?(\\d+)"))))
                .limit(15).peek(Thread::start).collect(Collectors.toList());
        List<Thread> postCity = Stream.generate(() -> new Thread(getRunnable(
                true, true, "city", citiesIterator.next(), cities::contains)))
                .limit(3).peek(Thread::start).collect(Collectors.toList());
        List<Thread> getWeather = Stream.generate(() -> new Thread(getRunnable(
                false, true, "weather", "", x -> x.matches("-?(\\d+)"))))
                .limit(10).peek(Thread::start).collect(Collectors.toList());
        List<Thread> getCity = Stream.generate(() -> new Thread(getRunnable(
                false, true, "city", "", cities::contains)))
                .limit(10).peek(Thread::start).collect(Collectors.toList());

        for (Thread thread : postWeather) {
            thread.join();
        }
        for (Thread thread : postCity) {
            thread.join();
        }
        for (Thread thread : getWeather) {
            thread.join();
        }
        for (Thread thread : getCity) {
            thread.join();
        }

    }

    private Runnable getRunnable(boolean modePost, boolean modeQueue, String queueOrTopic, String text, Predicate<String> assertion) {
        return () -> {
            int counter = 10;
            while (counter > 0) {
                try (SocketConnection socketConnection = new SocketConnection(url, port)) {
                    log("Connected to " + url);
                    String httpRequest = modePost ?
                            (modeQueue ? HttpProcessor.postQueueRequest(queueOrTopic, text, url)
                                    : HttpProcessor.postTopicRequest(queueOrTopic, text, url)) :
                            (modeQueue ? HttpProcessor.getQueueRequest(queueOrTopic, url)
                                    : HttpProcessor.getTopicRequest(queueOrTopic, url));
                    socketConnection.writeLine(httpRequest);
                    String httpResponse = socketConnection.readBlock();
                    log(httpRequest, httpResponse);
                    String[] response = HttpProcessor.parseJson(httpResponse);
                    Assert.assertEquals(queueOrTopic, response[0]);
                    Assert.assertTrue(assertion.test(response[1]) || response[1].equals("no data"));
                    counter = -1;
                } catch (Exception e) {
                    warn("server is busy: " + Thread.currentThread().getName());
                    counter--;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    @Test
    public void postGetTopicTest() throws InterruptedException {
        List<Thread> postWeatherTopic = Stream.generate(() -> new Thread(getRunnable(
                true, false, "weather", temperaturesIterator.next(), x -> x.matches("-?(\\d+)"))))
                .limit(10).peek(Thread::start).collect(Collectors.toList());
        List<Thread> getWeatherTopic = Stream.generate(() -> new Thread(getRunnable(
                false, false, "weather", "",  x -> x.matches("-?(\\d+)"))))
                .limit(10).peek(Thread::start).collect(Collectors.toList());
        for (Thread thread : postWeatherTopic) {
            thread.join();
        }
        for (Thread thread : getWeatherTopic) {
            thread.join();
        }
    }

    @Test
    public void post() throws InterruptedException {
        List<Thread> postWeatherTopic = Stream.generate(() -> new Thread(getRunnable(
                true, false, "weather", temperaturesIterator.next(), x -> x.matches("-?(\\d+)"))))
                .limit(2).peek(Thread::start).collect(Collectors.toList());
//        List<Thread> getWeatherTopic = Stream.generate(() -> new Thread(getRunnable(
//                false, false, "weather", "",  x -> x.matches("-?(\\d+)"))))
//                .limit(10).peek(Thread::start).collect(Collectors.toList());
        for (Thread thread : postWeatherTopic) {
            thread.join();
        }
//        for (Thread thread : getWeatherTopic) {
//            thread.join();
//        }
    }



}
