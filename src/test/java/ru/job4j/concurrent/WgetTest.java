package ru.job4j.concurrent;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WgetTest {

    @Test
    public void fileLoaded() throws IOException, InterruptedException {
        String url = "https://decoretto.ru/upload/iblock/149/1492d399120c1acf866bb25749663d1e.jpg";
        String outFile = "1492d399120c1acf866bb25749663d1e.jpg";
        String expectedFile = "src/main/resources/test.jpg";
        Wget wget = new Wget(url, 100);
        wget.begin();
        FileChannel resultChannel = FileChannel.open(Path.of(outFile), StandardOpenOption.READ);
        FileChannel expectedChannel = FileChannel.open(Path.of(expectedFile), StandardOpenOption.READ);
        ByteBuffer resultBuf = ByteBuffer.allocate(1024);
        ByteBuffer expectedBuf = ByteBuffer.allocate(1024);
        while (resultChannel.read(resultBuf) > 1 && expectedChannel.read(expectedBuf) > 1) {
            for (int i = 0; i < resultBuf.limit(); i++) {
                assertThat(resultBuf.get(i), is(expectedBuf.get(i)));
            }
            resultBuf.clear();
            expectedBuf.clear();
        }
        Files.deleteIfExists(Path.of(outFile));
    }

    @Test
    public void fileNotLoaded() throws InterruptedException {
        PrintStream stdout = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String url = "error.jpg";
        Wget wget = new Wget(url, 100);
        wget.begin();
        assertThat(out.toString(),
                is(
                        "Wrong url to download\r\n"
                ));
        System.setOut(stdout);

    }
}
