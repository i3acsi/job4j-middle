package ru.job4j.hibernate.myitems.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.hibernate.myitems.model.Category;
import ru.job4j.hibernate.myitems.model.MyItem;
import ru.job4j.hibernate.myitems.model.Role;
import ru.job4j.hibernate.myitems.model.User;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public User save(User user) {
        String roleName = user.getRole().getName();
        Role role = tx(session -> {
            Query query = session.createQuery("FROM " + Role.class.getSimpleName() + " WHERE name=:name");
            query.setParameter("name", roleName);
            Role result = (Role) query.uniqueResult();
            if (result == null) {
                int roleId = (int) session.save(Role.of(roleName));
                result = Role.of(roleName);
                result.setId(roleId);
            }
            return result;
        });
        user.setRole(role);
        int id = create(user);
        user.setId(id);
        return user;
    }

    public User findUserByEmail(String email) {
        return tx(session -> {
            Query query = session.createQuery("FROM " + User.class.getSimpleName() + " WHERE email=:email");
            query.setParameter("email", email);
            return (User) query.uniqueResult();
        });
    }

    @SuppressWarnings("unchecked")
    public List<MyItem> findAll(boolean isDone) {
        return tx(session -> {
            if (!isDone) {
                return session.createQuery("SELECT DISTINCT i FROM MyItem i JOIN FETCH i.categories ORDER BY i.id").getResultList();
            } else {
                return session.createQuery("SELECT DISTINCT i FROM MyItem i JOIN FETCH i.categories WHERE i.done = false ORDER BY i.id").getResultList();
            }
        });
    }

    public boolean add(MyItem item) {
        return tx(session -> ((int) session.save(item)) > 0);
    }

    public boolean makeDone(int id) {
//        Date now = new Date(System.currentTimeMillis());
        return tx(session -> {
            MyItem item = session.get(MyItem.class, id);
            item.setDone(true);
            item.setUpdated(new Date(System.currentTimeMillis()));
            session.update(item);
            return true;
//            Query query = session.createQuery("UPDATE " + MyItem.class.getSimpleName() + " SET done = true WHERE id = :paramId");
//            query.setParameter("paramId", id);
//            return query.executeUpdate() > 0;
        });
    }

    public List<Category> findAllCategories() {
        return findAll(Category.class);
    }

    @SuppressWarnings("unchecked")
    public List<Category> findSelectedCategories(String ids) {
        String cats = ids.replaceAll("\\[", "(").replaceAll("\\]", ")").replaceAll("\"", "");
        return tx(session -> session.createQuery("from " + Category.class.getSimpleName() + " where cat_id in " + cats).list());
    }

    public void add(Category newCat) {
        create(newCat);
    }

    private <T> List<T> findAll(Class<T> cl) {
        return tx(session -> session.createQuery("from " + cl.getName(), cl).list());
    }

    private <T> T tx(final Function<Session, T> command) {
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

    private <T> int create(T model) {
        return tx(session ->
                (Integer) session.save(model)
        );
    }
}