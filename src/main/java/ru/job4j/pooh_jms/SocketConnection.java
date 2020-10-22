package ru.job4j.pooh_jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection implements AutoCloseable {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final String url;

    public SocketConnection(String url, int port) {
        try {
            this.socket = getSocket(url, port);
            this.writer = createWriter();
            this.reader = createReader();
            this.url = url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketConnection(ServerSocket server) {
        try {
            this.socket = getSocket(server);
            this.writer = createWriter();
            this.reader = createReader();
            this.url = server.getInetAddress().getHostName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAdders(){
        return socket.getInetAddress().getHostAddress();
    }

    public void requestPostQueue(String queue, String text){
        writeLine(HttpProcessor.postQueueRequest(queue, text, url));
    }

    public void requestPostTopic(String topic, String text){
        writeLine(HttpProcessor.postTopicRequest(topic, text, url));
    }

    public void requestGetQueue(String queue){
        writeLine(HttpProcessor.getQueueRequest(queue, url));
    }

    public void requestGetTopic(String topic){
        writeLine(HttpProcessor.getQueueRequest(topic, url));
    }


    public void writeLine(String line) {
        writer.println(line);
        writer.flush();
    }



    public String readBlock() {
        StringBuilder result = new StringBuilder();

        reader.lines().forEach(result::append);
        return result.toString();
    }

    private PrintWriter createWriter() throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    private BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private Socket getSocket(String url, int port) throws IOException {
        return new Socket(url, port);
    }

    private Socket getSocket(ServerSocket server) throws IOException {
        return server.accept();
    }

    @Override
    public void close() throws Exception {
        writer.close();
        reader.close();
        socket.close();
    }

}

