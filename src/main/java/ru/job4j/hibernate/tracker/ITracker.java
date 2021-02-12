package ru.job4j.hibernate.tracker;

import ru.job4j.hibernate.Item;

import java.util.List;

public interface ITracker {
    Item add(Item item);
    boolean replace(String id, Item item);
    boolean delete(String id);
    List<Item> findAll();
    List<Item> findByName(String key);
    Item findById(String id);
    void clean();
}