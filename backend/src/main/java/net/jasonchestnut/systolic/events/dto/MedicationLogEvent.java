package net.jasonchestnut.systolic.events.dto;

import java.time.LocalDateTime;

public record MedicationLogEvent(
        Long patientId,
        Long medicationLogId,
        Long medicationId,
        String medicationName,
        LocalDateTime takenAt
) {
}