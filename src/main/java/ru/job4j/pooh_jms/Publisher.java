package ru.job4j.pooh_jms;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.job4j.pooh_jms.MyLogger.log;

public class Publisher extends JmsCli implements Runnable{
    private final List<Topic> topicList;
    private final List<String> responses = new CopyOnWriteArrayList<>();
    private SocketConnection connection;

    public Publisher(List<Topic> topicList) {
        this.topicList = topicList;
        this.connection = new SocketConnection(url, port);
        log("\r\nConnected: " + url);
    }

    public void run() {
        if (connection != null) {
            topicList.forEach(topic -> {
                String request = HttpProcessor.postTopicRequest(topic.getName(), topic.getContent(), url);
                connection.writeLine(request);
                String response = connection.readBlockChecked();
                log(request, response);
                responses.add(response);
            });
        }
    }

    public List<String> getResponses() {
        return responses;
    }
}

class Topic {
    private final String name;
    private final String content;

    public Topic(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}


