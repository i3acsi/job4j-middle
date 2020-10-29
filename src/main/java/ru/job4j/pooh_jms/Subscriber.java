package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subscriber extends JmsCli {
    private Set<String> topics;
    private List<String> responses = new CopyOnWriteArrayList<>();

    public Subscriber(SocketConnection connection, InputStream in) {
        Thread daemon = new Thread(() -> {
            while (connection.isAlive()) {
                readResponse(connection, responses);
            }
        });
        daemon.setDaemon(true);
        daemon.start();
        Scanner scanner = new Scanner(in);
        String line = "";
        while (!"stop".equals(line)) {
            System.out.println("type stop to terminate, or name of topic to subscribe / unsubscribe");
            if (line.length() > 0 && !line.contains(" ") && !line.contains("\r\n")) {
                subscribe(line.toLowerCase().trim(), connection);
            }
            line = scanner.nextLine();
        }
    }

    private void subscribe(String topic, SocketConnection connection) {
        String request = HttpProcessor.getTopicRequest(topic, connection.getName());
        connection.writeLine(request); // после этого уже могут начать приходить сообщения по топику
    }

    public List<String> getResponses() {
        return responses;
    }

    public static void main(String[] args) {
       new Subscriber(new SocketConnection("127.0.0.1", 3345, "subscriber"), System.in);
    }

    public Subscriber(Set<String> topics, SocketConnection connection) {
        this.topics = topics;
        if (connection != null) {
            new Thread(() -> {
                while (connection.isAlive()) {
                    readResponse(connection, responses);
                }
            }).start();

            topics.forEach(topic -> {
                subscribe(topic, connection);
            });
            try {
                Thread.sleep(3000);
                subscribe("weather", connection); //unsubscribe
                Thread.sleep(1000);
                connection.sendCloseRequest();
                Thread.sleep(1000);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("disconnected");
    }
}

