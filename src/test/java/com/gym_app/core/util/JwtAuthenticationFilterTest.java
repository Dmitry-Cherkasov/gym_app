package com.gym_app.core.util;

import com.gym_app.core.services.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Ensure no leftover security context
    }

    @Test
    void testDoFilterInternal_ValidToken() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testUser";

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.isValid(token)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(username);

        // Act
        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {
        // Arrange
        String token = "invalid.jwt.token";

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenProvider.isValid(token)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    void testDoFilterInternal_NoToken() throws Exception {
        // Arrange
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
}
