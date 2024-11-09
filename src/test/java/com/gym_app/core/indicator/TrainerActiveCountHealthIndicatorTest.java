package com.gym_app.core.indicator;

import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.common.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TrainerActiveCountHealthIndicatorTest {

    @Mock
    private TrainerJpaDaoImpl trainerRepository;

    @InjectMocks
    private TrainerActiveCountHealthIndicator trainerHealthIndicator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHealthStatusUpWhenActiveTrainersMoreThanOne() {
        Trainer activeTrainer1 = new Trainer();
        activeTrainer1.setActive(true);
        Trainer activeTrainer2 = new Trainer();
        activeTrainer2.setActive(true);

        when(trainerRepository.getAll()).thenReturn(Arrays.asList(activeTrainer1, activeTrainer2));

        Health health = trainerHealthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(2L, health.getDetails().get("activeTrainers"));
    }

    @Test
    public void testHealthStatusDownWhenActiveTrainersOneOrLess() {
        Trainer activeTrainer = new Trainer();
        activeTrainer.setActive(true);

        when(trainerRepository.getAll()).thenReturn(Collections.singletonList(activeTrainer));

        Health health = trainerHealthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(1L, health.getDetails().get("activeTrainers"));
        assertTrue(health.getDetails().get("message").toString().contains("Check for personnel statuses issues"));
    }

    @Test
    public void testHealthStatusDownWhenNoActiveTrainers() {
        when(trainerRepository.getAll()).thenReturn(Collections.emptyList());

        Health health = trainerHealthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(0L, health.getDetails().get("activeTrainers"));
        assertTrue(health.getDetails().get("message").toString().contains("Check for personnel statuses issues"));
    }
}
