//package ru.job4j.pooh_jms;
//
//import java.util.Set;
//import java.util.concurrent.CopyOnWriteArraySet;
//
////public class Topic {
////    private String name;
////    private Set<SocketConnection> subscribers = new CopyOnWriteArraySet<>();
//
//    public Topic(String name) {
//        this.name = name;
//    }
//
//    public int postTopic(String newContent) {
//        subscribers.forEach(x -> x.writeLine(HttpProcessor.postTopicRequest(name, newContent, x.getAdders())));
//        return subscribers.size();
//    }
//
//
//    public void subscribe(SocketConnection subscriber, String httpRequest) {
//        if (subscribers.contains(subscriber)) {
//
//        } else {
//
//        }
//    }
//}
