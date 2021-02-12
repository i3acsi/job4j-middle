package ru.job4j.hibernate.tracker;

import ru.job4j.hibernate.Item;

/**
 * Класс для добавления новой заявки.
 */
public class AddItem extends  BaseAction {
    
    public AddItem(int key, String info) {
        super(key, info);
    }
	
	@Override
	public void execute(Input input, ITracker tracker) {
        System.out.println("----------Добавление новой заявки----------");
        String task = input.ask("Введите название заявки ");
        String desc = input.ask("Введите описание заявки ");
        System.out.println("-------------------------------------------");
        Item temp = new Item(task, desc);
        tracker.add(temp);
        System.out.println("Заявка добавлена." + temp.show());
	}
}