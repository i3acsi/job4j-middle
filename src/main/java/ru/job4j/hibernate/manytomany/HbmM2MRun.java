package ru.job4j.hibernate.manytomany;

import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmM2MRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate2.cfg.xml").build();
        try (Session session = new MetadataSources(registry).buildMetadata().buildSessionFactory().openSession()){
            session.beginTransaction();

            Book one = Book.of("The twelve chairs");
            Book two = Book.of("The golden calf");

            Author first = Author.of("Ilf");
            first.getBooks().add(one);
            first.getBooks().add(two);

            Author second = Author.of("Petrov");
            second.getBooks().add(two);

            session.persist(first);
            session.persist(second);

            session.getTransaction().commit();

            session.beginTransaction();

            Author author = session.get(Author.class, 1);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}