package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.job4j.pooh_jms.JmsBase.*;

public class Subscriber {
    private List<String> responses = new CopyOnWriteArrayList<>();

    public Subscriber(SocketConnection connection, InputStream in) {
        Thread daemon = new Thread(() -> {
            while (connection.isAlive()) {
                readHttp(connection, responses);
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
        try {
            connection.sendCloseRequest();
            Thread.sleep(1000);
            if (connection.isAlive()) {
                System.out.println("forced close connection");
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        new Subscriber(new SocketConnection(url, port, "subscriber"), System.in);
    }

    public Subscriber(Set<String> topics, SocketConnection connection) {
        if (connection != null) {
            new Thread(() -> {
                while (connection.isAlive()) {
                    readHttp(connection, responses);
                }
            }).start();

            topics.forEach(topic -> {
                subscribe(topic, connection);
            });
            try {
                Thread.sleep(500);
                subscribe("weather", connection); //unsubscribe
                connection.sendCloseRequest();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

