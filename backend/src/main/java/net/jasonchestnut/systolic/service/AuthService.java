package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.RegistrationRequest;
import net.jasonchestnut.systolic.entity.User;
import net.jasonchestnut.systolic.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// TODO: Add proper exception handling and validation
// TODO: Consider using a DTO for registration requests
// TODO: Add all user fields as needed
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            // In a real app, you'd throw a more specific, custom exception
            throw new IllegalStateException("Username '" + request.username() + "' is already taken.");
        }

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole("ROLE_USER"); // Assign a default role

        return userRepository.save(newUser);
    }
}