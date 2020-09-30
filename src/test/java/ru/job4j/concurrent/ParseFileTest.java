package ru.job4j.concurrent;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParseFileTest {
    private String src = "src/main/resources/expectedBigTextDescending.txt";
    private String srcUnicode = "src/main/resources/expectedBigTextDescendingWithUnicode.txt";
    private File input = new File(src);
    @Test
    public void whenGetContentThenGetIt() throws IOException {
        StringBuilder expected = new StringBuilder();
        Files.readAllLines(Path.of(src)).forEach(s->expected.append(s).append(System.lineSeparator()));
        expected.delete(expected.lastIndexOf(System.lineSeparator()), expected.length());
        String result = ParseFile.getContent(src);
        assertThat(result, is(expected.toString()));
    }

    @Test
    public void whenGetContentWithUnicodeThenGetTextWithoutIt() throws IOException {
        StringBuilder expected = new StringBuilder();
        Files.readAllLines(Path.of(src)).forEach(s->expected.append(s).append(System.lineSeparator()));
        expected.delete(expected.lastIndexOf(System.lineSeparator()), expected.length());
        String result = ParseFile.getContentWithoutUnicode(srcUnicode);
        assertThat(result, is(expected.toString()));
    }

}