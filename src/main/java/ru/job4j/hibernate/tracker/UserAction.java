package ru.job4j.hibernate.tracker;

public interface UserAction {
    /**
     * Метод возвращает ключ опции.
     * @return key - ключ.
     */
    int key();

    /**
     * Основной метод.
     * @param input - объект типа Input
     * @param tracker - объект типа Tracker
     */
    void execute(Input input, ITracker tracker);

    /**
     * Метод возвращает информацию о данном пункте меню.
     * @return info - Строка меню
     */
    String info();
}