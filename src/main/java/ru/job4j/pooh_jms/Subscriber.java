package ru.job4j.pooh_jms;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.job4j.pooh_jms.MyLogger.log;

public class Subscriber extends JmsCli implements Runnable {
    private Set<String> topics;
    private List<String> responses = new CopyOnWriteArrayList<>();
    private SocketConnection connection;
    private String closeRequest = new String("POST /exit");


    public Subscriber(Set<String> topics) {
        this.topics = topics;
        this.connection = new SocketConnection(url, port);
        log("\r\nConnected: " + url);
    }


    public List<String> getResponses() {
        return responses;
    }

    @Override
    public void run() {
        if (connection != null) {
            topics.forEach(topic -> {
                String request = HttpProcessor.getTopicRequest(topic, url);
                connection.writeLine(request); // после этого уже могут начать приходить сообщения по топику
                String response = connection.readBlockChecked();
                responses.add(response);
                log(request, response);
            });
            int counter = 0;
            while (counter < 4) {
                String response = connection.readBlockChecked();
                responses.add(response);
                log("Response:\r\n<\t\t" + response + "\t\t>");
                counter++;
            }
            topics.forEach(topic -> {
                String request = HttpProcessor.getTopicRequest(topic, url);
                connection.writeLine(request);
                String response = connection.readBlockChecked();
                responses.add(response);
                log(request, response);
            });
            connection.writeLine(closeRequest);
        }
    }
}
