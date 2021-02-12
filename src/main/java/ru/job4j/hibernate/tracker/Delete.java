package ru.job4j.hibernate.tracker;

/**
 * Класс для удаления заявки.
 */
public class Delete extends  BaseAction {
    
    public Delete(int key, String info) {
        super(key, info);
    }
	
	private final String ln = System.lineSeparator();
	
	@Override
	public void execute(Input input, ITracker tracker) {
	    System.out.println("----------Удаление заявки----------");
        String id = input.ask("Введите id удаляемой заявки ");
        if (tracker.delete(id)) {
            System.out.printf("-------Заявка с ID %s удалена------" + ln, id);
        } else {
            System.out.println("---------Заявка не найдена---------");
        }
	}
}