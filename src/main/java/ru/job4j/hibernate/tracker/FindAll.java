package ru.job4j.hibernate.tracker;

import ru.job4j.hibernate.Item;

import java.util.List;

/**
 * Класс для отображения в консоли всех существующих заявок.
 */
public class FindAll extends  BaseAction {
    
    public FindAll(int key, String info) {
        super(key, info);
    }
	
	@Override
	public void execute(Input input, ITracker tracker) {
	    List<Item> temp = tracker.findAll();
        if (temp.size() != 0) {
            System.out.println("----------Все существующие заявки---------");
            for (Item item : temp) {
                System.out.println(item.show());
            }
            System.out.println("------------------------------------------");
        } else {
            System.out.println("----------------Заявок нет----------------");
        }
	}
 }