package ru.job4j.userstorage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @ EqualsAndHashCode.Include
    private int id;
    @Setter
    private int amount;
    private static volatile int counter = 0;

    public User(){
        synchronized (User.class){
            this.id = counter++;
        }
        this.amount=0;
    }


}
