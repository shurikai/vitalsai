package net.jasonchestnut.systolic.dto;

public record MedicationResponse(
        Long id,
        String name,
        String dosage,
        String frequency,
        String instructions
) {}