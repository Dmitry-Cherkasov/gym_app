package com.gym_app.core.indicator;

import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.common.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class TrainingSessionCountHealthIndicatorTest {

    @Mock
    private TrainingJpaDao trainingRepository;

    @InjectMocks
    private TrainingSessionCountHealthIndicator healthIndicator;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHealthUpWithLowUpcomingSessions() {
        // Mocking 5 upcoming sessions
        when(trainingRepository.getAll()).thenReturn(generate(5));

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(5L, health.getDetails().get("upcomingSessions"));
    }

    @Test
    public void testHealthUpWithEdgeLowUpcomingSessions() {
        // Mocking 10 upcoming sessions, which is at the upper limit for "UP" status
        when(trainingRepository.getAll()).thenReturn(generate(10));

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(10L,health.getDetails().get("upcomingSessions"));
    }

    @Test
    public void testHealthDownWithHighUpcomingSessions() {
        // Mocking 15 upcoming sessions, which should trigger a "DOWN" status
        when(trainingRepository.getAll()).thenReturn(generate(15));

        Health health = healthIndicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals(15L,health.getDetails().get("upcomingSessions"));
    }

    @Test
    public void testHealthUpWithNoUpcomingSessions() {
        // Mocking 0 upcoming sessions
        when(trainingRepository.getAll()).thenReturn(new ArrayList<Training>());

        Health health = healthIndicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(0L,health.getDetails().get("upcomingSessions"));
    }

    private List<Training> generate(int n){
        ArrayList<Training> result = new ArrayList<>();
        for (int i=0; i< n; i++){
            result.add(new Training());
        }
        return result;
    }
}
