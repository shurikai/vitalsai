package net.jasonchestnut.systolic.events.dto;

import java.time.LocalDateTime;

public record EventMetadata(
        String eventId,
        EventType eventType,
        LocalDateTime timestamp
) {}
