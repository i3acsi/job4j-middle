package ru.job4j.concurrent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.jcip.annotations.Immutable;

@Data
@Immutable
@EqualsAndHashCode
public class User {
    private final int id;
    private final String name;

    public static User of(final int id, final String name) {
        return new User(id, name);
    }
    private User(final int id, final String name){
        this.id = id;
        this.name = name;
    }
}