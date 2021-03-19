package ru.job4j.di;

import ru.job4j.hibernate.tracker.ITracker;
import ru.job4j.hibernate.tracker.Input;
import ru.job4j.hibernate.tracker.StartUI;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();

        context.reg(ru.job4j.hibernate.tracker.ConsoleInput.class);
        context.reg(ru.job4j.hibernate.tracker.HbmTracker.class);

        Input input = context.get(ru.job4j.hibernate.tracker.ConsoleInput.class);
        ITracker tracker = context.get(ru.job4j.hibernate.tracker.HbmTracker.class);

        ru.job4j.hibernate.tracker.StartUI ui = new StartUI(input, tracker, System.out::println);

        ui.init();

    }
}