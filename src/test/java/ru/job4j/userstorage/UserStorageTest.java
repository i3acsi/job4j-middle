package ru.job4j.userstorage;


import org.junit.After;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserStorageTest {
    private static UserStorage userStorage;
    static {
        userStorage = UserStorage.instance();
    }

    @After
    public void clear(){
        UserStorage.clear();
    }

    @Test
    public void addUserTest() throws InterruptedException {
        User[] tmp = new User[1];
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                User u = new User();
                if (u.getId() == 999) {
                    tmp[0] = u;
                }
                userStorage.addUser(u);
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        assertNotNull(userStorage.getById(999));
        assertNull(userStorage.getById(1000));
        assertThat(userStorage.getById(999), is(tmp[0]));
    }

    @Test
    public void updateUserTest() throws InterruptedException {
        User[] tmp = new User[1];
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                User u = new User();
                if (u.getId() == 999) {
                    tmp[0] = u;
                }
                userStorage.addUser(u);
            });

        }
        for (Thread t : threads) {
            t.start();
            t.join();
        }
        tmp[0].setAmount(100);
        assertThat(userStorage.getById(999).getAmount(), is(0));
        assertTrue(userStorage.update(tmp[0]));
        assertThat(userStorage.getById(999).getAmount(), is(100));
    }

    @Test
    public void deleteUserTest() {
        User u1 = new User();
        assertThat((int) userStorage.findAll().count(), is(0));
        assertTrue(userStorage.addUser(u1));
        assertThat((int) userStorage.findAll().count(), is(1));
        assertEquals(u1, userStorage.getById(0));
        assertTrue(userStorage.delete(u1));
        assertThat((int) userStorage.findAll().count(), is(0));
    }

    @Test
    public void transferToTest() {
        User u1 = new User();
        User u2 = new User();
        userStorage.addUser(u1);
        userStorage.addUser(u2);
        assertFalse(userStorage.transfer(0, 1, 100));
        u1.setAmount(500);
        assertTrue(userStorage.update(u1));
        assertTrue(userStorage.transfer(0, 1, 100));
        assertThat(userStorage.getById(0).getAmount(), is(400));
        assertThat(userStorage.getById(1).getAmount(), is(100));
        assertThat(u1.getAmount(), is(500));
        assertThat(u2.getAmount(), is(0));
    }
}