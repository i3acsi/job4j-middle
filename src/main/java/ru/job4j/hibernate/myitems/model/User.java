package ru.job4j.hibernate.myitems.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "j_user")
@EqualsAndHashCode(of = {"id"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name", "email"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "user_id")
    private int id;

    @Column (name = "user_name")
    private String name;

    @Column (name = "user_email", unique = true)
    private String email;

    @Column (name = "user_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public static User of(String name, String email,String password,Role role) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.role = role;
        return user;
    }
}
