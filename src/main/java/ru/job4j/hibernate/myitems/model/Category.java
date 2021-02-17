package ru.job4j.hibernate.myitems.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "j_category")
@EqualsAndHashCode(of = {"id"})
@Data
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cat_id")
    private int id;

    @Column (name = "cat_name", unique = true)
    private String name;

    public static Category of(String name) {
        Category category = new Category();
        category.name = name;
        return category;
    }
}
