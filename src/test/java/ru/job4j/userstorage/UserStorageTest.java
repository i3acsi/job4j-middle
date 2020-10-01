package ru.job4j.userstorage;


import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserStorageTest {

    private class UserProcessor extends Thread {
        private User user;

        private UserProcessor() {}

        @Override
        public void run() {
            this.user = new User();
            System.out.println(this.user.getId());
        }
        public User getUser(){
            return this.user;
        }
    }

    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        //Создаем счетчик.

        //Создаем нити.
        UserProcessor first = new UserProcessor();
        UserProcessor second = new UserProcessor();
        UserProcessor third = new UserProcessor();

        //Запускаем нити.
        first.start();
        second.start();
        third.start();
        //Заставляем главную нить дождаться выполнения наших нитей.
        first.join();
        second.join();
        third.join();
        //Проверяем результат.
//        assertThat(first.getUser().getId(), is(2));
//        assertThat(second.getUser().getId(), is(1));
//        assertThat(third.getUser().getId(), is(0));

    }
    public void whenAddAmountThanUserHaveIt(){
        User u1 = new User();
        int id1 = u1.getId();
        User u2 = new User();
        int id2 = u2.getId();

    }

}