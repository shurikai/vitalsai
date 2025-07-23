package net.jasonchestnut.systolic.events.dto;

import java.time.OffsetDateTime;

public record VitalReadingEvent(
        Long patientId,
        Long vitalId,
        OffsetDateTime readingTimestamp,
        Integer systolic,
        Integer diastolic,
        Integer pulse
) {
}