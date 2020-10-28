package ru.job4j.pooh_jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class SocketConnection implements AutoCloseable {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private final String url;

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

    SocketConnection(String url, int port) {
        try {
            this.socket = getSocket(url, port);
            this.out = socket.getOutputStream();
            this.in = socket.getInputStream();
            this.url = url;
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
        try {
            readBytes = in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(data, 0, readBytes);

    }

    String readBlock() throws IOException {

            byte[] data = new byte[32 * 1024];
            int readBytes = 0;
            readBytes = in.read(data);
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

    public boolean isAlive() {
        try {
            in.available();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }

}

