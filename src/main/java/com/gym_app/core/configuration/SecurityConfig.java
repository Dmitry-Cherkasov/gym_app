package com.gym_app.core.configuration;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

        @Bean
        public AuthenticationEntity config(){
            return new AuthenticationEntity();
        }

}

