package ru.job4j.hibernate.tracker;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MenuTracker {
    /**
     * хранит ссылку на объект .
     */
    private Input input;
    /**
     * хранит ссылку на объект .
     */
    private ITracker tracker;

    private final Consumer<String> output;

    /**
     * хранит ссылку на массив типа UserAction.
     */
    private List<UserAction> actions = new ArrayList<>();

    /**
     * Конструктор.
     *
     * @param input   объект типа Input
     * @param tracker объект типа Tracker
     */
    public MenuTracker(Input input, ITracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }

    /**
     * Метод для получения массива меню.
     *
     * @return длину массива
     */
    public int getActionsLentgh() {
        return this.actions.size();
    }

    /**
     * Метод заполняет массив.
     */
    public void fillActions() {
        this.actions.add(new AddItem(0, "Add new Item."));
        this.actions.add(new FindAll(1, "Find all items."));
        this.actions.add(new Edit(2, "Edit Item."));
        this.actions.add(new Delete(3, "Delete Item."));
        this.actions.add(new FindById(4, "Find Item by ID."));
        this.actions.add(new FindByName(5, "Find Items by Name."));
		this.actions.add(new Comment(6, "Comment."));
        this.actions.add(new Exit(7, "Exit."));
    }

    /**
     * Метод в зависимости от указанного ключа, выполняет соотвествующие действие.
     *
     * @param key ключ операции
     */
    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    /**
     * Метод выводит на экран меню.
     */
    public void show() {
        for (UserAction action : this.actions) {
            if (action != null) {
                output.accept(String.format("%d. %s", action.key(), action.info()));
            }
        }
    }
}