package ru.job4j.hibernate.poly.s2;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "h_books")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
