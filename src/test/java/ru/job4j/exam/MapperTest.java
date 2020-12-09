package ru.job4j.exam;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapperTest {

    @Test
    public void jsonToCameraTest() {
        String json =
                "        \"id\": 5,\n" +
                        "        \"sourceDataUrl\": \"http://www.mocky.io/v2/testSourceDataUrl\",\n" +
                        "        \"tokenDataUrl\": \"http://www.mocky.io/v2/testTokenDataUrl\"\n";
        Camera expected = new Camera();
        expected.setId("5");
        expected.setSourceDataUrl("http://www.mocky.io/v2/testSourceDataUrl");
        expected.setTokenDataUrl("http://www.mocky.io/v2/testTokenDataUrl");
        Camera result = Mapper.mapCamera.apply(json);
        Assert.assertEquals(result, expected);
    }

    @Test
    public void readJsonFromAddressTest() throws MalformedURLException {
        List<String> jsons = List.of(
                "\"id\": 1,\r\n" +
                        "\"sourceDataUrl\": \"src/main/resources/jsonDetails.txt\",\r\n" +
                        "\"tokenDataUrl\": \"src/main/resources/jsonDetails.txt\"",
                "\"id\": 2,\r\n" +
                        "\"sourceDataUrl\": \"src/main/resources/jsonDetails.txt\",\r\n" +
                        "\"tokenDataUrl\": \"src/main/resources/jsonDetails.txt\"",
                "\"id\": 3,\r\n" +
                        "\"sourceDataUrl\": \"src/main/resources/jsonDetails.txt\",\r\n" +
                        "\"tokenDataUrl\": \"src/main/resources/jsonDetails.txt\"",
                "\"id\": 4,\r\n" +
                        "\"sourceDataUrl\": \"src/main/resources/jsonDetails.txt\",\r\n" +
                        "\"tokenDataUrl\": \"src/main/resources/jsonDetails.txt\""
        );
        Iterator<String> expected = jsons.iterator();
        Mapper.readJsonFromAddress(Paths.get("src/main/resources/json.txt").toUri().toString(), (json) -> {
            Assert.assertEquals(json, expected.next());
        });
    }

    @Test
    public void getCameraDetailsJsonTest() {
        List<String> expectedJsons = List.of(
                "{\r\n" +
                        "\"id\": 1,\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\",\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\""
                        + "\r\n}",
                "{\r\n" +
                        "\"id\": 2,\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\",\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\""
                        + "\r\n}",
                "{\r\n" +
                        "\"id\": 3,\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\",\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\""
                        + "\r\n}",
                "{\r\n" +
                        "\"id\": 4,\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\",\r\n" +
                        "\"urlType\": \"TEST\",\r\n" +
                        "\"videoUrl\": \"rtsp://TEST\""
                        + "\r\n}");
        List<Camera> cameras = new ArrayList<>(4);
        List<String> resultJsons = new ArrayList<>(4);
        Mapper.readJsonFromAddress(Paths.get("src/main/resources/json.txt").toUri().toString(), (json) -> {
            cameras.add(Mapper.mapCamera.apply(json));
        });

        cameras.forEach(camera -> {
            String sourceDataUrl = Paths.get(camera.getSourceDataUrl()).toUri().toString();
            String tokenDataUrl = Paths.get(camera.getTokenDataUrl()).toUri().toString();
            camera.setSourceDataUrl(sourceDataUrl);
            camera.setTokenDataUrl(tokenDataUrl);
            resultJsons.add(Mapper.getCameraDetailJson.apply(camera));
        });
        Iterator<String> expected = expectedJsons.iterator();
        Iterator<String> result = resultJsons.iterator();
        while (expected.hasNext()) {
            Assert.assertEquals(expected.next(), result.next());
        }
    }
}
