package ru.job4j.hibernate.poly.s3;

import lombok.Data;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;

@Entity
@Data
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "BD_TYPE")
@DiscriminatorFormula("CASE WHEN CARD_NUMBER IS NOT NULL THEN 'HU' ELSE 'HB' END")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
}
