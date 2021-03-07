package ru.job4j.hibernate.tracker;

import org.junit.Test;
import ru.job4j.hibernate.Item;

import java.util.List;

import static org.junit.Assert.*;

public class HbmTrackerTest {

    @Test
    public void whenAddItem() {
        HbmTracker tracker = new HbmTracker();
        int sizeBefore = tracker.findAll().size();
        String task = "addItemTask";
        String desc = "addItemDesc";
        Item item = new Item(task, desc);
        Item result = tracker.add(item);
        assertEquals(item, result);
        List<Item> items = tracker.findAll();
        assertEquals(result, items.get(sizeBefore));
        assertEquals(1, items.size() - sizeBefore);
    }

    @Test
    public void whenAddItemAndFindById() {
        HbmTracker tracker = new HbmTracker();
        int sizeBefore = tracker.findAll().size();
        String task = "findByIdTask";
        String desc = "findByIdDesc";
        Item item = tracker.add(new Item(task, desc));
        Item result = tracker.findById(item.getId().toString());
        assertEquals(item, result);
        List<Item> items = tracker.findAll();
        assertEquals(result, items.get(sizeBefore));
        assertEquals(1, items.size() - sizeBefore);
    }

    @Test
    public void whenAddItemAndFindByName() {
        HbmTracker tracker = new HbmTracker();
        int sizeBefore = tracker.findAll().size();
        String task = "findByNameTask";
        String desc = "findByNameDesc";
        Item item = tracker.add(new Item(task, desc));
        Item result = tracker.findByName(item.getTask()).get(0);

        assertEquals(item, result);
        List<Item> items = tracker.findAll();
        assertEquals(result, items.get(sizeBefore));
        assertEquals(1, items.size() - sizeBefore);
    }

    @Test
    public void whenAddItemAndDeleteById() {
        HbmTracker tracker = new HbmTracker();
        List<Item> before = tracker.findAll();
        String task = "deleteByIdTask";
        String desc = "deleteByIdDesc";
        Item item = tracker.add(new Item(task, desc));
        boolean result = tracker.delete(item.getId().toString());
        assertTrue(result);
        List<Item> items = tracker.findAll();
        assertEquals(before, items);
    }

    @Test
    public void whenAddItemAndReplace() {
        HbmTracker tracker = new HbmTracker();
        List<Item> before = tracker.findAll();
        String task = "replaceTask";
        String desc = "replaceDesc";
        Item item = tracker.add(new Item(task, desc));
        boolean result = tracker.replace(item.getId().toString(), new Item("0", "0"));
        assertTrue(result);
        List<Item> items = tracker.findAll();
        assertEquals(before.size() + 1, items.size());
        item = items.get(before.size());
        assertEquals( item.getTask(), "0");
        assertEquals( item.getDescription(), "0");
    }
}