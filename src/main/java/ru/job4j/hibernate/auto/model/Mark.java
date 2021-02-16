    package ru.job4j.hibernate.auto.model;

    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.NoArgsConstructor;

    import javax.persistence.*;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
@Table(name = "mark")
@Data
@NoArgsConstructor
@EqualsAndHashCode()
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models = new ArrayList<>();

    public static Mark of(String name){
        Mark mark = new Mark();
        mark.setName(name);
        return mark;
    }
}
