package net.jasonchestnut.systolic.config;

import net.jasonchestnut.systolic.entity.*;
import net.jasonchestnut.systolic.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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
            ActivityLogRepository activityLogRepository,
            MedicationRepository medicationRepository,
            MedicationLogRepository medicationLogRepository,
            SleepLogRepository sleepLogRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Clean up previous data
            log.info("Clearing existing data in the database for development testing...");
            medicationLogRepository.deleteAllInBatch();
            sleepLogRepository.deleteAllInBatch();
            activityLogRepository.deleteAllInBatch();
            vitalsRepository.deleteAllInBatch();
            medicationRepository.deleteAllInBatch();
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

            // Create a second user
            Patient patient2 = new Patient();
            patient2.setUsername("sarahsmith");
            patient2.setEmail("sarah@email.com");
            patient2.setPassword(passwordEncoder.encode("password456"));
            patient2.setFirstName("sarah");
            patient2.setLastName("smith");
            patient2.setRole(Role.ROLE_USER);
            patientRepository.save(patient2);

            // Initialize Vitals data
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

            // Initialize Medication data
            Medication med1 = new Medication();
            med1.setName("Lisinopril");
            med1.setDosage("10mg");
            med1.setFrequency("Once daily");
            med1.setInstructions("Take in the morning with food");
            med1.setPatient(patient);
            
            Medication med2 = new Medication();
            med2.setName("Metformin");
            med2.setDosage("500mg");
            med2.setFrequency("Twice daily");
            med2.setInstructions("Take with meals");
            med2.setPatient(patient);
            
            medicationRepository.saveAll(List.of(med1, med2));
            
            // Initialize MedicationLog data
            MedicationLog medLog1 = new MedicationLog();
            medLog1.setMedication(med1);
            medLog1.setPatient(patient);
            medLog1.setTakenAt(LocalDateTime.now().minusDays(2));
            medLog1.setNotes("Felt fine after taking");
            
            MedicationLog medLog2 = new MedicationLog();
            medLog2.setMedication(med1);
            medLog2.setPatient(patient);
            medLog2.setTakenAt(LocalDateTime.now().minusDays(1));
            
            MedicationLog medLog3 = new MedicationLog();
            medLog3.setMedication(med2);
            medLog3.setPatient(patient);
            medLog3.setTakenAt(LocalDateTime.now().minusDays(2));
            medLog3.setNotes("Took with breakfast");
            
            medicationLogRepository.saveAll(List.of(medLog1, medLog2, medLog3));
            
            // Initialize ActivityLog data
            ActivityLog activity1 = new ActivityLog();
            activity1.setPatient(patient);
            activity1.setActivityType("Walking");
            activity1.setTimestamp(LocalDateTime.now().minusDays(3));
            activity1.setDuration(Duration.ofMinutes(30));
            activity1.setIntensity(Intensity.MEDIUM);
            activity1.setNotes("Evening walk in the park");
            
            ActivityLog activity2 = new ActivityLog();
            activity2.setPatient(patient);
            activity2.setActivityType("Running");
            activity2.setTimestamp(LocalDateTime.now().minusDays(1));
            activity2.setDuration(Duration.ofMinutes(45));
            activity2.setIntensity(Intensity.HIGH);
            activity2.setNotes("Morning run");
            
            ActivityLog activity3 = new ActivityLog();
            activity3.setPatient(patient);
            activity3.setActivityType("Yoga");
            activity3.setTimestamp(LocalDateTime.now().minusHours(5));
            activity3.setDuration(Duration.ofMinutes(60));
            activity3.setIntensity(Intensity.LOW);
            
            activityLogRepository.saveAll(List.of(activity1, activity2, activity3));
            
            // Initialize SleepLog data
            SleepLog sleep1 = new SleepLog(
                patient,
                LocalDateTime.now().minusDays(3).withHour(22).withMinute(0),
                LocalDateTime.now().minusDays(2).withHour(6).withMinute(30),
                4,
                1
            );
            sleep1.setNotes("Slept well, woke up once for water");
            
            SleepLog sleep2 = new SleepLog(
                patient,
                LocalDateTime.now().minusDays(2).withHour(23).withMinute(15),
                LocalDateTime.now().minusDays(1).withHour(7).withMinute(0),
                3,
                2
            );
            sleep2.setNotes("Restless night, noise from neighbors");
            
            SleepLog sleep3 = new SleepLog(
                patient,
                LocalDateTime.now().minusDays(1).withHour(22).withMinute(30),
                LocalDateTime.now().withHour(6).withMinute(45),
                5,
                0
            );
            sleep3.setNotes("Excellent sleep, felt refreshed");
            
            sleepLogRepository.saveAll(List.of(sleep1, sleep2, sleep3));

            log.info("Database has been initialized with development data for all entities.");
        };
    }
}