package ru.job4j.hibernate.myitems.service;

import org.apache.commons.codec.digest.DigestUtils;
import ru.job4j.hibernate.myitems.model.Role;
import ru.job4j.hibernate.myitems.model.User;

import javax.servlet.http.HttpServletRequest;

public class AuthService {
    private final String salt;
    private final Store store;

    public AuthService(Store store) {
        this.store = store;
        this.salt = "DEFAULT_SALT";
    }

    private String encodePassword(String password) {
        return DigestUtils.sha256Hex(password + salt);
    }

    public boolean checkAndSetCredentials(String email, String password, HttpServletRequest req) {
        User user = store.findUserByEmail(email);
        boolean result = false;
        if (user != null) {
            String passwordFromDB = user.getPassword();
            String passwordHash = encodePassword(password);
            if (passwordFromDB.equals(passwordHash)) {
                req.getSession().setAttribute("user", user);
                result = true;
            }
        }
        return result;
    }

    public boolean regAccount(String name, String email, String password) {
        String encodedPwd = encodePassword(password);
        Role roleUser = Role.of("USER");
        User user = store.save(User.of(name, email, encodedPwd, roleUser));
        System.out.println(user);
        return user.getId() > 0;
    }
}
