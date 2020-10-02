package ru.job4j.userstorage;

import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@ThreadSafe
public class UserStorage {

    private static UserStorage impl;
    private static ConcurrentMap<Integer, User> users;

    public static UserStorage instance() {
        if (impl == null) {
            synchronized (UserStorage.class) {
                if (impl == null) {
                    impl = new UserStorage();
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
                return User.of(user);
            });
            return result[0];
        }
    }

    public boolean update(User user) {
        boolean[] result = new boolean[1];
        if (users.get(user.getId()) != null) {
            synchronized (users.get(user.getId())) {
                users.computeIfPresent(user.getId(), (k, v) -> {
                    result[0] = true;
                    v.setAmount(user.getAmount());
                    return v;
                });
            }
        }
        return result[0];
    }

    public boolean delete(User user) {
        boolean result = false;
        if (users.get(user.getId()) != null)
            synchronized (users.get(user.getId())) {
                result = users.remove(user.getId()) != null;
            }
        return result;
    }

    public User getById(int id) {
        return User.of(users.get(id));
    }

    public boolean transfer(int fromId, int toId, int amount) {
        User from = users.get(fromId);
        User to = users.get(toId);
        boolean result = false;
        if (from != null && to != null) {
            synchronized (users.get(fromId)) {
                from = users.get(fromId);
                if (from != null && from.getAmount() >= amount) {
                    from.setAmount(from.getAmount() - amount);
                    result = true;
                }
            }
            synchronized (users.get(toId)) {
                to = users.get(toId);
                if (to != null && result) {
                    to.setAmount(to.getAmount() + amount);
                }
            }

        }
        return result;
    }

    public static void clear() {
        users.clear();
        User.clear();
    }

    public Stream<User> findAll() {
        return List.copyOf(users.values()).stream();
    }
}
