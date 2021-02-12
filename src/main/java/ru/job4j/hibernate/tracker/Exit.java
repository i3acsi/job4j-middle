package ru.job4j.hibernate.tracker;

/**
 * Класс для добавления комментария к заявке.
 */
public class Exit extends  BaseAction {
    
    public Exit(int key, String info) {
        super(key, info);
    }
	
	@Override
	public void execute(Input input, ITracker tracker) {
	}
}