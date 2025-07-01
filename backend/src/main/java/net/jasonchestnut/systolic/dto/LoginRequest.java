package net.jasonchestnut.systolic.dto;

/**
 * A Data Transfer Object for the login request payload.
 * Using a record provides an immutable, concise container for the data.
 *
 * @param username The user's username.
 * @param password The user's raw password.
 */
public record LoginRequest(String username, String password) {
}
