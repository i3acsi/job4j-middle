package ru.job4j.hibernate.hql.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table (name = "j_vacancy")
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@ToString(of = {"id", "name", "description"})
@Data
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_base_id")
    private VacancyBase vacancyBase;

    @NonNull
    private String name;

    @NonNull
    private String description;
}
