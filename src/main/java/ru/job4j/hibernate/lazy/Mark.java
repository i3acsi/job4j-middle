package ru.job4j.hibernate.lazy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lazy_marks")
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name"})
@Data
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "mark")
    private List<Model> models = new ArrayList<>();

    public static Mark of(String name) {
        Mark mark = new Mark();
        mark.name = name;
        return mark;
    }
}