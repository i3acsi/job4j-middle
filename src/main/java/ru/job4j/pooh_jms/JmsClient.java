package ru.job4j.pooh_jms;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.job4j.pooh_jms.Config.port;
import static ru.job4j.pooh_jms.Config.url;

public class JmsClient extends JmsBase {
    private BiConsumer<String, SocketConnection> responsesProcessor = (s, connection) -> {
    }; // used for tests;

    public JmsClient(String terminalMessage,
                     Predicate<String> checkLine,
                     BiConsumer<String, SocketConnection> messageProcessor,
                     Supplier<String> input,
                     BiConsumer<String, SocketConnection> responsesProcessor
    ) {
        super(terminalMessage, checkLine, messageProcessor, input);
        this.responsesProcessor = responsesProcessor;
    }

    public JmsClient(String terminalMessage,
                     Predicate<String> checkLine,
                     BiConsumer<String, SocketConnection> messageProcessor) {
        super(terminalMessage, checkLine, messageProcessor);
    }


    public final void start(String name) {
        try (SocketConnection connection = new SocketConnection(url, port, name)) {
            Runnable console = () -> {
                String tmp;
                while (connection.isAlive()) {
                    System.out.println("type stop to terminate" + this.terminalMessage);
                    tmp = input.get();
                    if (tmp.equals("stop")) {
                        break;
                    } else {
                        if (correctLine.test(tmp)) {
                            messageProcessor.accept(tmp, connection);
                        }
                    }
                }
                System.out.println("Terminate " + name);
                try {
                    connection.sendCloseRequest();
                    connection.close();
                } catch (Exception e) {
                    throw new SocketClosedException(e.getMessage());
                }
            };
            executorService.submit(console);
            Runnable task = () -> { //Thread?
                while (connection.isAlive()) {
                    readHttp(connection).forEach(req -> responsesProcessor.accept(req, connection));
                }
            };
            executorService.submit(task);
            while (connection.isAlive()) {
                if (!connection.checkConnection()) {
                    executorService.shutdown();
                    break;
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
