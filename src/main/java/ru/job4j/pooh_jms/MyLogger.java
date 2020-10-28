package ru.job4j.pooh_jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogger {
    private static final Logger log = LoggerFactory.getLogger(PoohJMS.class);
    private static final String TAB = "\t\t\t\t\t\t\t\t\t";
    private static final String LN = System.lineSeparator();

    public static void log(String request, String response) {
        log.info("<<<<<<<<<<<" + Thread.currentThread().getName() + "\r\nHTTP REQUEST:\r\n"
                + request + addTab("HTTP RESPONSE:\r\n" + response + ">>>>>>>>>\r\n\r\n"));

    }

    public static void log(String message) {
        log.info(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    private static String addTab(String string) {
        return string.lines().reduce("", (x, y) -> TAB + x + LN + TAB + y);
    }
}
