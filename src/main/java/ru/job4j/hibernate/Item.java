package ru.job4j.hibernate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trackitem")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    private String task;

    private String description;

    @CreationTimestamp
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    private String comment;

    public Item (String task, String description) {
        this.task = task;
        this.description = description;
        this.dateCreation = LocalDateTime.now();
        this.comment = "";
    }

    public Item(Item item) {
        this.task = item.getTask();
        this.description = item.getDescription();
        this.dateCreation = item.dateCreation;
        this.id = item.getId();
        this.comment = item.getComment();
    }



    public String show() {
        return "Task: " + this.task
                + ". Description: " + this.description
                + ". ID: " + this.id
                + ". Date: " + this.dateCreation
                + ". Comments: " + this.comment;
    }
}