package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.VitalReadingRequest;
import net.jasonchestnut.systolic.dto.VitalReadingResponse;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Role;
import net.jasonchestnut.systolic.entity.Vitals;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.exception.UnauthorizedException;
import net.jasonchestnut.systolic.mapper.VitalMapper;
import net.jasonchestnut.systolic.repository.PatientRepository;
import net.jasonchestnut.systolic.repository.VitalsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VitalsService Tests")
class VitalsServiceTest {

    @Mock
    private VitalsRepository vitalsRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private VitalMapper vitalMapper;

    @InjectMocks
    private VitalsService vitalsService;

    @Captor
    private ArgumentCaptor<Vitals> vitalsArgumentCaptor;

    private Patient patient;
    private Vitals vitals;
    private VitalReadingRequest vitalReadingRequest;
    private VitalReadingResponse vitalReadingResponse;

    @BeforeEach
    void setUp() {
        // --- Refactored Test Data Setup ---
        // Use all-args constructors to create immutable, realistic test fixtures.
        // This approach is cleaner and avoids relying on setters for IDs.
        OffsetDateTime now = OffsetDateTime.now();
        patient = new Patient(
                1L, "testuser", "test@example.com", "password",
                "Test", "User", now, now, List.of(), Role.ROLE_USER
        );

        // The 'vitals' object now has its patient set directly at creation.
        vitals = new Vitals(
                100L, patient, 120, 80, 60, now, null, now, now
        );

        vitalReadingRequest = new VitalReadingRequest(125, 85, 65, now, null);
        vitalReadingResponse = new VitalReadingResponse(100L, 120, 80, 60, now, null);
    }

    @Test
    @DisplayName("getVitalByIdForPatient should return vital when user is owner")
    void getVitalByIdForPatient_whenOwner_shouldSucceed() {
        // Arrange
        when(patientRepository.findByUsername("testuser")).thenReturn(Optional.of(patient));
        when(vitalsRepository.findById(100L)).thenReturn(Optional.of(vitals));
        when(vitalMapper.toVitalReadingResponse(vitals)).thenReturn(vitalReadingResponse);

        // Act
        VitalReadingResponse result = vitalsService.getVitalByIdForPatient(100L, "testuser");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(100L);
        verify(patientRepository).findByUsername("testuser");
        verify(vitalsRepository).findById(100L);
    }

    @Test
    @DisplayName("getVitalByIdForPatient should throw UnauthorizedException when user is not owner")
    void getVitalByIdForPatient_whenNotOwner_shouldThrowUnauthorized() {
        // Arrange
        Patient anotherPatient = new Patient(
                2L, "anotheruser", "another@example.com", "password",
                "Another", "User", OffsetDateTime.now(), OffsetDateTime.now(), List.of(), Role.ROLE_USER
        );

        // The vital belongs to patient 1, but patient 2 is trying to access it
        when(patientRepository.findByUsername("anotheruser")).thenReturn(Optional.of(anotherPatient));
        when(vitalsRepository.findById(100L)).thenReturn(Optional.of(vitals));

        // Act & Assert
        assertThatThrownBy(() -> vitalsService.getVitalByIdForPatient(100L, "anotheruser"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Access Denied. You do not have permission to access this resource.");
    }

    @Test
    @DisplayName("getVitalByIdForPatient should throw ResourceNotFoundException for non-existent vital")
    void getVitalByIdForPatient_whenVitalNotFound_shouldThrowNotFound() {
        // Arrange
        when(patientRepository.findByUsername("testuser")).thenReturn(Optional.of(patient));
        when(vitalsRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> vitalsService.getVitalByIdForPatient(999L, "testuser"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("createVitalForPatient should create and return a new vital")
    void createVitalForPatient_shouldSucceed() {
        // Arrange
        // Create a new Vitals object without an ID to simulate creation
        Vitals newVital = new Vitals(null, patient, 125, 85, 65, vitalReadingRequest.readingTimestamp(), null, null, null);
        Vitals savedVital = new Vitals(101L, patient, 125, 85, 65, vitalReadingRequest.readingTimestamp(), null, OffsetDateTime.now(), OffsetDateTime.now());
        VitalReadingResponse expectedResponse = new VitalReadingResponse(101L, 125, 85, 65, vitalReadingRequest.readingTimestamp(), null);

        when(patientRepository.findByUsername("testuser")).thenReturn(Optional.of(patient));
        when(vitalMapper.toEntity(any(VitalReadingRequest.class))).thenReturn(newVital);
        when(vitalsRepository.save(any(Vitals.class))).thenReturn(savedVital);
        when(vitalMapper.toVitalReadingResponse(any(Vitals.class))).thenReturn(expectedResponse);

        // Act
        VitalReadingResponse result = vitalsService.createVitalForPatient(vitalReadingRequest, "testuser");

        verify(vitalsRepository).save(vitalsArgumentCaptor.capture());
        Vitals capturedVital = vitalsArgumentCaptor.getValue();

        assertThat(capturedVital).isNotNull();
        assertThat(capturedVital.getPatient()).isEqualTo(patient);
        assertThat(capturedVital.getSystolic()).isEqualTo(125);
        assertThat(capturedVital.getDiastolic()).isEqualTo(85);
    }
}