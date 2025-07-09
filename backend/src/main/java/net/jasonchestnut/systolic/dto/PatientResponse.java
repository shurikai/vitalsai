package net.jasonchestnut.systolic.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for patient details, excluding sensitive information.")
public record PatientResponse(
        @Schema(description = "Unique identifier for the patient")
        Long id,
        @Schema(description = "The patient's username")
        String username,
        @Schema(description = "The patient's email address")
        String email,
        @Schema(description = "First name of the patient")
        String firstName,
        @Schema(description = "Last name of the patient")
        String lastName
) {}