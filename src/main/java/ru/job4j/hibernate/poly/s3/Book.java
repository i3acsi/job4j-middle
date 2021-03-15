package ru.job4j.hibernate.poly.s3;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("HA")
public class Book extends BaseEntity {
    @Column(name = "book_author")
    private String author;

    @Column(name = "book_pages")
    private Long pages;
}
