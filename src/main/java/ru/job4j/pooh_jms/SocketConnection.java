package ru.job4j.pooh_jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnection implements AutoCloseable {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private boolean alive = false;
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
            int i = counter.getAndIncrement();
            this.name = name + " " + i;
            this.alive = true;
        } catch (IOException e) {
            throw new SocketClosedException(e.getMessage());
        }
    }

    SocketConnection(ServerSocket server) {
        try {
            this.socket = getSocket(server);
            this.out = socket.getOutputStream();
            this.in = socket.getInputStream();
            this.name = "server";
            this.alive = true;
        } catch (IOException e) {
            throw new SocketClosedException(e.getMessage());
        }
    }

    String getAdders() {
        return socket.getInetAddress().toString();
    }


    void writeLine(String line) {
        try {
            String msg = HttpProcessor.addDelimiter(line);
            out.write(msg.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String readBlock() {
        byte[] data = new byte[128 * 1024];
        int readBytes = 0;
        while (readBytes <= 0) {
            try {
                readBytes = in.read(data);
                System.out.println(readBytes);
            } catch (SocketTimeoutException ex) {
                System.out.println("timeout");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("EXC");
                this.alive = false;
                return "";
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

    boolean isAlive() {
        return alive;
    }

    void sendCloseRequest() {
        writeLine(HttpProcessor.closeRequest(name));
    }

    @Override
    public void close() throws Exception {
        this.alive = false;
        in.close();
        out.close();
        socket.close();
    }
}

