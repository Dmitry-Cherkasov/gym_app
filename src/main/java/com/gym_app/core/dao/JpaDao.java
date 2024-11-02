package com.gym_app.core.dao;

import com.gym_app.core.dto.common.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public abstract class JpaDao<T extends User, V> implements Dao<T, V> {
    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> entityClass;

    protected JpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> getById(V id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    public Optional<T> getByUserName(String userName) {
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.userName = :userName";
        try {
            T entity = entityManager.createQuery(query, entityClass)
                    .setParameter("userName", userName)
                    .getSingleResult();
            return Optional.ofNullable(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> getAll() {
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(query, entityClass).getResultList();
    }

    @Override
    public T save(T t) {
        try {
          if (t.getId() != null) {
                return entityManager.merge(t);
            } else {
                entityManager.persist(t);
                return getByUserName(t.getUserName()).get();
            }
        } catch (Exception e) {

            throw new RuntimeException("Error saving " + t.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updatePassword(T t, String newPassword) {
        if (newPassword == null || newPassword.isBlank())
            throw new RuntimeException("Password should be not null or empty");
        t.setPassword(newPassword);
        save(t);
    }

    @Override
    public void delete(T t) {
        removeEntity(t);
    }

    public void deleteByUserName(String userName) {
        Optional<T> optionalEntity = getByUserName(userName);
        if (optionalEntity.isPresent()) {
            T entity = optionalEntity.get();
            removeEntity(entity);
        } else {
            throw new RuntimeException("No such entity in persistence with username: " + userName);
        }
    }

    @Override
    public void update(T oldEntity, T updatedEntity) {
        if(entityManager.contains(oldEntity)){
            updatedEntity.setId(oldEntity.getId());
            entityManager.merge(updatedEntity);
        }else {
            throw new RuntimeException("No such entity in persistence with ID: " + oldEntity.getId());
        }
    }

    private void removeEntity(T entity) {
        try {
            if (entityManager.contains(entity)) {
                entityManager.remove(entity);
            } else {
                entityManager.remove(entityManager.merge(entity)); // Remove if detached
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity: " + entity);
        }
    }

    public void changeStatus(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }
        entity.setActive(!entity.isActive());

        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error changing activation status for " + entity.getClass() + " " + entity.getId(), e);
        }
    }
}
