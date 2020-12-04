package ru.job4j.new_pooh_jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Jms {
    Supplier<String> input;
    Consumer<SocketConnection> handleRequest;

    public Jms(Supplier<String> input, Consumer<SocketConnection> handleRequest) {
        this.input = input;
        this.handleRequest = handleRequest;
    }

    public Jms(Consumer<SocketConnection> handleRequest) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Thread daemon = new Thread(() -> {
            while (true) {
                synchronized (reader) {
                    try {
                        while (System.in.available() == 0) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        try {
                            System.out.write("stop".getBytes());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    finally {
                        reader.notify();
                    }
                }
            }

        });
        daemon.setDaemon(true);
        daemon.start();
        this.input = () -> {
            String result = "";
            synchronized (reader){
                try {
                    while (System.in.available()==0)
                        reader.wait();
                    StringBuilder sb = new StringBuilder();
                    while (System.in.available()>0){
                        sb.append(reader.readLine()).append(System.lineSeparator());
                    }
                    result = sb.toString();
                } catch (InterruptedException | IOException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                finally {
                    reader.notify();
                }
            }
            return result;
        };
        this.handleRequest = handleRequest;
    }
}
