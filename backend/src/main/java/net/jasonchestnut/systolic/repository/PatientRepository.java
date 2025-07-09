package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a patient by their username. This is essential for the
     * UserDetailsService to load the user during authentication.
     *
     * @param username The username to search for.
     * @return An Optional containing the patient if found.
     */
    Optional<Patient> findByUsername(String username);

    /**
     * Finds a patient by their email address. This is useful for checking
     * for duplicate emails during registration or allowing email-based login.
     *
     * @param email The email to search for.
     * @return An Optional containing the patient if found.
     */
    Optional<Patient> findByEmail(String email);
}