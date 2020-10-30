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
class Topic extends Theme {
    public Topic(String name, String content) {
        super(name, content);
    }
}

class MyQueue extends Theme {
    public MyQueue(String name, String content) {
        super(name, content);
    }
}
