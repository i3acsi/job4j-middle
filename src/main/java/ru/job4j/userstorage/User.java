package ru.job4j.userstorage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class User {
    @EqualsAndHashCode.Include
    private final int id;

    @EqualsAndHashCode.Include
    @Setter
    private int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public User(int id) {
        this.id = id;
        this.amount = 0;
    }
}
