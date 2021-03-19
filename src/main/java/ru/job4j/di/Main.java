package ru.job4j.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.job4j.di");
        context.refresh();
        StartUI ui = context.getBean(StartUI.class);

        ui.add("Petr Arsentev");
        ui.add("Ivan ivanov");
        ui.print();

        StartUI ui2 = context.getBean(StartUI.class);

        ui2.print();
        ui2.add("Petr ivanov");
        ui2.add("Ivan Arsentev");
        ui2.print();
        ui.print();

    }
}