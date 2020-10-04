package ru.job4j.concurrent;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WgetTest {

    @Test
    public void fileLoaded() throws IOException {
        String url = "https://decoretto.ru/upload/iblock/149/1492d399120c1acf866bb25749663d1e.jpg";
        File result = new Wget(url, 100).call();
        FileChannel resultChannel = FileChannel.open(
                result.toPath(), StandardOpenOption.READ);
        FileChannel expectedChannel = FileChannel.open(
                Path.of("src/main/resources/test.jpg"),
                StandardOpenOption.READ);
        ByteBuffer resultBuf = ByteBuffer.allocate(1024);
        ByteBuffer expectedBuf = ByteBuffer.allocate(1024);
        while (resultChannel.read(resultBuf) > 1 && expectedChannel.read(expectedBuf) > 1) {
            for (int i = 0; i < resultBuf.limit(); i++) {
                assertThat(resultBuf.get(i), is(expectedBuf.get(i)));
            }
            resultBuf.clear();
            expectedBuf.clear();
        }
        Files.deleteIfExists(result.toPath());
    }

    @Test
    public void fileNotLoaded() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String url = "error.jpg";
        Wget wget = new Wget(url, 100);
        wget.call();
        assertThat(out.toString(),
                is(
                        "Wrong url to download\r\n"
                ));
        System.setOut(stdout);

    }
}
