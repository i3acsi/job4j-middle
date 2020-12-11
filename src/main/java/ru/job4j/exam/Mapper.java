package ru.job4j.exam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.BiFunction;
import java.util.function.Function;

class Mapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static Function<String, Camera> mapCamera = (json) -> {
        json = "{ " + json + " }";
        Camera camera = null;
        try {
            camera = OBJECT_MAPPER.readValue(json, Camera.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return camera;
    };

    static Function<Camera, String> getCameraDetailJson = (camera) -> {
        StringBuilder resultJson = new StringBuilder("{\r\n");
        resultJson.append(camera.toString()).append("\r\n");
        String sourceDataUrl = camera.getSourceDataUrl();
        String tokenDataUrl = camera.getTokenDataUrl();
        String json = Mapper.readJson.apply(sourceDataUrl, tokenDataUrl);
        resultJson.append(json);
        resultJson.append("\r\n}");
        return resultJson.toString();
    };

    private static BiFunction<String, String, String> readJson = (address1, address2) -> {
        StringBuilder result = new StringBuilder();
        readJsonFromAddress(address1, result::append);
        result.append(",\r\n");
        readJsonFromAddress(address2, result::append);
        return result.toString();
    };

    static void readJsonFromAddress(String address, ConsumerInterrupted<String> jsonConsumer) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new URL(address)
                                .openStream()))) {
            String regxp = "[]\\[]";
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.matches(regxp)) {
                    if (line.contains("}")) {
                        try {
                            jsonConsumer.apply(json.toString().trim());
                        } catch (InterruptedException e) {
                            break;
                        }
                    } else if (line.equals("{")) {
                        json = new StringBuilder();
                    } else {
                        json.append(line).append("\r\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

@FunctionalInterface
interface ConsumerInterrupted<T> {
    void apply(T t) throws InterruptedException;
}
