package com.gym_app.core.dto.profile;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeSummaryTest {

    @Test
    public void testTraineeSummarySettersAndGetters() {
        // Create a new TraineeSummary instance
        TraineeSummary traineeSummary = new TraineeSummary();

        // Set values
        traineeSummary.setUsername("traineeUser");
        traineeSummary.setFirstName("John");
        traineeSummary.setLastName("Doe");

        // Verify getters return the expected values
        assertEquals("traineeUser", traineeSummary.getUsername());
        assertEquals("John", traineeSummary.getFirstName());
        assertEquals("Doe", traineeSummary.getLastName());
    }
}
