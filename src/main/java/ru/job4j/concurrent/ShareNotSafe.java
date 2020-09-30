package ru.job4j.concurrent;

public class ShareNotSafe {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            UserCache cache = new UserCache();
            User user = User.of("name");
            cache.add(user);
            Thread first = new Thread(
                    () -> {
                        user.setName("rename");
                    }
            );
            first.start();
            first.join();
            if (cache.findById(1).getName().equals("name")) System.out.println("OK");
        }
    }
}