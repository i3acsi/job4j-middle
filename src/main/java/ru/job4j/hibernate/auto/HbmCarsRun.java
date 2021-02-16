package ru.job4j.hibernate.auto;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hibernate.auto.model.Mark;
import ru.job4j.hibernate.auto.model.Model;

public class HbmCarsRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate2.cfg.xml").build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model rav4 = Model.of("rav4");
            Model corolla = Model.of("corolla");
            Model landcruiser = Model.of("land cruiser");
            Model camry = Model.of("camry");
            Model highlander = Model.of("highlander");

            Mark toyota = Mark.of("toyota");

            toyota.getModels().add(rav4);
            toyota.getModels().add(corolla);
            toyota.getModels().add(landcruiser);
            toyota.getModels().add(camry);
            toyota.getModels().add(highlander);

            session.save(toyota);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
