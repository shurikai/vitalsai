package net.jasonchestnut.systolic.config;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

    /**
     * Unit tests for the JwtUtil class.
     */
    @ExtendWith(MockitoExtension.class)
    class JwtUtilTest {

        // A secret key that is long enough for the HS256 algorithm (at least 256 bits).
        private static final String SECRET_KEY = "test-secret-key-that-is-long-enough-for-the-hs256-algorithm";
        private static final long EXPIRATION_MS = 3600000; // 1 hour
        private static final String TEST_USERNAME = "testuser";

        private JwtUtil jwtUtil;

        @Mock
        private UserDetails userDetails;

        @BeforeEach
        void setUp() {
            // Initialize JwtUtil with test values before each test
            jwtUtil = new JwtUtil(SECRET_KEY, EXPIRATION_MS);
        }

        @Test
        @DisplayName("generateToken should create a valid, non-empty JWT")
        void generateToken_shouldCreateValidJwt() {
            String token = jwtUtil.generateToken(TEST_USERNAME);
            when(userDetails.getUsername()).thenReturn(TEST_USERNAME);

            assertNotNull(token);
            assertFalse(token.isEmpty());

            // We can verify the token using the same utility's public methods
            String extractedUsername = jwtUtil.extractUsername(token);
            assertEquals(TEST_USERNAME, extractedUsername);
            assertTrue(jwtUtil.validateToken(token, userDetails));
        }

        @Test
        @DisplayName("extractUsername should return the correct username from a valid token")
        void extractUsername_shouldReturnCorrectUsername() {
            String token = jwtUtil.generateToken(TEST_USERNAME);
            String username = jwtUtil.extractUsername(token);
            assertEquals(TEST_USERNAME, username);
        }

        @Test
        @DisplayName("validateToken should return true for a valid token and matching user")
        void validateToken_shouldReturnTrueForValidTokenAndMatchingUser() {
            String token = jwtUtil.generateToken(TEST_USERNAME);
            when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
            assertTrue(jwtUtil.validateToken(token, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false when the token username does not match UserDetails")
        void validateToken_shouldReturnFalseWhenUsernameDoesNotMatch() {
            // Token is for "testuser"
            String token = jwtUtil.generateToken(TEST_USERNAME);
            // But our UserDetails mock is for a different user
            when(userDetails.getUsername()).thenReturn("anotheruser");

            assertFalse(jwtUtil.validateToken(token, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false for an expired token")
        void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
            // Create a JwtUtil with a very short expiration time (1ms)
            JwtUtil shortLivedJwtUtil = new JwtUtil(SECRET_KEY, 1);
            String token = shortLivedJwtUtil.generateToken(TEST_USERNAME);

            // Wait for a moment to ensure the token is expired
            Thread.sleep(50);

            assertFalse(shortLivedJwtUtil.validateToken(token, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false for a token with an invalid signature")
        void validateToken_shouldReturnFalseForTokenWithInvalidSignature() {
            // Create another JwtUtil with a different secret key
            JwtUtil anotherJwtUtil = new JwtUtil("another-different-secret-key-for-testing-purposes", EXPIRATION_MS);

            // Generate a token with the original util
            String token = jwtUtil.generateToken(TEST_USERNAME);

            // Try to validate with the other util, which uses the wrong key.
            // This will cause a signature mismatch.
            assertFalse(anotherJwtUtil.validateToken(token, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false for a malformed token string")
        void validateToken_shouldReturnFalseForMalformedToken() {
            String malformedToken = "this.is.not.a.valid.jwt";
            assertFalse(jwtUtil.validateToken(malformedToken, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false for a null token")
        void validateToken_shouldReturnFalseForNullToken() {
            assertFalse(jwtUtil.validateToken(null, userDetails));
        }

        @Test
        @DisplayName("validateToken should return false for an empty token")
        void validateToken_shouldReturnFalseForEmptyToken() {
            assertFalse(jwtUtil.validateToken("", userDetails));
        }

        @Test
        @DisplayName("extractUsername should throw JwtException for a token with an invalid signature")
        void extractUsername_shouldThrowExceptionForInvalidSignature() {
            JwtUtil anotherJwtUtil = new JwtUtil("another-different-secret-key-for-testing-purposes", EXPIRATION_MS);
            String tokenWithWrongSignature = anotherJwtUtil.generateToken(TEST_USERNAME);

            // Trying to extract claims with the original util (which has a different key) should fail
            assertThrows(JwtException.class, () -> jwtUtil.extractUsername(tokenWithWrongSignature));
        }

        @Test
        @DisplayName("extractUsername should throw JwtException for a malformed token")
        void extractUsername_shouldThrowExceptionForMalformedToken() {
            String malformedToken = "this.is.not.a.valid.jwt";
            assertThrows(JwtException.class, () -> jwtUtil.extractUsername(malformedToken));
        }
    }