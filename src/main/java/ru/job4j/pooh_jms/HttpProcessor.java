package ru.job4j.pooh_jms;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class HttpProcessor {
    static final String LN = System.lineSeparator();
    static final String MSG_DELIMITER = "##@@";

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
        StringBuilder result = new StringBuilder();
        result.append(postMode ?
                "POST " :
                "GET ");
        result.append(isTopic ? "/topic" : "/queue");
        result.append(LN).append("Accept: application/json")
                .append(LN).append("Content-Type: application/json")
                .append(LN).append(String.format("Content-Length: %d", jsonString.length() - 2))
                .append(LN).append(String.format("Host: %s", url))
                .append(LN).append(LN)
                .append(jsonString);
        return result.toString();
    }

    private static String getJson(String httpRequest) {
        return httpRequest.substring(httpRequest.indexOf("{"), httpRequest.indexOf("}") + 1);
    }

    private static String firstLine(String request) {
        return request.split(LN)[0];
    }

    static boolean isCloseConnectionRequest(String httpRequest) {
        return firstLine(httpRequest).equals("POST /exit");
    }

    static boolean isPostRequest(String httpRequest) {
        boolean result = httpRequest.startsWith("POST");
        if (!result) {
            if (!httpRequest.startsWith("GET")) {
                System.out.println(httpRequest);
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
            System.out.println("JSON IS :" + json);
            e.printStackTrace();
        }
        return result;
    }

    static String closeRequest(String name) {
        return MSG_DELIMITER + "POST /exit\r\n" + "Host: " + name + MSG_DELIMITER;
    }

    static String addDelimiter(String message) {
        return MSG_DELIMITER + message + MSG_DELIMITER;
    }

    static String removeDelimiter(String message) {
        return message.replaceAll(MSG_DELIMITER, "");
    }

}
