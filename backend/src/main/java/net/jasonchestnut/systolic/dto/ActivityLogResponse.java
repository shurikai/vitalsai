package net.jasonchestnut.systolic.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record ActivityLogResponse(
        Long id,
        String activityType,
        LocalDateTime timestamp,
        Duration duration,
        String intensity,
        String notes
) {}