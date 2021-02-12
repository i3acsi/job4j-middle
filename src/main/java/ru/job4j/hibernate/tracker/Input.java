package ru.job4j.hibernate.tracker;

public interface Input {
    String ask(String question);

    int ask(String question, int[] range);
}
