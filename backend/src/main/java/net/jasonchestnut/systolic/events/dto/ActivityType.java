package net.jasonchestnut.systolic.events.dto;

public enum ActivityType {
    WALKING("Walking"),
    RUNNING("Running"),
    CYCLING("Cycling"),
    SWIMMING("Swimming"),
    STRENGTH_TRAINING("Strength Training"),
    YOGA("Yoga"),
    OTHER("Other");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static ActivityType fromDisplayName(String displayName) {
        for(ActivityType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown activity type: " + displayName);
    }
}
