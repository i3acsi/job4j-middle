package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static ru.job4j.pooh_jms.MyLogger.log;

public abstract class JmsCli{
    protected int port = 3345;
    protected String url = "127.0.0.1";

    protected void readResponse(SocketConnection connection, List<String> responses) {
        String response = connection.readBlockChecked();
        responses.add(response);
        log("Response:\r\n<\t\t" + response + "\t\t>");
    }


    private static class Config {
        private static final Properties values = new Properties();
        static  {
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream("jmsConfig")) {
                values.load(in);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        public static String get(String key) {
            return values.getProperty(key);
        }
    }
}
