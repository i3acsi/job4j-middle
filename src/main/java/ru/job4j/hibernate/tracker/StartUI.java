package ru.job4j.hibernate.tracker;

import java.util.function.Consumer;

public class StartUI {
    /**
     * Получение данных от пользователя.
     */
    private final Input input;

    /**
     * Создание хранилища для заявок.
     */
    private final ITracker tracker;

    private final Consumer<String> output;

    /**
     * Конструктор с инициалицацией полей.
     * @param input ввод данных.
     * @param tracker хранилище заявок.
     */
    public StartUI(Input input, ITracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }

    /**
     * Основой цикл программы.
     */
    public void init() {
        MenuTracker menu = new MenuTracker(this.input, this.tracker, this.output);
        menu.fillActions();
        int length = menu.getActionsLentgh();
        int[] range = new int[length];
        for (int i = 0; i < length; i++) {
            range[i] = range[i] + i;
        }
        do {
            menu.show();
            menu.select(input.ask("select", range));
        } while (!"y".equals(this.input.ask("Exit?(y) ")));
    }
}


