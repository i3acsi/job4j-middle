package ru.job4j.exam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static ru.job4j.exam.Mapper.mapCamera;
import static ru.job4j.exam.Mapper.readJsonFromAddress;

public class Aggregator {
    private BlockingQueue<Camera> cameras;
    private BlockingQueue<String> cameraDetails;
    private ExecutorService executorService;
    private Runnable producer;
    private Runnable consumer;
    private Runnable releaseCameraDetail;
    private Consumer<String> printJson;
    private AtomicInteger counter = new AtomicInteger(0);
    private CountDownLatch latch = new CountDownLatch(1);
    private volatile boolean isDone = false;

    Aggregator(Consumer<String> printJson) {
        this.cameras = new ArrayBlockingQueue<>(20);
        this.cameraDetails = new ArrayBlockingQueue<>(20);
        this.executorService = Executors.newCachedThreadPool();
        init();
        this.printJson = printJson;
    }

    private void init() {
        this.producer = () -> {
            readJsonFromAddress("http://www.mocky.io/v2/5c51b9dd3400003252129fb5", (json) -> {
                cameras.put(mapCamera.apply(json));
                counter.incrementAndGet();
            });
            isDone = true;
        };

        this.consumer = () -> {
            do {
                try {
                    Camera camera = cameras.take();
                    executorService.execute(() -> {
                        String json = Mapper.getCameraDetailJson.apply(camera);
                        try {
                            cameraDetails.put(json);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                } catch (InterruptedException e) {
                    break;
                }
            } while (true);
        };

        this.releaseCameraDetail = () -> {
            printJson.accept("[");
            do {
                try {
                    String json = this.cameraDetails.take();
                    if (counter.decrementAndGet() == 0 && isDone) {
                        printJson.accept(json);
                        break;
                    } else {
                        json = json + ",";
                        printJson.accept(json);
                    }
                } catch (InterruptedException e) {
                    break;
                }
            } while (true);
            printJson.accept("]");
            latch.countDown();
        };
    }

    private void parse() {
        boolean terminated = false;
        try {
            executorService.execute(producer);
            executorService.execute(consumer);
            executorService.execute(releaseCameraDetail);
            latch.await();
            executorService.shutdown();
            terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!terminated) {
            executorService.shutdownNow();
        }
    }

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator(System.out::println);
        aggregator.parse();
    }
}

@Data
@NoArgsConstructor
@EqualsAndHashCode
class Camera {
    private String id;
    private String sourceDataUrl;
    private String tokenDataUrl;

    @Override
    public String toString() {
        return String.format("\"id\": %s,", id);
    }
}