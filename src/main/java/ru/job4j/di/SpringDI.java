package ru.job4j.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.hibernate.tracker.StartUI;

public class SpringDI {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.job4j.hibernate.tracker");
        context.refresh();
        StartUI ui = context.getBean(StartUI.class);

        ui.init();

    }
}