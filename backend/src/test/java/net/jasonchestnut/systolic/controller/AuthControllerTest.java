package net.jasonchestnut.systolic.controller;

import net.jasonchestnut.systolic.dto.LoginRequest;
import net.jasonchestnut.systolic.dto.LoginResponse;
import net.jasonchestnut.systolic.dto.RegistrationRequest;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.repository.PatientRepository;
import net.jasonchestnut.systolic.service.AuthService;
import net.jasonchestnut.systolic.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthService authService;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AuthController authController;

    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest("testuser", "test@example.com", "password123");
        loginRequest = new LoginRequest("testuser", "password123");
    }

    @Test
    void register_shouldReturnCreated_whenUserIsRegisteredSuccessfully() {
        // Arrange
        Patient patient = new Patient();
        when(authService.registerUser(any(RegistrationRequest.class))).thenReturn(patient);

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Patient registered successfully!", response.getBody());
        verify(authService, times(1)).registerUser(registrationRequest);
    }

    @Test
    void register_shouldThrowException_whenUserIsNotRegisteredSuccessfully() {
        // Arrange
        Patient patient = new Patient();
        when(authService.registerUser(any(RegistrationRequest.class))).thenThrow(new IllegalStateException("Registration failed"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            authController.register(registrationRequest);
        });

        // Verify that the patient repository was not called to save a patient
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void login_shouldReturnOkWithToken_whenCredentialsAreValid() {
        // Arrange
        String username = loginRequest.username();
        String password = loginRequest.password();
        String expectedToken = "dummy.jwt.token";

        UserDetails userDetails = new User(username, password, new ArrayList<>());
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password))
        ).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().token());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {
        // Arrange
        String username = loginRequest.username();
        String password = loginRequest.password();

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password))
        ).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authController.login(loginRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }
}