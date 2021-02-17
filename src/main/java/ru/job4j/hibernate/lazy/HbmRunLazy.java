package ru.job4j.hibernate.lazy;

import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRunLazy {
    public static void main(String[] args) {
        List<Mark> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernateLazyinitException.cfg.xml").build();

        System.out.println("LazyInitializationException prevention with HIBERNATE_SESSION SCOPE:");
        try (Session session = new MetadataSources(registry).buildMetadata().buildSessionFactory().openSession()) {
            session.beginTransaction();
            Mark one = Mark.of("TOYOTA");
            Model model1 = Model.of("RAV4", one);
            Model model2 = Model.of("COROLLA", one);
            Model model3 = Model.of("LAND CRUISER", one);
            Model model4 = Model.of("CAMRY", one);
            Model model5 = Model.of("HIGHLANDER", one);

            List<Model> models = List.of(model1, model2, model3, model4, model5);
            one.setModels(models);
            session.persist(one);
            session.persist(model1);
            session.persist(model2);
            session.persist(model3);
            session.persist(model4);
            session.persist(model5);

            list = session.createQuery("from Mark").list();
            for (Mark mark : list) {
                for (Model model : mark.getModels()) {
                    System.out.println(model);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("LazyInitializationException prevention with \" join fetch \" :");
        try (Session session = new MetadataSources(registry).buildMetadata().buildSessionFactory().openSession()) {
            session.beginTransaction();
            list = session.createQuery(
                    "select distinct m from Mark m join fetch m.models"
            ).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Mark mark : list) {
            for (Model model : mark.getModels()) {
                System.out.println(model);
            }
        }
    }
}