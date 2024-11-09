package com.gym_app.core.indicator;

import com.gym_app.core.dao.TrainingJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainingSessionCountHealthIndicator implements HealthIndicator {
    private final TrainingJpaDao trainingRepository;

    @Autowired
    public TrainingSessionCountHealthIndicator(TrainingJpaDao trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Health health() {
        long upcomingSessions = trainingRepository.getAll().size();
        if (upcomingSessions >= 0 && upcomingSessions <= 10) {
            return Health.up().withDetail("upcomingSessions", upcomingSessions).build();
        } else {
            return Health.down().withDetail("upcomingSessions", upcomingSessions)
                    .withDetail("message", "Check for scheduling issues").build();
        }
    }
}
