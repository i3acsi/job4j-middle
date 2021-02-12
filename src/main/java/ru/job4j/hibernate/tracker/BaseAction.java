package ru.job4j.hibernate.tracker;

public abstract class BaseAction implements UserAction {
    private final int key;
    private final String info;

    protected BaseAction(final int key, final String info) {
        this.key = key;
        this.info = info;
    }

    @Override
    public int key() {
        return this.key;
    }

    @Override
    public String info() {
        return String.format("%s", this.info);
    }
}