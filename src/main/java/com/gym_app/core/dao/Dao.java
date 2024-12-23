package com.gym_app.core.dao;


import java.util.List;
import java.util.Optional;

public interface Dao<T, Id> {

    Optional<T> getById(Id id);

    List<T> getAll();

    T save(T t);

    void update(T oldEntity, T updatedEntity);

    void delete(T t);
}
