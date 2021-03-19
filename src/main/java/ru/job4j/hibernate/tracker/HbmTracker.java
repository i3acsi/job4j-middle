package ru.job4j.hibernate.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import ru.job4j.hibernate.Item;

import java.util.List;
import java.util.function.Consumer;

@Component
public class HbmTracker implements ITracker, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        Integer id = (int) session.save(item);
        item.setId(id);
        session.close();
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        int result;
        try (Session session = sf.openSession()) {
            session.getTransaction().begin();
            Query query = session.createQuery("UPDATE " + Item.class.getSimpleName() + " SET task = :paramTask, description = :paramDesc, comment = :paramComment WHERE id = :paramId");
            query.setParameter("paramTask", item.getTask());
            query.setParameter("paramDesc", item.getDescription());
            query.setParameter("paramComment", item.getComment());
            query.setParameter("paramId", Integer.parseInt(id));
            result = query.executeUpdate();
            session.getTransaction().commit();
        }
        return result == 1;
    }

    @Override
    public boolean delete(String id) {
        int result;
        try (Session session = sf.openSession()) {
            session.getTransaction().begin();
            Query query = session.createQuery("DELETE FROM " + Item.class.getSimpleName() + " WHERE id = :paramId");
            query.setParameter("paramId", Integer.parseInt(id));
            result = query.executeUpdate();
            session.getTransaction().commit();
        }
        return result == 1;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        List<Item> result = session.createQuery("FROM " + Item.class.getSimpleName()).getResultList();
        session.close();
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        Session session = sf.openSession();
        String hql = "FROM " + Item.class.getSimpleName() + " WHERE task = :paramName";
        Query query = session.createQuery(hql);
        query.setParameter("paramName", key);
        List result = query.list();
        session.close();
        return result;
    }

    @Override
    public Item findById(String id) {
        Session session = sf.openSession();
        Item result = session.get(Item.class, Integer.parseInt(id));
        session.close();
        return result;
    }

    @Override
    public void clean() {
        Session session = sf.openSession();
        session.createQuery("DELETE FROM " + Item.class.getSimpleName()).executeUpdate();
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}

class Main extends StartUI {
    /**
     * Конструктор с инициалицацией полей.
     *
     * @param input   ввод данных.
     * @param tracker хранилище заявок.
     * @param output
     */
    public Main(Input input, ITracker tracker, Consumer<String> output) {
        super(input, tracker, output);
    }

    public static void main(String[] args) {
        HbmTracker tracker = null;
        try {
            tracker = new HbmTracker();
            new Main(new ValidateInput(
                    new ConsoleInput()),
                    tracker,
                    System.out::println).
                    init();
        } finally {
            try {
                if (tracker != null)
                    tracker.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
