package ru.job4j.hibernate.poly.s3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("HU")
public class User extends BaseEntity {
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_phone")
    private String phone;
}
