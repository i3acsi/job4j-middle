package ru.job4j.pooh_jms;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Topic {
    private String name;
    private Set<SocketConnection> subscribers = new CopyOnWriteArraySet<>();

    public Topic(String name) {
        this.name = name;
    }

    public void postTopic(String newContent) {
        subscribers.forEach(x -> x.writeLine(HttpProcessor.postTopicRequest(name, newContent, x.getAdders())));
    }


    public boolean subscribe(SocketConnection subscriber, String httpRequest) {
        if (subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
            String response = HttpProcessor.postTopicRequest(name, "you have successfully unsubscribed", subscriber.getAdders());
            subscriber.writeLine(response);
            MyLogger.log(httpRequest, response);
            return false;
        } else {
            subscribers.add(subscriber);
            String response = HttpProcessor.postTopicRequest(name, String.format("you have successfully subscribed on topic: \"%s\"", name), subscriber.getAdders());
            subscriber.writeLine(response);
            MyLogger.log(httpRequest, response);
            return true;
        }
    }
}
