package net.jasonchestnut.systolic.events.dto;

import net.jasonchestnut.systolic.entity.Intensity;

import java.time.Duration;
import java.time.LocalDateTime;

public record ActivityLogEvent(
        Long patientId,
        EventMetadata metadata,
        ActivityType activityType,
        Duration duration,
        Intensity intensity,
        String notes) {}