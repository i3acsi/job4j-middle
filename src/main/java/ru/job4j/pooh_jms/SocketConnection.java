package ru.job4j.pooh_jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection implements AutoCloseable {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;
    private final String url;

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


    String readBlock() {
        try {
            byte[] data = new byte[32 * 1024];
            int readBytes = 0;
            readBytes = in.read(data);
            return new String(data, 0, readBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Socket getSocket(String url, int port) throws IOException {
        return new Socket(url, port);
    }

    private Socket getSocket(ServerSocket server) throws IOException {
        Socket res = server.accept();
        res.setSoTimeout(1000);
        return res;
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }

}

