package ru.job4j.hibernate.myitems.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "j_role")
@EqualsAndHashCode(of = {"id"})
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name" , unique = true)
    private String name;

    public static Role of(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}