package ru.job4j.hibernate.tracker;

import java.util.Scanner;

public class ConsoleInput implements Input {
    private Scanner scanner = new Scanner(System.in);

    private boolean exist(int key, int[] range) {
        boolean result = false;
        for (int value: range) {
            if (value == key) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public String ask(String question) {
        System.out.print(question + ":");
        return scanner.nextLine();
    }

    @Override
    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
		if (!exist(key, range)) {
            throw new MenuOutException("Out of menu range");
		}
		return key;
    }
}
