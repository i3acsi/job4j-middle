package ru.job4j.pooh_jms;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

abstract class Client {
    private final String url;

    protected Client(String url) {
        this.url = url;
    }

    protected CloseableHttpResponse postQueue(String queue, String text) {
        return post(queue, text, true);
    }

    protected CloseableHttpResponse postTopic(String topic, String text) {
        return post(topic, text, false);
    }

    protected CloseableHttpResponse getQueue(String queue) {
        return get(queue, true);
    }

    protected CloseableHttpResponse getTopic(String topic) {
        return get(topic, false);
    }

    private CloseableHttpResponse post(String parameter, String text, boolean postMode) {
        HttpEntity query = postMode ?
                JsonParser.postQueue(parameter, text) :
                JsonParser.postTopic(parameter, text);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setEntity(query);
            return httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CloseableHttpResponse get(String parameter, boolean postMode) {
        HttpEntity query = postMode ?
                JsonParser.getQueue(parameter) :
                JsonParser.getTopic(parameter);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setEntity(query);
            return httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}