package net.jasonchestnut.systolic.events.dto;

public enum EventType {
    ACTIVITY_LOGGED,
    MEDICATION_TAKEN,
    VITALS_READING_RECORDED;

    public String getValue() {
        return this.name();
    }
}
