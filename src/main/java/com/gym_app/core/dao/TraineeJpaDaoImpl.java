package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Repository
public class TraineeJpaDaoImpl extends JpaDao<Trainee, Long> {

    public TraineeJpaDaoImpl() {
        super(Trainee.class);
    }

    public List<Training> getTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        String query = "SELECT t FROM Training t WHERE 1=1"
                + (username != null && !username.isEmpty() ? " AND t.trainee.user.userName = :username" : "")
                + (fromDate != null ? " AND t.trainingDate >= :fromDate" : "")
                + (toDate != null ? " AND t.trainingDate <= :toDate" : "")
                + (trainerName != null ? " AND t.trainer.user.userName = :trainerName" : "")
                + (trainingType != null ? " AND t.trainingType = :trainingType" : "");

        TypedQuery<Training> typedQuery = getEntityManager().createQuery(query, Training.class);

        typedQuery.setParameter("username", username);
        typedQuery.setParameter("fromDate", fromDate);
        typedQuery.setParameter("toDate", toDate);
        typedQuery.setParameter("trainerName", trainerName);
        typedQuery.setParameter("trainingType", trainingType);
        try {
            System.out.println("REsult:  " + typedQuery.getResultList().toString());
        } catch (Exception e) {
            throw new RuntimeException("Query fails " + e, e);
        }
        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query: " + typedQuery, e);
        }

    }

    public List<Trainer> getAvailableTrainers(String traineeUsername) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
        Root<Trainer> trainerRoot = query.from(Trainer.class);

        // Subquery to find trainers assigned to the specified trainee
        Subquery<Trainer> subquery = query.subquery(Trainer.class);
        Root<Training> trainingRoot = subquery.from(Training.class);
        subquery.select(trainingRoot.get("trainer"))
                .where(cb.equal(trainingRoot.get("trainee").get("userName"), traineeUsername));

        // Main query
        query.select(trainerRoot)
                .where(cb.not(trainerRoot.in(subquery)));

        return getEntityManager().createQuery(query).getResultList();
    }
}

