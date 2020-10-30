package ru.job4j.pooh_jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogger {
    private static final Logger log = LoggerFactory.getLogger(PoohJMS.class);
    private static final String TAB = "\t\t\t\t\t\t\t\t\t";
    private static final String LN = System.lineSeparator();

    public static void log(SocketConnection connection, String request, String response) {
        log.info(LN + LN + "<<<<<<<<<<<" + Thread.currentThread().getName() +
                "\r\nFor host: " + connection.getName() +
                " HTTP REQUEST:\r\n" + request + addTab(
                "HTTP RESPONSE:\r\n" + response + ">>>>>>>>>\r\n\r\n"));

    }

    public static void log(SocketConnection connection, String message) {
        log.info(formatMessage(connection.getName(), message));
    }

    public static void warn(SocketConnection connection, String message) {
        log.warn(formatMessage(connection.getName(), message));
    }

    public static void warn(String message) {
        log.warn(formatMessage("", message));
    }

    private static String addTab(String string) {
        return string.lines().reduce("", (x, y) -> TAB + x + LN + TAB + y);
    }

    private static String formatMessage(String name, String message) {
        return LN + LN + "\r\nMessage for host: " + name + LN + addTab(message) + LN;
    }
}
