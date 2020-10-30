package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class Publisher extends JmsClient {
    public Publisher(SocketConnection connection, InputStream in) {
        if (connection != null) {
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
                System.out.println("type stop to terminate, or name_of_topic/text to post : name/text");
                if (line.length() > 0 && !line.contains(" ") && !line.contains("\r\n") && line.contains("/")) {
                    String[] parts = line.split("/");
                    connection.writeLine(HttpProcessor.postTopicRequest(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim(), connection.getName()));
                }
                line = scanner.nextLine();
            }
        }
    }

    public Publisher(List<Topic> topicList, SocketConnection connection) {
        if (connection != null) {
            new Thread(() -> {
                while (connection.isAlive()) {
                    readHttp(connection, responses);
                }
            }).start();

            topicList.forEach(topic -> {
                String request = HttpProcessor.postTopicRequest(topic.getName(), topic.getContent(), connection.getName());
                connection.writeLine(request);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try {
                connection.sendCloseRequest();
                Thread.sleep(500);
                if (connection.isAlive()) {
                    System.out.println("forced close connection");
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public List<String> getResponses() {
        return responses;
    }

    public static void main(String[] args) {
        new Publisher(new SocketConnection(url, port, "publisher"), System.in);
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


