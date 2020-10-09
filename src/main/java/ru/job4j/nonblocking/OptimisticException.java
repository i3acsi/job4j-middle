package ru.job4j.nonblocking;

public class OptimisticException extends RuntimeException {
    private String msg;

    OptimisticException(String load) {
        super("", null, false, false);
        this.msg = load;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}