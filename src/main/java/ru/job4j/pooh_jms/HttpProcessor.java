package ru.job4j.pooh_jms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpProcessor {
    private static final String LN = System.lineSeparator();


    public static String postQueueRequest(String queue, String text, String hostUrl) {
        return buildRequest(queue, text, false, true,hostUrl);
    }

    public static String postTopicRequest(String topic, String text, String hostUrl) {
        return buildRequest(topic, text, true,true, hostUrl);
    }

    public static String getQueueRequest(String queue, String hostUrl) {
        return buildRequest(queue, "", false, false,hostUrl);
    }

    public static String getTopicRequest(String topic, String hostUrl) {
        return buildRequest(topic, "", true,false, hostUrl);
    }

    private static String buildRequest(String queueTopic, String text, boolean isTopic, boolean postMode, String url) {
        JSONObject obj = new JSONObject();
        if (isTopic) {
            obj.put("topic", queueTopic);
        } else {
            obj.put("queue", queueTopic);
        }
        if (!text.isEmpty()) {
            obj.put("text", text);
        }
        String jsonString = obj.toJSONString();
        StringBuilder result = postMode ?
                new StringBuilder("POST ") :
                new StringBuilder("GET " );
        result.append(isTopic? "/topic" : "/queue");
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

    public static boolean isPostRequest(String httpRequest) {
        boolean result = httpRequest.startsWith("POST");
        if (!result) {
            if (!httpRequest.startsWith("GET")) {
                throw new RuntimeException("wrong http request!");
            }
        }
        return result;
    }

    public static boolean isTopic(String httpRequest) {
        String line = httpRequest.substring(0, httpRequest.indexOf(LN));
        boolean result = httpRequest.endsWith("topic");
        if (!result) {
            if (!httpRequest.startsWith("queue")) {
                throw new RuntimeException("wrong http request!");
            }
        }
        return result;
    }

    public static String[] parseJson(String httpRequest) {
        String json = getJson(httpRequest);
        System.out.println("JSON: " +json);
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

    public static void main(String[] args) {
        String httpRequest = "REQUEST: POST /queue\n" +
                "Accept: application/json\n" +
                "Content-Type: application/json\n" +
                "Host: 127.0.0.1\n" +
                "\n" +
                "\n" +
                "\n" +
                "REQUEST: Content-Length: 30\n" +
                "{\"text\":\"-39\",\"queue\":\"weather\"}";
        System.out.println(getJson(httpRequest));
    }
}
