package ru.job4j.pooh_jms;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;

import static ru.job4j.pooh_jms.HttpProcessor.*;
import static ru.job4j.pooh_jms.JmsServer.queues;
import static ru.job4j.pooh_jms.JmsServer.topics;
import static ru.job4j.pooh_jms.MyLogger.log;


class ServerUtils {
    static final BiConsumer<String, SocketConnection> httpProcessor = (httpRequest, connection) -> {
        if (isCloseConnectionRequest(httpRequest)) {
            try {
                connection.close();
                topics.forEach((k, v) -> v.remove(connection));
                log(connection.getName(), "client disconnected: " + (httpRequest.substring(httpRequest.lastIndexOf(":"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (isPostRequest(httpRequest)) {            // POST
                if (isTopic(httpRequest)) {             //POST /topic
                    processPostTopic(httpRequest, connection);
                } else {                                 //POST /queue
                    processPostQueue(httpRequest, connection);
                }
            } else {
                if (isTopic(httpRequest)) {             //GET /topic
                    processGetTopic(httpRequest, connection);
                } else {                                 //GET /queue
                    processGetQueue(httpRequest, connection);
                }
            }
        }
    };


    static void processPostQueue(String httpRequest, SocketConnection connection) {
        String[] json = HttpProcessor.parseJson(httpRequest);
        String queueName = json[0];
        String queueText = json[1];
        queues.computeIfAbsent(queueName, v -> new ConcurrentLinkedDeque<>());
        queues.get(queueName).offer(queueText);
        String response = postQueueRequest(queueName, queueText, connection.getName());
        MyLogger.log(connection.getName(), httpRequest, response);
        connection.writeLine(response);
    }

    static void processPostTopic(String httpRequest, SocketConnection connection) {
        String[] json = HttpProcessor.parseJson(httpRequest);
        String topicName = json[0];
        String topicText = json[1];
        topics.computeIfAbsent(topicName, v -> new CopyOnWriteArraySet<>());
        int subscribers = topics.get(topicName).size();
        topics.get(topicName)
                .forEach(sub ->
                        sub.writeLine(HttpProcessor.postTopicRequest(topicName, topicText, sub.getName())));
        MyLogger.log(connection.getName(), "There are " + subscribers + " subscribers on " + json[0] + " topic");
        String response = postTopicRequest(topicName, topicText, connection.getName());
        MyLogger.log(connection.getName(), httpRequest, response);
        connection.writeLine(response);
    }

    static void processGetQueue(String httpRequest, SocketConnection connection) {
        String queueName = HttpProcessor.parseJson(httpRequest)[0];
        String data = queues.getOrDefault(queueName, emptyList()).poll();// remove from head
        if (data == null) {
            data = "no data";
        }
        String response = postQueueRequest(queueName, data, connection.getName());
        MyLogger.log(connection.getName(), httpRequest, response);
        connection.writeLine(response);
    }

    static void processGetTopic(String httpRequest, SocketConnection connection) {
        String topicName = HttpProcessor.parseJson(httpRequest)[0];
        topics.computeIfAbsent(topicName, v -> new CopyOnWriteArraySet<>());
        if (topics.get(topicName).contains(connection)) { // do unsubscribe
            topics.get(topicName).remove(connection);
            String response = HttpProcessor.postTopicRequest(topicName, String.format("you have successfully unsubscribed on topic: \"%s\"", topicName), connection.getAdders());
            connection.writeLine(response);
            MyLogger.log(connection.getName(), httpRequest, response);
        } else { // do subscribe
            topics.get(topicName).add(connection);
            String response = HttpProcessor.postTopicRequest(topicName, String.format("you have successfully subscribed on topic: \"%s\"", topicName), connection.getAdders());
            connection.writeLine(response);
            MyLogger.log(connection.getName(), httpRequest, response);
        }
    }

    private static LinkedList<String> emptyList() {
        LinkedList<String> result = new LinkedList<>();
        result.add("no data");
        return result;
    }
}
