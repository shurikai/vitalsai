package net.jasonchestnut.systolic.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MedicationLogRequest(
        @NotNull(message = "Medication ID is required")
        Long medicationId,

        @NotNull(message = "Taken at time is required")
        LocalDateTime takenAt,

        String notes
) {}