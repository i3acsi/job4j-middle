package ru.job4j.concurrent;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ParseFile {
    private static ConcurrentMap<File, ParseFile> files = new ConcurrentHashMap<>();
    private File file;

    public static ParseFile of(File file) {
        return files.computeIfAbsent(file, k -> new ParseFile(file));
    }

    private ParseFile(File file) {
        this.file = file;
    }

    public String getContent() {
        if (this.file.exists()) {
            synchronized (this) {
                StringBuilder result = new StringBuilder();
                try (FileChannel channel = FileChannel.open(this.file.toPath(), StandardOpenOption.READ)) {
                    int bufferSize = 1024;
                    if (bufferSize > channel.size()) {
                        bufferSize = (int) channel.size();
                    }
                    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                    while (channel.read(buffer) > 0) {
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
        return null;
    }

    public String getContentWithoutUnicode() {
        if (this.file.exists()) {
            synchronized (this) {
                int data = 0;
                StringBuilder result = new StringBuilder();
                try (FileChannel channel = FileChannel.open(this.file.toPath(), StandardOpenOption.READ)) {
                    int bufferSize = 1024;
                    if (bufferSize > channel.size()) {
                        bufferSize = (int) channel.size();
                    }
                    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                    while (channel.read(buffer) > 0) {
                        int limit = buffer.position();
                        buffer.limit(limit);
                        buffer.rewind();
                        for (int i = 0; i < limit; i++) {
                            data = buffer.get();
                            if (data == 13 || data == 10 || data > 90) { //if (data == 13 || data == 10 || data > 90)
                                result.append((char) data);
                            }
                        }
                        buffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result.toString();
            }
        }
        return null;
    }

    public void saveContent(String content) {
        synchronized (this) {
            if (!file.exists()) {
                try {
                    Files.createFile(this.file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file.exists()) {
                try (FileChannel channel = FileChannel.open(file.toPath(),
                        StandardOpenOption.APPEND)) {
                    ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseFile parseFile = (ParseFile) o;
        return Objects.equals(file, parseFile.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}