package ru.job4j.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.hibernate.tracker.ITracker;
import ru.job4j.hibernate.tracker.Input;
import ru.job4j.hibernate.tracker.StartUI;

public class SpringDI {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ru.job4j.hibernate.tracker.ConsoleInput.class);
        context.register(ru.job4j.hibernate.tracker.HbmTracker.class);

        context.refresh();


        Input input = context.getBean(ru.job4j.hibernate.tracker.ConsoleInput.class);
        ITracker tracker = context.getBean(ru.job4j.hibernate.tracker.HbmTracker.class);

        ru.job4j.hibernate.tracker.StartUI ui = new StartUI(input, tracker, System.out::println);

        ui.init();

    }
}