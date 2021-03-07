package ru.job4j.hibernate.integration.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.hibernate.integration.entity.Order;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        int sizeBefore = store.findAll().size();
        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size() - sizeBefore, is(1));
        assertThat(all.get(sizeBefore).getDescription(), is("description1"));
        assertTrue(all.get(sizeBefore).getId() > 0);
    }

    @Test
    public void whenSaveOrderThanUpdateAndFindAllOneRowWithUpdatedDescription() {
        OrdersStore store = new OrdersStore(pool);
        int sizeBefore = store.findAll().size();

        Order o = store.save(Order.of("test_update", "test_update_desc"));

        o.setDescription("updated_desc");
        boolean result = store.update(o);

        assertTrue(result);
        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size() - sizeBefore, is(1));
        assertThat(all.get(sizeBefore).getDescription(), is("updated_desc"));
    }

    @Test
    public void whenSaveOrderAndFindItByName() {
        OrdersStore store = new OrdersStore(pool);
        Order one = store.save(Order.of("some_name", "simple_desc"));
        Order two = store.findByName("some_name");

        assertEquals(one, two);
    }

    @Test
    public void whenSaveOrderAndFindItById() {
        OrdersStore store = new OrdersStore(pool);
        Order one = store.save(Order.of("some_name1", "simple_desc1"));
        Order two = store.findById(one.getId());

        assertEquals(one, two);
    }
}