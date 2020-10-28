package ru.job4j.pooh_jms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

class HttpProcessor {
    private static final String LN = System.lineSeparator();
    private static final Object LOCK = new Object();
    private static final AtomicInteger counter = new AtomicInteger(0);


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

    private static String firstLine(String request) {
        return request.split(LN)[0];
    }

    static boolean isCloseConnectionRequest(String httpRequest) {
        return firstLine(httpRequest).contains("/exit");
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
        String line = firstLine(httpRequest);
        boolean result = line.endsWith("topic");
        if (!result) {
            if (!line.endsWith("queue")) {
                System.out.println(httpRequest);
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
            //todo DEL
            System.out.println("<#####" + httpRequest + " ######>");
            e.printStackTrace();
        }
        return result;
    }

    static void processPostQueue(String httpRequest, Map<String, Deque<String>> queues, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        queues.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
        queues.get(args[0]).offer(args[1]); // push to tail
        String response = postQueueRequest(args[0], args[1], connection.getAdders());
        MyLogger.log(httpRequest, response);
        connection.writeLine(response);
    }

    static void processPostTopic(String httpRequest, Map<String, CopyOnWriteArraySet<SocketConnection>> topics, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        topics.computeIfAbsent(args[0], v -> new CopyOnWriteArraySet<>());
        int subs = topics.get(args[0]).size();
        topics.get(args[0]).forEach(x -> x.writeLine(HttpProcessor.postTopicRequest(args[0], args[1], x.getAdders())));
        MyLogger.log("\r\nThere are " + subs + " subscribers on " + args[0] + " topic");
        String response = postTopicRequest(args[0], args[1], connection.getAdders());
        MyLogger.log(httpRequest, response);
        connection.writeLine(response);
    }

    static void processGetQueue(String httpRequest, Map<String, Deque<String>> queues, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        String result = queues.getOrDefault(args[0], emptyList()).poll();// remove from head
        if (result == null) {
            result = "no data";
        }
        String response = postQueueRequest(args[0], result, connection.getAdders());
        MyLogger.log(httpRequest, response);
        connection.writeLine(response);
    }

    static void processGetTopic(String httpRequest, Map<String, CopyOnWriteArraySet<SocketConnection>> topics, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        topics.computeIfAbsent(args[0], v -> new CopyOnWriteArraySet<>());
        if (topics.get(args[0]).contains(connection)) {
            topics.get(args[0]).remove(connection);
            String response = HttpProcessor.postTopicRequest(args[0], String.format("you have successfully unsubscribed on topic: \"%s\"", args[0]), connection.getAdders());
            connection.writeLine(response);
            MyLogger.log(httpRequest, response);
        } else {
            topics.get(args[0]).add(connection);
            String response = HttpProcessor.postTopicRequest(args[0], String.format("you have successfully subscribed on topic: \"%s\"", args[0]), connection.getAdders());
            connection.writeLine(response);
            MyLogger.log(httpRequest, response);
        }
    }

    private static LinkedList<String> emptyList() {
        LinkedList<String> result = new LinkedList<>();
        result.add("no data");
        return result;
    }

    static List<String> splitResponse(String response) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String line : response.split(LN)) {
            if (line.contains("{") && line.contains("}")) {
                sb.append(line);
                result.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(line).append(LN);
            }
        }
        return result;
    }
}
