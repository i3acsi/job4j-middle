package ru.job4j.hibernate.manytomany;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@EqualsAndHashCode(of = {"id"})
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Book> books = new ArrayList<>();

    public static Author of(String name) {
        Author author = new Author();
        author.name = name;
        return author;
    }
}