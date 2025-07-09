package net.jasonchestnut.systolic.config;

import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Role;
import net.jasonchestnut.systolic.entity.Vitals;
import net.jasonchestnut.systolic.repository.PatientRepository;
import net.jasonchestnut.systolic.repository.VitalsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    // Use a @Bean to define the CommandLineRunner.
    // Use @Profile("!prod") so this bean is NOT created when the "prod" profile is active.
    // This prevents seeding your production database by accident.
    @Bean
    @Profile("dev")
    @Transactional
    CommandLineRunner initDatabase(
            PatientRepository patientRepository,
            VitalsRepository vitalsRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Clean up previous data
            log.info("Clearing existing data in the database for development testing...");
            vitalsRepository.deleteAllInBatch();
            patientRepository.deleteAllInBatch();

            log.info("Database cleared. Initializing with development data...");
            // Create and save a user
            Patient patient = new Patient();
            patient.setUsername("jasonchestnut");
            patient.setEmail("jason@email.com");
            patient.setPassword(passwordEncoder.encode("password123"));
            patient.setFirstName("jason");
            patient.setLastName("chestnut");
            patient.setRole(Role.ROLE_USER);
            patientRepository.save(patient);

            Vitals reading1 = new Vitals(
                    patient, 118, 78, 60, OffsetDateTime.now().minusDays(2)
            );
            reading1.setNotes(Map.of("feeling", "relaxed", "arm", "left"));

            Vitals reading2 = new Vitals(
                    patient, 122, 81, 65, OffsetDateTime.now().minusDays(1)
            );
            reading2.setNotes(Map.of("feeling", "normal", "arm", "left"));

            Vitals reading3 = new Vitals(
                    patient, 135, 85, 75, OffsetDateTime.now()
            );
            reading3.setNotes(Map.of("feeling", "stressed from work", "arm", "right"));

            // Save all readings to the database
            vitalsRepository.saveAll(List.of(reading1, reading2, reading3));

            log.info("Database has been initialized with development data.");
        };
    }
}