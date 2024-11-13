package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.UserJpaDao;
import com.gym_app.core.dto.common.User;
import com.gym_app.core.util.PasswordGenerator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractDbService<T extends User> {

    protected abstract JpaDao<T, Long> getDao();
    @Autowired
    private UserJpaDao userJpaDao;

    @Autowired
    private MeterRegistry meterRegistry;
    protected final AtomicInteger userTotalCounter;
    private Counter failedAuthenticationCounter;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AbstractDbService() {
        this.userTotalCounter = new AtomicInteger(0);
        this.failedAuthenticationCounter = null;
    }

    @PostConstruct
    public void init() {
        this.userTotalCounter.set(getDao().getAll().size());
        this.failedAuthenticationCounter = meterRegistry.counter("gym.authentication.fails");
        Gauge.builder("gym." + getTypeName() + "s.total", userTotalCounter, AtomicInteger::get)
                .description("Total " + getTypeName() + "s count")
                .register(meterRegistry);
    }

    protected abstract String getTypeName();

    public T create(T user) {
        user.setUserName(generate(user.getFirstName(), user.getLastName()));
        String generatedPassword = PasswordGenerator.createPassword(10);
        user.setPassword(PasswordGenerator.hashPassword(generatedPassword));
        try {
            userTotalCounter.getAndIncrement();
            getDao().save(user);
            user.setPassword(generatedPassword);
            return user;
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
            userTotalCounter.decrementAndGet();
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
                getDao().updatePassword(user.get(), PasswordGenerator.hashPassword(newPassword));
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
            System.out.println("Password: " + user.getPassword());// to DELETE
            // Use BCrypt to compare the provided password with the stored hashed password
            return passwordEncoder.matches(password, user.getPassword());
        }
        failedAuthenticationCounter.increment();
        return false;
    }

    protected abstract T updateUser(T user, String[] updates);

    public AtomicInteger getUserTotalCounter() {
        return userTotalCounter;
    }

}
