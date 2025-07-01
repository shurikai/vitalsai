package net.jasonchestnut.systolic.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for user details")
public record UserResponse(
    @Schema(description = "Unique identifier for the user")
    Long id,
    @Schema(description = "First name of the user")
    String firstName,
    @Schema(description = "Last name of the user")
    String lastName,
    @Schema(description = "Email address of the user")
    String email
) {
}
