package ru.job4j.pooh_jms;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.job4j.pooh_jms.MyLogger.log;

abstract class JmsBase {
    final String terminalMessage;
    final Supplier<String> input;
    final Predicate<String> correctLine;
    final BiConsumer<String, SocketConnection> messageProcessor;
    private final Predicate<String> postCheck = (s) -> s.length() != 0 && !s.contains("\r\n") && !s.contains(" ");

    JmsBase(String terminalMessage,
            Predicate<String> checkLine,
            BiConsumer<String, SocketConnection> messageProcessor,
            Supplier<String> input) {
        this.terminalMessage = terminalMessage;
        this.input = input;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && postCheck.test(line);
    }

    JmsBase(String terminalMessage,
            Predicate<String> checkLine,
            BiConsumer<String, SocketConnection> messageProcessor) {
        this.terminalMessage = terminalMessage;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && postCheck.test(line);
        Scanner scanner = new Scanner(System.in);
        this.input = () -> {
            while (true) {
                try {
                    if ((System.in.available() != 0)) break;
                    Thread.sleep(1000);
                } catch (IOException e) {
                    return "";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return scanner.nextLine();
        };
    }

    /**
     * It can be read several http-posts
     *
     * @param connection Current connection
     * @return list of http posts
     */
    List<String> readHttp(SocketConnection connection) {
        return Arrays.stream(connection.readBlock()
                .split(HttpProcessor.MSG_DELIMITER))
//                .map(HttpProcessor::removeDelimiter)
                .filter(s -> s.length() > 0)
                .peek(httpPost -> log(connection.getName(), "incoming post is:\r\n" + httpPost))
                .collect(Collectors.toList());
    }
}
