package ru.job4j.userstorage;


import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserStorageTest {
    private static final AtomicInteger index = new AtomicInteger(0);
    private static UserStorage userStorage;
    private static User user999;

    static {
        userStorage = UserStorage.instance();
        initUsers();
    }

    private static void initUsers() {
        for (int i = 0; i < 1000; i++) {
            User u = new User(index.getAndIncrement(), 0);
            assertTrue(userStorage.addUser(u));
            if (u.getId() == 999) {
                user999 = u;
            }
        }
    }

    @Test
    public void addUserTest() {
        assertNotNull(userStorage.getById(999));
        assertNull(userStorage.getById(1000));
        assertThat(userStorage.getById(999), is(user999));
        assertFalse(userStorage.addUser(new User(1, 500)));
        assertThat(userStorage.getById(1).getAmount(), is(0));
        assertTrue(userStorage.addUser(new User(1000, 0)));
        assertNotNull(userStorage.getById(1000));

    }

    @Test
    public void updateUserTest() {
        user999.setAmount(100);
        assertThat(userStorage.getById(999).getAmount(), is(0));
        assertTrue(userStorage.update(user999));
        assertThat(userStorage.getById(999).getAmount(), is(100));
        assertFalse(userStorage.update(new User(20000, 99)));
    }

    @Test
    public void deleteUserTest() {
        int i = index.incrementAndGet();
        User u1 = new User(i, 0);
        int before = (int) userStorage.findAll().count();
        assertTrue(userStorage.addUser(u1));
        int after = (int) userStorage.findAll().count();
        assertThat(after - before, is(1));
        assertEquals(u1, userStorage.getById(i));
        assertTrue(userStorage.delete(u1));
        before = after;
        after = (int) userStorage.findAll().count();
        assertThat(after - before, is(-1));
        assertFalse(userStorage.delete(new User(20000, 99)));


    }

    @Test
    public void transferToTest() {
        userStorage.clear();
        index.set(0);
        User u1 = new User(0,0);
        User u2 = new User(1, 0);
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
        assertFalse(userStorage.transfer(20,30, 100));

        userStorage.clear();
        initUsers();
    }
}