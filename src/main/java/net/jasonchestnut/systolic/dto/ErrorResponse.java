package net.jasonchestnut.systolic.dto;

import java.time.OffsetDateTime;

public record ErrorResponse(
        int statusCode,
        String message,
        String path,
        OffsetDateTime timestamp
) {
}
