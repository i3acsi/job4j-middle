package ru.job4j.pooh_jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static ru.job4j.pooh_jms.MyLogger.log;

class JmsBase {
    protected List<String> responses = new CopyOnWriteArrayList<>();
    static int port;
    static String url;

    static {
        BufferedReader reader = new BufferedReader(new InputStreamReader(JmsBase.class.getResourceAsStream("/jmsConfig")));
        try {
            port = Integer.parseInt(param(reader.readLine()));
            url = param(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It can be read several http-posts
     *
     * @param connection Current connection
     * @param incoming   container for incoming requests
     * @return list of http posts
     */
    static List<String> readHttp(SocketConnection connection, List<String> incoming) {
        List<String> result = splitToHttpPosts(connection.readBlock());
        result.forEach(post -> {
            incoming.add(post);
            log(connection, "incoming post is:\r\n" + post);

        });
        return result;
    }

    private static String param(String string) {
        return string.substring(string.indexOf("=") + 1);
    }

    static List<String> splitToHttpPosts(String post) {
        return Arrays.stream(post.split(HttpProcessor.MSG_DELIMITER)).map(HttpProcessor::removeDelimiter).filter(s -> s.length() > 0).collect(Collectors.toList());
    }

    public List<String> getResponses() {
        return responses;
    }
}
