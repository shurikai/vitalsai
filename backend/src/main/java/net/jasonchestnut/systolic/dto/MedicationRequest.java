package net.jasonchestnut.systolic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicationRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Dosage is required")
        String dosage,

        @NotNull(message = "Frequency is required")
        String frequency,

        String instructions
) {}