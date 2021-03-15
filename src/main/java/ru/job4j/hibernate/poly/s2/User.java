package ru.job4j.hibernate.poly.s2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_users")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}
