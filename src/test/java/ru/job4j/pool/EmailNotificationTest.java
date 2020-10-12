package ru.job4j.pool;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailNotificationTest {
    @Test
    public void emailNotificationTest() {
        EmailNotification emailNotification = new EmailNotification();
        User user1 = new User("user1", "user1@mail.com");
        User user2 = new User("user2", "user2@mail.com");
        User user3 = new User("user3", "user3@mail.com");
        User user4 = new User("user4", "user4@mail.com");
        emailNotification.emailTo(user1);
        emailNotification.emailTo(user2);
        emailNotification.emailTo(user3);
        emailNotification.emailTo(user4);
        emailNotification.close();
    }

}