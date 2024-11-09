package com.gym_app.core.configuration;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private AuthenticationEntity authenticationEntity;

    @Test
    public void testAuthenticationEntityBean() {
        assertNotNull(authenticationEntity, "The AuthenticationEntity bean should not be null.");
        System.out.println(authenticationEntity);
    }
}