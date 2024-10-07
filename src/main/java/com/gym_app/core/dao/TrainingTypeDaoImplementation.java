package com.gym_app.core.dao;

import com.gym_app.core.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class TrainingTypeDaoImplementation implements TrainingTypeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrainingTypeDaoImplementation(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Optional<TrainingType> getById(Long id) {
        return Optional.ofNullable(jdbcTemplate.
                queryForObject(BY_ID, (rs, rowNum) -> TrainingType.valueOf(rs.getString("TRAINING_TYPE_NAME")), id)
        );

    }

    @Override
    public List<TrainingType> getAll() {
        return jdbcTemplate.query(ALL, new RowMapper<>() {
            @Override
            public TrainingType mapRow(ResultSet rs, int rowNum) throws SQLException {
                return TrainingType.valueOf(rs.getString("TRAINING_TYPE_NAME"));
            }
        });
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return null;
    }

    @Override
    public void delete(TrainingType trainingType) {
    }
}

