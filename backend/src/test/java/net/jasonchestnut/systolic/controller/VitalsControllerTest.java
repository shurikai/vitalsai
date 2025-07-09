package net.jasonchestnut.systolic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jasonchestnut.systolic.config.DataInitializer;
import net.jasonchestnut.systolic.dto.VitalReadingRequest;
import net.jasonchestnut.systolic.dto.VitalReadingResponse;
import net.jasonchestnut.systolic.repository.PatientRepository;
import net.jasonchestnut.systolic.service.JwtService;
import net.jasonchestnut.systolic.service.VitalsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = VitalsController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = DataInitializer.class
        )
)
@DisplayName("VitalsController Tests")
class VitalsControllerTest {

    @Autowired
    private MockMvc mockMvc; // The main tool for simulating HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    @MockBean // Creates a mock of VitalsService and adds it to the application context
    private VitalsService vitalsService;

    @MockBean
    private PatientRepository patientRepository; // Mock for PatientRepository

    @MockBean
    private JwtService jwtService; // Mock for JWT service

    @Test
    @DisplayName("GET /api/patients/me/vitals should return 401 for unauthenticated user")
    void getVitalsForCurrentUser_whenUnauthenticated_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/patients/me/vitals"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser") // Simulates a logged-in user
    @DisplayName("GET /api/patients/me/vitals should return list of vitals for authenticated user")
    void getVitalsForCurrentUser_whenAuthenticated_shouldReturnVitals() throws Exception {
        // Arrange
        VitalReadingResponse response = new VitalReadingResponse(1L, 120, 80, 60, OffsetDateTime.now(), null);
        when(vitalsService.getVitalsForPatient("testuser")).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/patients/me/vitals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].systolic").value(120));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /api/patients/me/vitals should create a new vital")
    void createVital_shouldSucceed() throws Exception {
        // Arrange
        VitalReadingRequest request = new VitalReadingRequest(130, 85, 70, OffsetDateTime.now(), null);
        VitalReadingResponse response = new VitalReadingResponse(2L, 130, 85, 70, request.readingTimestamp(), null);

        when(vitalsService.createVitalForPatient(any(VitalReadingRequest.class), eq("testuser"))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/patients/me/vitals")
                        .with(csrf()) // Include a CSRF token for POST requests
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.systolic").value(130));
    }
}