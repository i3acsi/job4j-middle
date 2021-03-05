package ru.job4j.hibernate.hql.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table (name = "j_candidate",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})})
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString(of = {"id", "name", "experience", "salary"})
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;
    @NonNull
    @Column (name = "name")
    private String name;
    @NonNull
    private Long experience;
    @NonNull
    private Long salary;

}
