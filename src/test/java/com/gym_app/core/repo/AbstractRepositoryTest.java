package com.gym_app.core.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractRepositoryTest {

    private AbstractRepository<Integer, String> repository;

    // Concrete subclass for testing
    private static class TestRepository extends AbstractRepository<Integer, String> {}

    @BeforeEach
    public void setUp() {
        repository = new TestRepository();
    }

    @Test
    public void testPutAndGet() {
        repository.put(1, "Value1");
        assertEquals("Value1", repository.get(1));
    }

    @Test
    public void testSize() {
        repository.put(1, "Value1");
        repository.put(2, "Value2");
        assertEquals(2, repository.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(repository.isEmpty());
        repository.put(1, "Value1");
        assertFalse(repository.isEmpty());
    }

    @Test
    public void testContainsKey() {
        repository.put(1, "Value1");
        assertTrue(repository.containsKey(1));
        assertFalse(repository.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        repository.put(1, "Value1");
        assertTrue(repository.containsValue("Value1"));
        assertFalse(repository.containsValue("Value2"));
    }

    @Test
    public void testRemove() {
        repository.put(1, "Value1");
        assertEquals("Value1", repository.remove(1));
        assertNull(repository.get(1));
    }

    @Test
    public void testClear() {
        repository.put(1, "Value1");
        repository.put(2, "Value2");
        repository.clear();
        assertTrue(repository.isEmpty());
    }

    @Test
    public void testKeySet() {
        repository.put(1, "Value1");
        repository.put(2, "Value2");
        Set<Integer> keys = repository.keySet();
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
    }

    @Test
    public void testValues() {
        repository.put(1, "Value1");
        repository.put(2, "Value2");
        assertTrue(repository.values().contains("Value1"));
        assertTrue(repository.values().contains("Value2"));
    }

    @Test
    public void testEntrySet() {
        repository.put(1, "Value1");
        repository.put(2, "Value2");
        Set<Map.Entry<Integer, String>> entries = repository.entrySet();
        assertEquals(2, entries.size());
    }
}
