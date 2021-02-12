package ru.job4j.hibernate.tracker;

import ru.job4j.hibernate.Item;

/**
 * Класс для поиска заявки по ID.
 */
public class FindById extends  BaseAction {
    
    public FindById(int key, String info) {
        super(key, info);
    }
	
	@Override
	public void execute(Input input, ITracker tracker) {
	    System.out.println("----------Поиск заявки по ID----------");
        String id = input.ask("Введите id искомой заявки ");
        Item temp = tracker.findById(id);
        if (temp != null) {
            System.out.println("Найдена заявка " + temp.show());
        } else {
            System.out.println("---------Заявка не найдена---------");
        }
	}
}