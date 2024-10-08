package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class TrainerDBDaoImplementation implements TrainerDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TrainerDBDaoImplementation(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Trainer> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Trainer> getAll() {
        return List.of();
    }

    @Override
    public Trainer save(Trainer trainer) {
        return null;
    }

    @Override
    public void delete(Trainer trainer) {

    }

    @Override
    public void update(Trainer trainer, String[] params) {

    }
}
