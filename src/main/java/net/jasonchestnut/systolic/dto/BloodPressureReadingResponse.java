package net.jasonchestnut.systolic.dto;

import java.time.OffsetDateTime;
import java.util.HashMap;


public record BloodPressureReadingResponse(
        Long id,
        UserResponse user,
        Integer systolic,
        Integer diastolic,
        Integer pulse,
        OffsetDateTime readingTimestamp,
        HashMap<String, Object> notes
) {
}
