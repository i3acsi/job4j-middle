package ru.job4j.new_pooh_jms.server;

import ru.job4j.new_pooh_jms.Jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JmsServer extends Jms implements Runnable {
    private static JmsServer instance = null;
    private static final ExecutorService executor = Executors.newScheduledThreadPool(10);
    private Runnable readConsole;

    public JmsServer() {
        super();
        this.readConsole = () -> {
            String line = "";
            while (!line.equals("stop")) {
                System.out.println("type stop to terminate server" + this.terminalMessage);
                line = this.input.get();
            }
            System.out.println("Terminate server");
            terminate(connections, server);
            executor.execute();
        });;
    }

    @Override
    public void run() {
        List<SocketConnection> connections = new CopyOnWriteArrayList<>();
        try (ServerSocket server = new ServerSocket(port)) {
             executor.submit(
            while (!server.isClosed()) {
                SocketConnection connection;
                try {
                    connection = new SocketConnection(server);
                    connections.add(connection);
                    System.out.println("connected");
                    executorService.submit(() -> {
                        while (connection.isAlive()) {
                            readHttp(connection).forEach(req -> messageProcessor.accept(req, connection));
                        }
                        connections.remove(connection);
                    });
                } catch (SocketClosedException e) {
                    terminate(connections, server);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() {
        if (instance == null) {
            synchronized (JmsServer.class) {
                if (instance == null) {
                    new JmsServer().run();
                }
            }
        }
    }
}
