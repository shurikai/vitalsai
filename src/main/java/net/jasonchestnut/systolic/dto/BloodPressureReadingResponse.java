package net.jasonchestnut.systolic.dto;

import java.util.HashMap;


public record BloodPressureReadingResponse(
        Long id,
        UserResponse user,
        Integer systolic,
        Integer diastolic,
        Integer pulse,
        String readingTimestamp,
        HashMap<String, Object> notes
) {
}
