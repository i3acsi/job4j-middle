package ru.job4j.userstorage;

import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@ThreadSafe
public class UserStorage {
    private static volatile UserStorage impl;
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
        synchronized (user) { // м.б. в качестве монитора использовать Integer.valueOf(user.getId()) ?? вместо user
            return (users.putIfAbsent(user.getId(),
                    new User(user.getId(), user.getAmount()))) == null;
        }
    }

    public boolean update(User user) {
        User tmp;
        synchronized ((tmp = users.get(user.getId())) != null ? tmp : user) { // Integer.valueOf(user.getId()) или
            return users.replace(user.getId(),
                    new User(user.getId(), user.getAmount())) != null;
        }
    }

    public boolean delete(User user) {
        User tmp;
        synchronized ((tmp = users.get(user.getId())) != null ? tmp : user) {
            return users.remove(user.getId()) != null;
        }
    }

    public User getById(int id) {
        User tmp;
        synchronized ((tmp = users.get(id)) != null ? tmp : new Object()) {
            if (tmp != null) {
                return new User(id, tmp.getAmount());
            } else {
                return null;
            }
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        User from = users.get(fromId);
        User to = users.get(toId);
        boolean result = false;
        if (from != null && to != null && from.getAmount() >= amount) {
            synchronized ((from = users.get(fromId)) != null ? from : new Object()) {
                synchronized ((to = users.get(toId)) != null ? to : new Object()) {
                    if (from != null && to != null && from.getAmount() >= amount) {
                        from.setAmount(from.getAmount() - amount);
                        to.setAmount(to.getAmount() + amount);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public void clear() {
        users.clear();
    }

    public Stream<User> findAll() {
        return List.copyOf(users.values()).stream();
    }
}