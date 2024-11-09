package com.gym_app.core.indicator;


import com.gym_app.core.dao.TrainerJpaDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainerActiveCountHealthIndicator implements HealthIndicator {
    private final TrainerJpaDaoImpl trainerRepository;

    @Autowired
    public TrainerActiveCountHealthIndicator(TrainerJpaDaoImpl trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Health health() {
        long activeTrainersCount = trainerRepository.getAll().stream().filter(trainer -> trainer.isActive()).count();
        if (activeTrainersCount > 1) {
            return Health.up().withDetail("activeTrainers", activeTrainersCount).build();
        } else {
            return Health.down().withDetail("activeTrainers", activeTrainersCount)
                    .withDetail("message", "Check for personnel statuses issues").build();
        }
    }
}
