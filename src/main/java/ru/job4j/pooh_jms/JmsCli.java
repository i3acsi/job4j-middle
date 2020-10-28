package ru.job4j.pooh_jms;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Properties;

public abstract class JmsCli{
    protected int port = 3345;
    protected String url = "127.0.0.1";





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
