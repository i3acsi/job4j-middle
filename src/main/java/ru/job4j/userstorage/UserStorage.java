package ru.job4j.userstorage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public class UserStorage {
    @GuardedBy("UserStorage.class")
    private static UserStorage impl;
    @GuardedBy("UserStorage.class")
    private static ConcurrentMap<Integer, User> users;

    public static UserStorage instance() {
        if (impl == null) {
            synchronized (UserStorage.class) {
                if (impl == null) {
                    return new UserStorage();
                }
            }
        }
        return impl;
    }

    private UserStorage() {
        users = new ConcurrentHashMap<>();
    }


    public boolean addUser(User user) {
        synchronized (user) {
            boolean[] result = new boolean[1];
            users.computeIfAbsent(user.getId(), k -> {
                result[0] = true;
                return user;
            });
            return result[0];
        }
    }

    public boolean update(User user) {
        synchronized (user) {
            boolean[] result = new boolean[1];
            users.computeIfPresent(user.getId(), (k, v) -> {
                result[0] = true;
                v = user;
                return v;
            });
            return result[0];
        }
    }

    public boolean delete(User user) {
        synchronized (user) {
            return users.remove(user.getId()) != null;
        }
    }

    public User getById(int id) {
        return users.get(id);
    }

    public boolean transfer(int fromId, int toId, int amount) {
        return false;
    }
}
