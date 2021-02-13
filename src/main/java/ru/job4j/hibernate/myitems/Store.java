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
        List<MyItem> result = null;
        try (Session session = sf.openSession()) {
            if (!isDone) {
                result = session.createQuery("FROM " + MyItem.class.getSimpleName()).getResultList();
            } else {
                result = session.createQuery("FROM " + MyItem.class.getSimpleName() + " WHERE done = false ").getResultList();
            }
        }
        return result;
    }

    public boolean add(MyItem item) {
        boolean res = false;
        try (Session session = sf.openSession()) {
            int id = (int) session.save(item);
            res = id > 0;
        }
        return res;
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
