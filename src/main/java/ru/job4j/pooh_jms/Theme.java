package ru.job4j.pooh_jms;

public class Theme {
    private final String name;
    private final String content;

    public Theme(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
