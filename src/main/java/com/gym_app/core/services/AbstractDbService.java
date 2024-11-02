package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.UserJpaDao;
import com.gym_app.core.dto.common.User;
import com.gym_app.core.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractDbService<T extends User> {

    protected abstract JpaDao<T, Long> getDao();
    @Autowired
    private UserJpaDao userJpaDao;

    // New abstract method to get the type name
    protected abstract String getTypeName();

    public T create(T user) {
        user.setUserName(generate(user.getFirstName(), user.getLastName()));
        user.setPassword(PasswordGenerator.createPassword(10));
        try {
            return getDao().save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create new " + getTypeName() + ": " + user.getUserName());
        }
    }

    public boolean delete(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for " + getTypeName() + " with username: " + username);
        }
        try {
            getDao().deleteByUserName(username);
        }catch (RuntimeException exception){
            throw new RuntimeException(getTypeName() + " with username " + username + " not found.");
        }
        return true;
    }


    public Optional<T> selectByUsername(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for " + getTypeName() + " with username: " + username);
        }
        try {
            return getDao().getByUserName(username);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to find " + getTypeName() + " with username " + username);
        }
    }

    public boolean changePassword(String newPassword, String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for " + getTypeName() + " with username: " + username);
        }

        try {
            Optional<T> user = getDao().getByUserName(username);
                getDao().updatePassword(user.get(), newPassword);
                return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to change password for " + getTypeName() + ": " + username, e);
        }
    }

    public void changeStatus(T user, String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for " + getTypeName() + " with username: " + username);
        }
        try {
            getDao().changeStatus(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to change active/inactive status of " + getTypeName() + " with id: " + user.getUserName());
        }
    }

    public void update(T oldEntity, String username, String password, String[] updates) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for " + getTypeName() + " with username: " + username);
        }
        try {
            getDao().update(oldEntity, updateUser(oldEntity, updates));
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update " + getTypeName() + " with id: " + oldEntity.getUserName());
        }
    }


    private String generate(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        String userName = baseUserName;
        int serialNumber = 1;

        while (userJpaDao.getByUserName(userName).isPresent()) {
            userName = baseUserName + serialNumber;
            serialNumber++;
        }
        return userName;
    }

    public boolean authenticate(String username, String password) {
        Optional<User> userOpt = userJpaDao.getByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    protected abstract T updateUser(T user, String[] updates);
}
