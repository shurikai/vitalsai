package net.jasonchestnut.systolic.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtRequestFilter;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("passwordEncoder bean should return BCryptPasswordEncoder")
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder,
                "The returned password encoder should be an instance of BCryptPasswordEncoder");
    }

    @Test
    @DisplayName("authenticationManager bean should retrieve from AuthenticationConfiguration")
    void authenticationManager_shouldReturnManagerFromConfig() throws Exception {
        // Given
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockManager);

        // When
        AuthenticationManager resultManager = securityConfig.authenticationManager(authenticationConfiguration);

        // Then
        assertNotNull(resultManager);
        assertEquals(mockManager, resultManager, "The AuthenticationManager should be the one provided by the configuration");
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    @DisplayName("securityFilterChain should configure HttpSecurity correctly")
    void securityFilterChain_shouldConfigureHttpSecurity() throws Exception {
        // --- Arrange ---
        // Mock the fluent API chain to return the HttpSecurity mock itself
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

        // --- Act ---
        securityConfig.securityFilterChain(httpSecurity);

        // --- Assert ---

        // 1. Verify CSRF is disabled
        ArgumentCaptor<Customizer<CsrfConfigurer<HttpSecurity>>> csrfCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        verify(httpSecurity).csrf(csrfCustomizerCaptor.capture());
        CsrfConfigurer<HttpSecurity> csrfConfigurer = mock(CsrfConfigurer.class);
        csrfCustomizerCaptor.getValue().customize(csrfConfigurer);
        verify(csrfConfigurer).disable();

        // 2. Verify authorization rules
        ArgumentCaptor<Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>> authCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        verify(httpSecurity).authorizeHttpRequests(authCustomizerCaptor.capture());

        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        var authorizedUrlMock = mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);
        var anyRequestAuthorizedUrlMock = mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        // When requestMatchers(...) is called, return a mock that can handle permitAll()
        when(authRegistry.requestMatchers(any(String[].class))).thenReturn(authorizedUrlMock);
        // CRITICAL: When permitAll() is called, return the registry itself to continue the chain
        when(authorizedUrlMock.permitAll()).thenReturn(authRegistry);
        // When anyRequest() is called, return a different mock to handle authenticated()
        when(authRegistry.anyRequest()).thenReturn(anyRequestAuthorizedUrlMock);

        // Execute the captured lambda with our fully configured mock registry
        authCustomizerCaptor.getValue().customize(authRegistry);

        // Now, verify the full chain of calls
        verify(authRegistry).requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**");
        verify(authorizedUrlMock).permitAll();
        verify(authRegistry).anyRequest();
        verify(anyRequestAuthorizedUrlMock).authenticated();
        // --- End of corrected logic ---


        // 3. Verify session management is STATELESS
        ArgumentCaptor<Customizer<SessionManagementConfigurer<HttpSecurity>>> sessionCustomizerCaptor = ArgumentCaptor.forClass(Customizer.class);
        verify(httpSecurity).sessionManagement(sessionCustomizerCaptor.capture());
        SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer = mock(SessionManagementConfigurer.class);
        sessionCustomizerCaptor.getValue().customize(sessionManagementConfigurer);
        verify(sessionManagementConfigurer).sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 4. Verify JWT filter is added correctly
        verify(httpSecurity).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 5. Verify the chain is built
        verify(httpSecurity, times(1)).build();
    }
}