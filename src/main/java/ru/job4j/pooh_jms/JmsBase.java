package ru.job4j.pooh_jms;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.job4j.pooh_jms.MyLogger.log;

public abstract class JmsBase {
    protected final String terminalMessage;
    protected final Supplier<String> input;
    protected final Predicate<String> correctLine;
    protected final BiConsumer<String, SocketConnection> messageProcessor;
    protected final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public JmsBase(String terminalMessage,
                   Predicate<String> checkLine,
                   BiConsumer<String, SocketConnection> messageProcessor,
                   Supplier<String> input) {
        this.terminalMessage = terminalMessage;
        this.input = input;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
    }

    public JmsBase(String terminalMessage,
                   Predicate<String> checkLine,
                   BiConsumer<String, SocketConnection> messageProcessor) {
        this.terminalMessage = terminalMessage;
        this.messageProcessor = messageProcessor;
        this.correctLine = line -> checkLine.test(line) && line.length() != 0 && !line.contains("\r\n") && !line.contains(" ");
        this.input = () -> { //default
            Scanner scanner = new Scanner(System.in);
            while (!scanner.hasNext()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    public List<String> readHttp(SocketConnection connection) {
        return Arrays.stream(connection.readBlock()
                .split(HttpProcessor.MSG_DELIMITER))
                .map(HttpProcessor::removeDelimiter)
                .filter(s -> s.length() > 0)
                .peek(httpPost -> log(connection.getName(), "incoming post is:\r\n" + httpPost))
                .collect(Collectors.toList());
    }
}
