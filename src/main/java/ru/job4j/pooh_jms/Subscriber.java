package ru.job4j.pooh_jms;

import org.apache.http.client.methods.CloseableHttpResponse;

public class Subscriber extends Client {
    protected Subscriber(String url) {
        super(url);
    }

    @Override
    protected CloseableHttpResponse getQueue(String queue) {
        return super.getQueue(queue);
    }

    @Override
    protected CloseableHttpResponse getTopic(String topic) {
        return super.getTopic(topic);
    }
}
