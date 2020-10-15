package ru.job4j.pooh_jms;


import org.apache.http.client.methods.CloseableHttpResponse;

public class Publisher extends Client{


    protected Publisher(String url) {
        super(url);
    }

    @Override
    protected CloseableHttpResponse postQueue(String queue, String text) {
        return super.postQueue(queue, text);
    }

    @Override
    protected CloseableHttpResponse postTopic(String topic, String text) {
        return super.postTopic(topic, text);
    }
}
