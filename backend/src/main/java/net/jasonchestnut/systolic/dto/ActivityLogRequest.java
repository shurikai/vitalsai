package net.jasonchestnut.systolic.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

public record ActivityLogRequest(
        @NotNull(message = "Activity type is required")
        String activityType,

        @NotNull(message = "Timestamp is required")
        LocalDateTime timestamp,

        @NotNull(message = "Duration is required")
        Duration duration,

        @NotNull(message = "Intensity is required")
        String intensity,

        String notes
) {}