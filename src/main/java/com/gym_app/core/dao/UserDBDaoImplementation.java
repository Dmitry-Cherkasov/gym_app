package com.gym_app.core.dao;

import com.gym_app.core.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class UserDBDaoImplementation implements UserDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBDaoImplementation(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void update(User user, String[] params) {

    }
}
