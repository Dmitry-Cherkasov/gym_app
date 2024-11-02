package com.gym_app.core.dao;

import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public class TrainerJpaDaoImpl extends JpaDao<Trainer, Long> {

    public TrainerJpaDaoImpl(){
        super(Trainer.class);
    }

    public List<Training> getTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName, TrainingType trainingType) {
        String query = "SELECT t FROM Training t WHERE 1=1"
                + (username != null && !username.isEmpty() ? " AND t.trainer.user.userName = :username" : "")
                + (fromDate != null ? " AND t.trainingDate >= :fromDate" : "")
                + (toDate != null ? " AND t.trainingDate <= :toDate" : "")
                + (traineeName != null && !traineeName.isEmpty() ? " AND t.trainee.user.userName LIKE :traineeName" : "")
                + (trainingType != null ? " AND t.trainingType = :trainingType" : "");

        TypedQuery<Training> typedQuery = getEntityManager().createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("traineeName",  traineeName);
        typedQuery.setParameter("trainingType", trainingType);

        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query: " + typedQuery, e);
        }

    }
}
