package net.jasonchestnut.systolic.dto;

// A simple record for the registration payload
public record RegistrationRequest(String username, String password, String email) {
}
