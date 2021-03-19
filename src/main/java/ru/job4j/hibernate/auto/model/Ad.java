package ru.job4j.hibernate.auto.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "test_ad")
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode()
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "ad_id")
    private Long id;

    @NonNull
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account owner;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="photos", joinColumns=@JoinColumn(name="ad_id"))
    @Column(name="photo")
    private Set<String> photos = new HashSet<>();
}
