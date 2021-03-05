package ru.job4j.hibernate.hql.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "j_vacancy_base")
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@ToString(of = {"id", "name", "vacancies"})
@Data
public class VacancyBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacancy_base_id")
    private Long id;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "vacancyBase", fetch = FetchType.LAZY)
    private List<Vacancy> vacancies = new ArrayList<>();
}