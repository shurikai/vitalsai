package net.jasonchestnut.systolic.config;

import net.jasonchestnut.systolic.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    // Mocks for the filter's dependencies
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    // The instance of the filter we are testing, with mocks injected
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        // Clear the security context before each test to ensure isolation
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenTokenIsValid_thenSetsAuthenticationInContext() throws ServletException, IOException {
        // Arrange: Prepare test data and mock behavior
        final String token = "valid-jwt-token";
        final String username = "testuser";
        final String authHeader = "Bearer " + token;

        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // Act: Execute the filter's logic
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verify the outcome
        // 1. The security context should now hold an authentication object
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        // 2. The principal's username should match
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(username);
        // 3. The filter chain should have been called
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void whenTokenIsInvalid_thenContextRemainsEmpty() throws ServletException, IOException {
        // Arrange
        final String token = "invalid-jwt-token";
        final String username = "testuser";
        final String authHeader = "Bearer " + token;

        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false); // Token validation fails.

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // The security context should be empty because the token was invalid
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void whenTokenIsExpired_thenContextRemainsEmpty() throws ServletException, IOException {
        // Arrange
        final String token = "expired-jwt-token";
        final String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        // Simulate an expired token exception
        when(jwtService.extractUsername(token)).thenThrow(new ExpiredJwtException(null, null, "JWT expired"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // The security context should be empty
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void whenNoAuthorizationHeader_thenContextRemainsEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        // Ensure JWT logic was never triggered
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    void whenHeaderIsNotBearer_thenContextRemainsEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic some-other-token");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
    }
}
