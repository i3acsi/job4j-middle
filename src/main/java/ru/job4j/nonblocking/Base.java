package ru.job4j.nonblocking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.jcip.annotations.Immutable;

@EqualsAndHashCode
@Getter
@Immutable
class Base {
    private final int id;

    private final int version;

    private final String name;

    public Base(final int id, final String name){
        this.id = id;
        this.version = 0;
        this.name = name;
    }

    private Base(final int id, int version, final String name){
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public static Base changeName(Base model, String name) {
        return new Base(model.id, model.version + 1, name);
    }
}