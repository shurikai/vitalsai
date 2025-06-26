package net.jasonchestnut.systolic.dto;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String email
) {
}
