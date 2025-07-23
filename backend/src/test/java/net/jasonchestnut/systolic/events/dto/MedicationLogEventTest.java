package net.jasonchestnut.systolic.events.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MedicationLogEvent Tests")
class MedicationLogEventTest {

    @Test
    @DisplayName("Should create event and verify accessors")
    void testMedicationLogEventCreationAndAccessors() {
        MedicationLogEvent event = new MedicationLogEvent(1L, 101L, 201L, "Lisinopril", LocalDateTime.now());

        assertThat(event.patientId()).isEqualTo(1L);
        assertThat(event.medicationLogId()).isEqualTo(101L);
        assertThat(event.medicationId()).isEqualTo(201L);
        assertThat(event.medicationName()).isEqualTo("Lisinopril");
        assertThat(event.takenAt()).isNotNull();
    }
}