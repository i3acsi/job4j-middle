package ru.job4j.concurrent;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

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
        return getContentWithPredicate((x) -> true);
    }

    public String getContentWithoutUnicode() {
        return getContentWithPredicate((x) -> x == 13 || x == 10 || x > 90);
    }

    private String getContentWithPredicate(Predicate<Integer> predicate) {
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
                            if (predicate.test(data)) {
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