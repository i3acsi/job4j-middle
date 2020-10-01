package ru.job4j.concurrent;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ParseFileTest {
    private String src = "src/main/resources/expectedBigTextDescending.txt";
    private String srcUnicode = "src/main/resources/expectedBigTextDescendingWithUnicode.txt";
    private String tmp = "src/main/resources/tmp.txt";
    String content = src + srcUnicode + tmp;

    @Test
    public void whenGetContentThenGetIt() throws IOException {
        File file = new File(src);
        ParseFile parseFile = ParseFile.of(file);
        StringBuilder expected = new StringBuilder();
        Files.readAllLines(Path.of(src)).forEach(s -> expected.append(s).append(System.lineSeparator()));
        expected.delete(expected.lastIndexOf(System.lineSeparator()), expected.length());
        String result = parseFile.getContent();
        assertThat(result, is(expected.toString()));
    }

    @Test
    public void whenGetContentWithUnicodeThenGetTextWithoutIt() throws IOException {
        File unicode = new File(srcUnicode);
        ParseFile parseFile = ParseFile.of(unicode);
        StringBuilder expected = new StringBuilder();
        Files.readAllLines(Path.of(src)).forEach(s -> expected.append(s).append(System.lineSeparator()));
        expected.delete(expected.lastIndexOf(System.lineSeparator()), expected.length());
        String result = parseFile.getContentWithoutUnicode();
        result = result.replaceFirst("\r\n", "");
        assertThat(result, is(expected.toString()));
    }

    @Test
    public void whenSaveContentThanFileContentsData() throws IOException {

        File toSave = new File(tmp);
        assertThat(!toSave.exists(), is(true));
        ParseFile parseFile =ParseFile.of(toSave);
        parseFile.saveContent(content);
        String result = parseFile.getContent();
        assertThat(result, is(content));

        parseFile.saveContent(content);
        result = parseFile.getContent();
        assertThat(result, is(content + content));

        Files.deleteIfExists(toSave.toPath());
    }

    @Test
    public void whenFileNotExistsThenGetNull(){
        File f1= new File("null");
        ParseFile parseFile = ParseFile.of(f1);
        assertNull(parseFile.getContent());

        assertNull(parseFile.getContentWithoutUnicode());
    }
}