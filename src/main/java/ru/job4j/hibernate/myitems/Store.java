package ru.job4j.hibernate.myitems;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Function;

public class Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @SuppressWarnings("unchecked")
    public List<MyItem> findAll(boolean isDone) {
        return tx(session -> {
            if (!isDone) {
                return session.createQuery("FROM " + MyItem.class.getSimpleName() + " i ORDER BY i.id ASC").getResultList();
            } else {
                return  session.createQuery("FROM " + MyItem.class.getSimpleName() + " i  WHERE done = false ORDER BY i.id ASC").getResultList();
            }
        });
    }

    public boolean add(MyItem item) {
        return tx(session -> ((int) session.save(item))>0);
    }

    public boolean makeDone(int id) {
        return tx(session -> {
            Query query = session.createQuery("UPDATE " + MyItem.class.getSimpleName() + " SET done = true WHERE id = :paramId");
            query.setParameter("paramId", id);
            return query.executeUpdate() > 0;
        });
    }

    private <T> T tx(final Function<Session, T> command) {
        T result = null;
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            result = command.apply(session);
            tx.commit();
        } catch (final Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
        return result;
    }
}
