package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Publisher extends JmsCli {
    List<String> responses = new CopyOnWriteArrayList<>();

    public Publisher(SocketConnection connection, InputStream in) {
        if (connection != null) {
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
                    readResponse(connection, responses);
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
                Thread.sleep(1000);
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
        new Publisher(new SocketConnection("127.0.0.1", 3345, "publisher"), System.in);
//        List<Topic> topicList = List.of(
//                new Topic("weather", "34"),
//                new Topic("weather", "-12"),
//                new Topic("city", "Moscow"),
//                new Topic("city", "Novosibirsk"),
//                new Topic("weather", "3"),
//                new Topic("weather", "-1"),
//                new Topic("city", "Saint_Petersburg"),
//                new Topic("city", "Leningrad"),
//                new Topic("weather", "-24"),
//                new Topic("weather", "21"),
//                new Topic("city", "Yekaterinburg"),
//                new Topic("city", "Chelyabinsk")
//
//        );
//        Runnable postTask = () -> {
//            SocketConnection connection = new SocketConnection("127.0.0.1", 3345, "publisher ");
//            Publisher pub = new Publisher(topicList, connection);
//            System.out.println("publisher is closed");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("\r\n\r\n\r\nPUB RESPONSES:\r\n");
//            pub.getResponses().forEach(System.out::println);
//        };
//        Thread t = new Thread(postTask);
//        t.start();
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


