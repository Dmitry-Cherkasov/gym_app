package com.gym_app.core.repo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractRepository<K, V> {
    protected final Map<K, V> repository;

    public AbstractRepository() {
        this.repository = new HashMap<>();
    }

    public int size() {
        return repository.size();
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }

    public boolean containsKey(K key) {
        return repository.containsKey(key);
    }

    public boolean containsValue(V value) {
        return repository.containsValue(value);
    }

    public V get(K key) {
        return repository.get(key);
    }

    public V put(K key, V value) {
        return repository.put(key, value);
    }

    public V remove(K key) {
        return repository.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        repository.putAll(m);
    }

    public void clear() {
        repository.clear();
    }

    public Set<K> keySet() {
        return repository.keySet();
    }

    public Collection<V> values() {
        return repository.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return repository.entrySet();
    }
}
