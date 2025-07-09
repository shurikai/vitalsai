package net.jasonchestnut.systolic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Request object for creating or updating a vital reading.")
public record VitalReadingRequest(
        @Schema(description = "Systolic blood pressure value (mmHg)")
        Integer systolic,
        @Schema(description = "Diastolic blood pressure value (mmHg)")
        Integer diastolic,
        @Schema(description = "Pulse rate (beats per minute)")
        Integer pulse,
        @Schema(description = "The specific time the reading was taken", example = "2023-10-27T10:00:00-04:00")
        OffsetDateTime readingTimestamp,
        @Schema(description = "A flexible map for additional notes or context")
        Map<String, String> notes
) {}