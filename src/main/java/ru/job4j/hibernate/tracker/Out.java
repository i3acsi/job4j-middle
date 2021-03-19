package ru.job4j.hibernate.tracker;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class Out implements Consumer<String> {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
}
