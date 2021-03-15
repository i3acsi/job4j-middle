package ru.job4j.hibernate.poly.s4;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
