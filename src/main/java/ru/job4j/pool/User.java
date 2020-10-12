package ru.job4j.pool;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class User {
    @EqualsAndHashCode.Include
    private final String userName;

    @EqualsAndHashCode.Include
    private final String email;

    public User(final String userName, final String email) {
        this.userName = userName;
        this.email = email;
    }
}
