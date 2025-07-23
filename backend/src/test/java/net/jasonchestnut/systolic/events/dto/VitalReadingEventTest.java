package net.jasonchestnut.systolic.events.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VitalReadingEvent Tests")
class VitalReadingEventTest {

    @Test
    @DisplayName("Should create event and verify accessors")
    void testVitalReadingEventCreationAndAccessors() {
        VitalReadingEvent event = new VitalReadingEvent(1L, 101L, OffsetDateTime.now(), 120, 80, 60);

        assertThat(event.patientId()).isEqualTo(1L);
        assertThat(event.vitalId()).isEqualTo(101L);
        assertThat(event.readingTimestamp()).isNotNull();
        assertThat(event.systolic()).isEqualTo(120);
        assertThat(event.diastolic()).isEqualTo(80);
        assertThat(event.pulse()).isEqualTo(60);
    }
}