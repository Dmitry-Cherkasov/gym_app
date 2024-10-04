package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;

import java.util.List;
import java.util.Optional;

public class TrainerDaoImplementation implements TrainerDao{
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
}
