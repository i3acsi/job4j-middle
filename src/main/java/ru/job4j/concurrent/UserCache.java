package ru.job4j.concurrent;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ThreadSafe
@Immutable
public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(String userName) {
        users.put(id.incrementAndGet(), User.of(id.get(), userName));
    }

    public User findById(int id) {
        return User.of(id, users.get(id).getName());
    }

    public List<User> findAll() {
        return users.values().stream()
                .map(user -> User.of(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }
}
