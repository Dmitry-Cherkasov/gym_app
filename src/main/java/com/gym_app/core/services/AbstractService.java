package com.gym_app.core.services;

import com.gym_app.core.dao.Dao;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractService<T, Id> {

    protected abstract Dao<T, Id> getDao(); // This forces subclasses to provide their DAO

    public T create(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity argument cannot be null");
        }
        return getDao().save(entity);
    }

    public void update(T entity, String[] updates) {
        if (updates == null || updates.length == 0) {
            throw new IllegalArgumentException("Update array cannot be null or empty");
        } else if (entity == null) {
            throw new IllegalArgumentException("Entity argument cannot be null");
        }
        getDao().update(entity, updates);
    }

    public void delete(Id id) {
        Optional<T> entity = getDao().getById(id);
        if (entity.isPresent()) {
            getDao().delete(entity.get());
        } else {
            throw new NoSuchElementException("Entity with ID " + id + " not found");
        }
    }

    public Optional<T> select(Id id) {
        Optional<T> entity = getDao().getById(id);
        if (entity.isEmpty()) {
            throw new NoSuchElementException("Entity with ID " + id + " not found");
        }
        return entity;
    }
}
