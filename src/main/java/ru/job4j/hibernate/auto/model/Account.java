package ru.job4j.hibernate.auto.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "test_account")
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode()
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "account_id")
    private Long id;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    Set<Ad> adList = new HashSet<>();
}
