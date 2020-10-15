package ru.job4j.pooh_jms;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;

public class JsonParser {

    public static HttpEntity postQueue(String queue, String text){
        JSONObject obj = new JSONObject();
        obj.put("queue", queue);
        obj.put("text", text);
        String query = obj.toJSONString();
        return  new StringEntity(query, ContentType.APPLICATION_JSON);
    }

    public static HttpEntity getQueue(String queue){
        JSONObject obj = new JSONObject();
        obj.put("queue", queue);
        String query = obj.toJSONString();
        return new StringEntity(query, ContentType.APPLICATION_JSON);
    }

    public static HttpEntity postTopic(String topic, String text) {
        JSONObject obj = new JSONObject();
        obj.put("topic", topic);
        obj.put("text", text);
        String query = obj.toJSONString();
        return new StringEntity(query, ContentType.APPLICATION_JSON);
    }


    public static HttpEntity getTopic(String topic){
        JSONObject obj = new JSONObject();
        obj.put("topic", topic);
        String query = obj.toJSONString();
        return new StringEntity(query, ContentType.APPLICATION_JSON);
    }

}
