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
    private final ExecutorService executorService;
    private final Consumer<String> printJson;
    private volatile boolean producerIsDone = false;

    Aggregator(Consumer<String> printJson) {
        this.executorService = Executors.newCachedThreadPool();
        this.printJson = printJson;
    }


    private void parse() {
        BlockingQueue<Camera> cameras = new ArrayBlockingQueue<>(20);
        BlockingQueue<String> cameraDetails = new ArrayBlockingQueue<>(20);
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            executorService.execute(() -> {
                readJsonFromAddress("http://www.mocky.io/v2/5c51b9dd3400003252129fb5", (json) -> {
                    cameras.put(mapCamera.apply(json));
                    counter.incrementAndGet();
                });
                producerIsDone = true;
            });
            executorService.execute(() -> {
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
            });
            executorService.execute(() -> {
                printJson.accept("[");
                do {
                    try {
                        String json = cameraDetails.take();
                        if (counter.decrementAndGet() == 0 && producerIsDone) {
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
            });
            latch.await();
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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