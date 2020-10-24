package ru.job4j.pooh_jms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

class HttpProcessor {
    private static final String LN = System.lineSeparator();
    private static final Object LOCK = new Object();
    //    private static final Queue<SocketConnection> connections = new ConcurrentLinkedQueue<>();
    private static final Map<String, Set<SocketConnection>> topicConnections = new ConcurrentHashMap<>();


    static String postQueueRequest(String queue, String text, String hostUrl) {
        return buildRequest(queue, text, false, true, hostUrl);
    }

    static String postTopicRequest(String topic, String text, String hostUrl) {
        return buildRequest(topic, text, true, true, hostUrl);
    }

    static String getQueueRequest(String queue, String hostUrl) {
        return buildRequest(queue, "", false, false, hostUrl);
    }

    static String getTopicRequest(String topic, String hostUrl) {
        return buildRequest(topic, "", true, false, hostUrl);
    }

    private static String buildRequest(String queueTopic, String text, boolean isTopic, boolean postMode, String url) {
        JSONObject obj = new JSONObject();
        if (isTopic) {
            obj.put("topic", queueTopic);
        } else {
            obj.put("queue", queueTopic);
        }
        if (text != null && !text.isEmpty()) {
            obj.put("text", text);
        }
        String jsonString = obj.toJSONString();
        StringBuilder result = postMode ?
                new StringBuilder("POST ") :
                new StringBuilder("GET ");
        result.append(isTopic ? "/topic" : "/queue");
        result.append(LN).append("Accept: application/json")
                .append(LN).append("Content-Type: application/json")
                .append(LN).append(String.format("Content-Length: %d", jsonString.length() - 2))
                .append(LN).append(String.format("Host: %s", url))
                .append(LN).append(LN)
                .append(jsonString).append(LN);
        return result.toString();
    }

    private static String getJson(String httpRequest) {
        return httpRequest.substring(httpRequest.indexOf("{"));
    }

    static boolean isPostRequest(String httpRequest) {
        boolean result = httpRequest.startsWith("POST");
        if (!result) {
            if (!httpRequest.startsWith("GET")) {
                throw new RuntimeException("wrong http request!");
            }
        }
        return result;
    }

    static boolean isTopic(String httpRequest) {
        String line = httpRequest.substring(0, httpRequest.indexOf(LN));
        boolean result = line.endsWith("topic");
        if (!result) {
            if (!line.endsWith("queue")) {
                throw new RuntimeException("wrong http request!");
            }
        }
        return result;
    }

    static String[] parseJson(String httpRequest) {
        String json = getJson(httpRequest);
        String[] result = new String[2];
        JSONParser parser = new JSONParser();
        try {
            Object tmp;
            JSONObject obj = (JSONObject) parser.parse(json);
            result[0] = (tmp = obj.get("topic")) == null ? obj.get("queue").toString() : (String) tmp;
            result[1] = (tmp = obj.get("text")) == null ? null : (String) tmp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    static String getHttpResponseOnPostQueue(String httpRequest, Map<String, Deque<String>> queues, String address) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        queues.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
        queues.get(args[0]).offer(args[1]); // push to tail
        return postQueueRequest(args[0], args[1], address);
    }

    static String getHttpResponseOnPostTopic(String httpRequest, Map<String, Deque<String>> topics, String address) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        topics.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
        topics.get(args[0]).offer(args[1]); // push to tail
//        connection.notify();
        return postTopicRequest(args[0], args[1], address);
    }

    static String getHttpResponseOnGetQueue(String httpRequest, Map<String, Deque<String>> queues, String address) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        String response = queues.getOrDefault(args[0], emptyList()).poll();// remove from head
        if (response == null) {
            response = "no data";
        }
        return postQueueRequest(args[0], response, address);
    }

//    static String getHttpResponse(String httpRequest, SocketConnection connection, Map<String, Deque<String>> queues, Map<String, Deque<String>> topics, Map<String, SocketConnection> connectionMap) {
//        String[] args = HttpProcessor.parseJson(httpRequest);
//        if (isPostRequest(httpRequest)) {                             // POST
//            if (isTopic(httpRequest)) {                                // POST /topic
//                topics.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
//                topics.get(args[0]).offer(args[1]); // push to tail
//                connection.notify();
//                return postTopicRequest(args[0], args[1], connection.getAdders());
//            } else {                                                                 // POST /queue
//                queues.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
//                queues.get(args[0]).offer(args[1]); // push to tail
//                return postQueueRequest(args[0], args[1], connection.getAdders());
//            }
//        } else {                                                                    // GET
//            if (isTopic(httpRequest)) {                                // GET /topic
//                topicConnections.putIfAbsent(args[0], new CopyOnWriteArraySet<>());
//                Set<SocketConnection> connections = topicConnections.get(args[0]);
//                if (!connections.remove(connection)) { // если соединение уже было в наборе - то просто удаляем
//                    connections.add(connection);//
//                    topics.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
//                    while (connections.contains(connection)) {
//                        if (topics.get(args[0]).isEmpty()) {
//                            try {
//                                LOCK.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        while (!topics.get(args[0]).isEmpty()){
//                            String text = topics.get(args[0]).removeFirst();
//                            connections.forEach(c->{
//                                String postTopicRequest = postTopicRequest(args[0], text, connection.getAdders());
//                                c.writeLine(postTopicRequest);
//                            });
//                        }
//                    }
//                }
//
//
////                connectionMap.compute(connection.getAdders(), (k,v)->{
////                    if (v==null){
////                        connectionMap.put(connection.getAdders(), connection);
////                    } else {
////                        connectionMap.remove(connection.getAdders());
////                    }
////                    return v;
////                });
////                String response = topics.getOrDefault(args[0], emptyList()).poll();  // remove from head
////                return HttpProcessor.postTopicRequest(args[0], response, connection.getAdders());
//                return"";
//            } else {                                                                 // GET /queue
//                String response = queues.getOrDefault(args[0], emptyList()).poll(); // remove from head
//                return postQueueRequest(args[0], response, connection.getAdders());
//            }
//        }
//    }

    private static LinkedList<String> emptyList() {
        LinkedList<String> result = new LinkedList<>();
        result.add("no data");
        return result;
    }





}
