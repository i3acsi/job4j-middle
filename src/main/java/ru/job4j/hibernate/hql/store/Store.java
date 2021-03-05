package ru.job4j.hibernate.hql.store;

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
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure("hibernateHQL.cfg.xml").build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    Session openSession() {
        return sf.openSession();
    }

    <T> T tx(final Function<Session, T> command) {
        T result = null;
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            result = command.apply(session);
            tx.commit();
        } catch (final Exception e) {
            e.printStackTrace();
            if (tx != null)
                tx.rollback();
            throw e;
        }
        return result;
    }

    <T> void create(List<T> models) {
        tx(session -> {
            models.forEach(session::persist);
            return true;
        });
    }

    <T> List<T> findAll(Class<T> cl) {
        return tx(session -> session.createQuery("from " + cl.getName(), cl).list());
    }

    @SuppressWarnings("unchecked")
    <T> T findById(Long id, Class<T> cl) {
        return tx(session -> (T) session.get(cl.getName(), id));
    }

    @SuppressWarnings("unchecked")
    <T> T findBy(Object fieldValue, String fieldName, Class<T> cl) {
        return tx(session ->
                (T) session.createQuery("FROM " + cl.getSimpleName() + " WHERE " + fieldName + "=:field")
                        .setParameter("field", fieldValue).uniqueResult()
        );
    }

    <T> T update(T model) {
        return tx(session -> {
                    session.update(model);
                    return model;
                }
        );
    }

    <T> void delete(Long id, Class<T> cl) {
        tx(session -> {
                    String hql = "delete from " + cl.getSimpleName() + " where id = : fId";
                    Query q = session.createQuery(hql);
                    q.setParameter("fId", id);
                    q.executeUpdate();
                    return true;
                }
        );
    }
}
