package net.jasonchestnut.systolic.dto;

import java.time.LocalDateTime;

public record MedicationLogResponse(
        Long id,
        Long medicationId,
        String medicationName,
        LocalDateTime takenAt,
        String notes
) {}