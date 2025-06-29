package net.jasonchestnut.systolic.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record BloodPressureReadingRequest(
        Integer systolic,
        Integer diastolic,
        Integer pulse,
        OffsetDateTime readingTimestamp,
        Map<String, String> notes
) {}
