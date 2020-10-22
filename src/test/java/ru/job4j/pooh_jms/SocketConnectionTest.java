package ru.job4j.pooh_jms;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SocketConnectionTest {
    private Random random = new Random();
    private String url = "127.0.0.1";
    private int port = 3345;
    private List<String> cities;

    {
        cities = List.of("Moscow", "London", "Cape Town", "Canberra", "Dakar", "Hanoi", "Helsinki", "Havana", "Kingston", "Kyiv", "Lisbon");
    }
//@Test
//public void test() throws InterruptedException {
//        Runnable r = ()->{
//            try (SocketConnection connection = new SocketConnection(url, port)){
//                System.out.println("connected");
//                connection.writeLine("I'm here!");
////                System.out.println(connection.readBlock());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        Thread thread = new Thread(r);
//        thread.start();
//        thread.join();
//}

    @Test
    public void connectionTest() {
        Runnable postQueueWeather = () -> {
            boolean result = false;
            while (!result) {
                try (SocketConnection socketConnection = new SocketConnection(url, port);
                ) {
                    System.out.println("connected");
                    String t = (random.nextBoolean() ? "+" : "-") + random.nextInt(50);
                    socketConnection.requestPostQueue("weather", t);
                    System.out.println("weather" + t);
                    System.out.println("request done");
//                    String[] response = HttpProcessor.parseJson(socketConnection.readBlock());
//                    System.out.println("response: " + response);
//                    Assert.assertEquals("weather", response[0]);
//                    Assert.assertFalse(response[1].isEmpty());
                    result = true;
                } catch (Exception e) {
                    System.out.println("server is busy");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        };
        Runnable postQueueCities = () -> {
            boolean result = false;
            while (!result) {
                try (SocketConnection socketConnection = new SocketConnection(url, port);
                ) {
                    System.out.println("connected");
                    socketConnection.requestPostQueue("city", cities.get(random.nextInt(cities.size() - 1)));
                    String[] response = HttpProcessor.parseJson(socketConnection.readBlock());
                    Assert.assertEquals("city", response[0]);
                    Assert.assertTrue(cities.contains(response[1]));
                    result = true;
                } catch (Exception e) {
                    System.out.println("server is busy");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        };
        Runnable getQueueWeather = () -> {
            boolean result = false;
            while (!result) {
                try (SocketConnection socketConnection = new SocketConnection(url, port);
                ) {
                    System.out.println("connected");

                    socketConnection.requestGetQueue("weather");
                    String[] response = HttpProcessor.parseJson(socketConnection.readBlock());
                    Assert.assertEquals("weather", response[0]);
                    Assert.assertFalse(response[1].isEmpty());
                    result = true;
                } catch (Exception e) {
                    System.out.println("server is busy");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        Runnable getQueueCity = () -> {
            boolean result = false;
            while (!result) {
                try (SocketConnection socketConnection = new SocketConnection(url, port);
                ) {
                    System.out.println("connected");

                    socketConnection.requestGetQueue("city");
                    String[] response = HttpProcessor.parseJson(socketConnection.readBlock());
                    Assert.assertEquals("city", response[0]);
                    Assert.assertTrue(cities.contains(response[1]));
                    result = true;
                } catch (Exception e) {
                    System.out.println("server is busy");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        List<Thread> postWeather = Stream.generate(() -> new Thread(
                postQueueWeather
        )).limit(1).peek(Thread::start).collect(Collectors.toList());

//        List<Thread> postCity = Stream.generate(() -> new Thread(
//                postQueueCities
//        )).limit(10).peek(Thread::start).collect(Collectors.toList());
//
//        List<Thread> getWeather = Stream.generate(() -> new Thread(
//                getQueueCity
//        )).limit(10).peek(Thread::start).collect(Collectors.toList());
//
//        List<Thread> getCity = Stream.generate(() -> new Thread(
//                getQueueWeather
//        )).limit(10).peek(Thread::start).collect(Collectors.toList());
//
        for (Thread thread : postWeather) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        for (Thread thread : postCity) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Thread thread : getWeather) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Thread thread : getCity) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

}