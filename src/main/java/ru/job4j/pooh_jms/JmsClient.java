package ru.job4j.pooh_jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Config.port;
import static ru.job4j.pooh_jms.Config.url;

public class JmsClient extends JmsBase {
    private final BiConsumer<String, SocketConnection> responsesProcessor; // used for tests;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    JmsClient(String terminalMessage,
              Predicate<String> checkLine,
              BiConsumer<String, SocketConnection> messageProcessor,
              Supplier<String> input,
              BiConsumer<String, SocketConnection> responsesProcessor
    ) {
        super(terminalMessage, checkLine, messageProcessor, input);
        this.responsesProcessor = responsesProcessor;
    }

    JmsClient(String terminalMessage,
                     Predicate<String> checkLine,
                     BiConsumer<String, SocketConnection> messageProcessor) {
        super(terminalMessage, checkLine, messageProcessor);
        this.responsesProcessor = (s, connection) -> {
        };
    }


    public final void start(String name) {
        try (SocketConnection connection = new SocketConnection(url, port, name)) {
            executorService.submit(() -> {
                String command;
                while (connection.isAlive()) {
                    System.out.println("type stop to terminate" + this.terminalMessage);
                    command = input.get();
                    if (command.equals("stop")) {
                        System.out.println("Terminate " + name);
                        try {
                            connection.sendCloseRequest();
                            connection.close();
                            break;
                        } catch (Exception e) {
                            terminate();
                            throw new SocketClosedException(e.getMessage());
                        }
                    } else {
                        if (correctLine.test(command)) {
                            messageProcessor.accept(command, connection);
                        }
                    }
                }
                terminate();
            });
            executorService.submit(() -> {
                while (connection.isAlive()) {
                    readHttp(connection).forEach(req -> responsesProcessor.accept(req, connection));
                }
            });
            while (connection.isAlive()) {
                if (!connection.checkConnection()) {
                    terminate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void terminate() {
        executorService.shutdown();
        boolean terminated = false;
        try {
            terminated = executorService.awaitTermination(11, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!terminated) {
            executorService.shutdownNow();
        }
    }
}
