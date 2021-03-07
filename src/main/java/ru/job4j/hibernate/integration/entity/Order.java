package ru.job4j.hibernate.integration.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "j_order")
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name", "description", "created"})
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Order {

    private int id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    private Timestamp created = new Timestamp(System.currentTimeMillis());
}