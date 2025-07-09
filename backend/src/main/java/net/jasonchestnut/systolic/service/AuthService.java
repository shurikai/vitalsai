package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.dto.RegistrationRequest;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Role;
import net.jasonchestnut.systolic.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// TODO: Add proper exception handling and validation
// TODO: Consider using a DTO for registration requests
// TODO: Add all user fields as needed
@Service
public class AuthService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Patient registerUser(RegistrationRequest request) {
        if (patientRepository.findByUsername(request.username()).isPresent()) {
            // In a real app, you'd throw a more specific, custom exception
            throw new IllegalStateException("Username '" + request.username() + "' is already taken.");
        }

        Patient newPatient = new Patient();
        newPatient.setUsername(request.username());
        newPatient.setEmail(request.email());
        newPatient.setPassword(passwordEncoder.encode(request.password()));
        newPatient.setRole(Role.ROLE_USER); // Assign a default role

        return patientRepository.save(newPatient);
    }
}