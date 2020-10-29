package ru.job4j.pooh_jms;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnection implements AutoCloseable {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private final String url;
    private boolean alive = true;
    private final String name;
    private static final AtomicInteger counter = new AtomicInteger(0);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketConnection that = (SocketConnection) o;
        return Objects.equals(socket, that.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }

    SocketConnection(String url, int port, String name) {
        try {
            this.socket = getSocket(url, port);
            this.out = socket.getOutputStream();
            this.in = socket.getInputStream();
            socket.getInputStream();
            this.url = url;
            int i = counter.getAndIncrement();
            this.name = name + " " + i;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    SocketConnection(ServerSocket server) {
        try {
            this.socket = getSocket(server);
            this.out = socket.getOutputStream();
            this.in = socket.getInputStream();
            this.url = server.getInetAddress().toString();
            this.name = "server";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String getAdders() {
        return socket.getInetAddress().toString();
    }


    void writeLine(String line) {
        try {
            out.write(line.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String readBlockChecked() {

        byte[] data = new byte[32 * 1024];
        int readBytes = 0;
        while (readBytes == 0) {
            try {
                readBytes = in.read(data);
            } catch (SocketTimeoutException ex) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(data, 0, readBytes);
    }

    private Socket getSocket(String url, int port) throws IOException {
        return new Socket(url, port);
    }

    private Socket getSocket(ServerSocket server) throws IOException {
        Socket res = server.accept();
        res.setSoTimeout(1000);
        return res;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void sendCloseRequest() {
        writeLine("POST /exit\r\n" + "Host: " + name);
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
        this.alive = false;
    }
}

