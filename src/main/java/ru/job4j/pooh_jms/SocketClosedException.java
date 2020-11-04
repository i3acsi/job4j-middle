package ru.job4j.pooh_jms;

public class SocketClosedException extends RuntimeException {
    private String msg;

    SocketClosedException(String load) {
        super("", null, false, false);
        this.msg = load;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
