package ru.job4j.hibernate.manytomany;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "books")
@EqualsAndHashCode(of = {"id"})
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    public static Book of(String name) {
        Book book = new Book();
        book.name = name;
        return book;
    }
}