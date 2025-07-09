package net.jasonchestnut.systolic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Response object for a single vital reading.")
public record VitalReadingResponse(
        @Schema(description = "Unique identifier for the vital reading")
        Long id,
        @Schema(description = "Systolic blood pressure value (mmHg)")
        Integer systolic,
        @Schema(description = "Diastolic blood pressure value (mmHg)")
        Integer diastolic,
        @Schema(description = "Pulse rate (beats per minute)")
        Integer pulse,
        @Schema(description = "The specific time the reading was taken")
        OffsetDateTime readingTimestamp,
        @Schema(description = "A flexible map of notes or context")
        Map<String, Object> notes
) {}