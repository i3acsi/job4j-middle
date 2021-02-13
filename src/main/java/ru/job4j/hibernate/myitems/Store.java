package ru.job4j.hibernate.myitems;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public List<MyItem> findAll(boolean isDone) {
        Session session = sf.openSession();
        List<MyItem> result = null;
        if (!isDone) {
            result = session.createQuery("FROM " + MyItem.class.getSimpleName()).getResultList();
        } else {
            result = session.createQuery("FROM " + MyItem.class.getSimpleName() + " WHERE done = false ").getResultList();
        }
        session.close();
        return result;
    }

    public boolean add(MyItem item) {
        Session session = sf.openSession();
        System.out.println(item);
        int id = (int) session.save(item);
        System.out.println(id);
        session.close();
        return id > 0;
    }

    public boolean makeDone(int id) {
        int result;
        try (Session session = sf.openSession()) {
            session.getTransaction().begin();
            Query query = session.createQuery("UPDATE " + MyItem.class.getSimpleName() + " SET done = true WHERE id = :paramId");
            query.setParameter("paramId", id);
            result = query.executeUpdate();
            session.getTransaction().commit();
        }
        return result > 0;
    }
}
