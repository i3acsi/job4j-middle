package ru.job4j.pooh_jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Config {
    static int port;
    static String url;

    static {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Config.class.getResourceAsStream("/jmsConfig")));
        try {
            port = Integer.parseInt(param(reader.readLine()));
            url = param(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String param(String string) {
        return string.substring(string.indexOf("=") + 1);
    }
}
