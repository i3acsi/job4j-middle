package ru.job4j.hibernate;

import com.sun.istack.Nullable;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Car car = Car.of("ZIL", Timestamp.valueOf(LocalDateTime.now()), "Sidorov Ivan", 150, 12);
            session.save(car);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

@Entity
@Table(name = "cars")
@Data
class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String model;

    @CreationTimestamp
    private Timestamp created;

    private String owner;

    private int power;

    @Nullable
    private Integer serial;

    public static Car of(String model, Timestamp created, String owner, int power, int serial) {
        Car car = new Car();
        car.model = model;
        car.created = created;
        car.owner = owner;
        car.power = power;
        car.serial = serial;
        return car;
    }
}
