package ru.job4j.pooh_jms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

public class PoohJMS {
    private final ForkJoinPool pool;
    private final int port = 3345;

    public PoohJMS(){
        this.pool = ForkJoinPool.commonPool();
    }

    public void process(){
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket client = server.accept();
                pool.submit()
                executeIt.execute(new MonoThreadClientHandler(client));
                System.out.print("Connection accepted.");
            }

            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
