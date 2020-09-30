package ru.job4j.concurrent;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ParseFile {

    private ParseFile() {
    }

    public static String getContent(String file) {
        synchronized (ParseFile.class) {
            StringBuilder result = new StringBuilder();
            try (FileChannel channel = FileChannel.open(Path.of(file), StandardOpenOption.READ)) {
                int bufferSize = 1024;
                if (bufferSize > channel.size()) {
                    bufferSize = (int) channel.size();
                }
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                while (channel.read(buffer)>0){
                    buffer.limit(buffer.position());
                    buffer.rewind();
                    result.append(StandardCharsets.UTF_8.decode(buffer).toString());
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }
    }

    public static String getContentWithoutUnicode(String file) throws IOException {
        File f = new File(file);
        InputStream i = new FileInputStream(f);
        String output = "";
        int data;
        while ((data = i.read()) > 0) {
            if (data < 0x80) {
                output += (char) data;
            }
        }
        return output;
    }

    public static void saveContent(String content, File file) {
        try (OutputStream o = new FileOutputStream(file)) {

            for (int i = 0; i < content.length(); i += 1) {
                o.write(content.charAt(i));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}