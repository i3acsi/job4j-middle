package ru.job4j.hibernate.lazy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "lazy_models")
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name", "mark"})
@Data
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "mark_id")
    private Mark mark;

    public static Model of(String name, Mark mark) {
        Model model = new Model();
        model.name = name;
        model.mark = mark;
        return model;
    }
}