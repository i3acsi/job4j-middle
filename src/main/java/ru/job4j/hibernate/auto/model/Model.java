package ru.job4j.hibernate.auto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "model")
@Data
@NoArgsConstructor
@EqualsAndHashCode()
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String name;

    public static Model of(String name){
        Model model = new Model();
        model.setName(name);
        return model;
    }
}
