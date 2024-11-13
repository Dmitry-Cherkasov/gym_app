package com.gym_app.core.configuration;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/trainee", "/trainer")
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST,"/login", "/trainee", "/trainer").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/trainee/{username}").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public AuthenticationEntity config() {
        return new AuthenticationEntity();
    }

}

