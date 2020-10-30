//package ru.job4j.pooh_jms;
//
//import org.junit.Test;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static ru.job4j.pooh_jms.Tools.*;
//
//
//public class QueueTest {
//    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    private Runnable postWeatherTask;
//    private Runnable getWeatherTask;
//    private Runnable postCityTask;
//    private Runnable getCityTask;
//
//
//    {
//        postWeatherTask = () -> {
//            List<String> responses = new CopyOnWriteArrayList<>();
//            SocketConnection connection =
//                    getConnection("postWeatherQueue");
//            if (connection != null) {
//                postQueueWeather(connection);
//                postQueueWeather(connection);
//                connection.sendCloseRequest();
//            }
//        };
//        getWeatherTask = () -> {
//            SocketConnection connection =
//                    getConnection("getWeatherQueue");
//            if (connection != null) {
//                getQueueWeather(connection);
//                getQueueWeather(connection);
//                getQueueWeather(connection);
//                connection.sendCloseRequest();
//                executor.shutdown();
//            }
//        };
//        postCityTask = () -> {
//            SocketConnection connection =
//                    getConnection("postCityQueue");
//            if (connection != null) {
//                postQueueCity(connection);
//                postQueueCity(connection);
//                connection.sendCloseRequest();
//            }
//        };
//        getCityTask = () -> {
//            SocketConnection connection =
//                    getConnection("getCityQueue");
//            if (connection != null) {
//                getQueueCity(connection);
//                getQueueCity(connection);
//                getQueueCity(connection);
//                connection.sendCloseRequest();
//            }
//        };
//    }
//
//    @Test
//    public void queueTest() throws InterruptedException {
//        Thread pooh = new Thread(()->new PoohJMS(executor));
//        pooh.start();
//
//        Thread pc1 = new Thread(postCityTask);
//        Thread pc2 = new Thread(postCityTask);
//pc1.start();
//pc2.start();
//pc1.join();
//pc2.join();
////        executor.execute(() -> new PoohJMS(executor));
//        executor.execute(postCityTask);
//        executor.execute(postCityTask);
//        executor.execute(getCityTask);
//        executor.execute(getCityTask);
//        executor.execute(postWeatherTask);
//        executor.execute(postWeatherTask);
//        executor.execute(getWeatherTask);
//        executor.execute(getWeatherTask);
////        Thread.sleep();
////        while (!executor.isShutdown()) {
//        pooh.join();
//
////        }
//
//    }
//
//    private void executeIt(Runnable task, int limit) {
//        for (int i = 0; i < limit; i++) {
//            executor.execute(task);
//        }
//    }
//
//}
//
