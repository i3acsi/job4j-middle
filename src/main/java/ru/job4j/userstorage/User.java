package ru.job4j.userstorage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @Setter
    @EqualsAndHashCode.Include
    private int amount;
    private static volatile int counter = 0;

    public User() {
        synchronized (User.class) {
            this.id = counter++;
        }
        this.amount = 0;
    }

    public static User of(User user) {
        if (user != null) {
            synchronized (user) { // String.valueOf(user.id)
                return new User(user.getId(), user.getAmount());
            }
        }
        return null;
    }

    private User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public static void clear(){
        counter = 0;
    }
}
