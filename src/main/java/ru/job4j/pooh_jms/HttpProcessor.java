package ru.job4j.pooh_jms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

class HttpProcessor {
    private static final String LN = System.lineSeparator();
    private static final Object LOCK = new Object();


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

    static void processPostQueue(String httpRequest, Map<String, Deque<String>> queues, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        queues.computeIfAbsent(args[0], v -> new ConcurrentLinkedDeque<>());
        queues.get(args[0]).offer(args[1]); // push to tail
        String response = postQueueRequest(args[0], args[1], connection.getAdders());
        MyLogger.log(httpRequest, response);
        connection.writeLine(response);
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void processPostTopic(String httpRequest, Map<String, Topic> topics, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        topics.computeIfAbsent(args[0], v -> new Topic(args[0]));
        topics.get(args[0]).postTopic(args[1]);
        String response = postTopicRequest(args[0], args[1], connection.getAdders());
        //todo close fix
        MyLogger.log(httpRequest, response);
        connection.writeLine(response);
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void processGetTopic(String httpRequest, Map<String, Topic> topics, SocketConnection connection) {
        String[] args = HttpProcessor.parseJson(httpRequest);
        topics.computeIfAbsent(args[0], v -> new Topic(args[0]));
        if (!topics.get(args[0]).subscribe(connection, httpRequest)) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedList<String> emptyList() {
        LinkedList<String> result = new LinkedList<>();
        result.add("no data");
        return result;
    }
}
