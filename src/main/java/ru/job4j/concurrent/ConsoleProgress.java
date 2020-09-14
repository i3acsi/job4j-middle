package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    private int count = 0;
    private String[] process = {"-", "\\", "|", "/"};

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\r load: " + process[count]);
            count = count == 3 ? 0 : count + 1;
        }
        count = 0;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1000); /* симулируем выполнение параллельной задачи в течение 1 секунды. */
        progress.interrupt(); //
    }
}
