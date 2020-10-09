package ru.job4j.nonblocking;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ThreadSafe
@Immutable
public class NonBlockingCache<T extends Base> {
    private final Map<Integer, T> map;

    public NonBlockingCache() {
        this.map = new ConcurrentHashMap<>();
    }

    public boolean add(final T model) {
        return map.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(final T newModel) throws OptimisticException{
        return map.computeIfPresent(newModel.getId(), (k, oldModel) -> {
            if (newModel.getVersion() <= oldModel.getVersion()) {
                throw new OptimisticException("model version conflict");
            }
            return newModel;
        }) == newModel;
    }

    public boolean delete(T model) {
        return map.remove(model.getId()) != null;
    }

    Stream<T> findAll(){
        return map.values().stream();
    }
}
