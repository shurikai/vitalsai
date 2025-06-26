package net.jasonchestnut.systolic.config;

import net.jasonchestnut.systolic.entity.BloodPressureReading;
import net.jasonchestnut.systolic.entity.User;
import net.jasonchestnut.systolic.repository.BloodPressureReadingRepository;
import net.jasonchestnut.systolic.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
    CommandLineRunner initDatabase(UserRepository userRepository, BloodPressureReadingRepository readingRepository) {
        return args -> {
            // Clean up previous data
            log.info("Clearing existing data in the database for development testing...");
            readingRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();

            log.info("Database cleared. Initializing with development data...");
            // Create and save a user
            User devUser = userRepository.save(new User(
                    "jason@email.com",
                    "password123",
                    "jason",
                    "chestnut")
            );

            BloodPressureReading reading1 = new BloodPressureReading(
                    devUser, 118, 78, 60, OffsetDateTime.now().minusDays(2)
            );
            reading1.setNotes(Map.of("feeling", "relaxed", "arm", "left"));

            BloodPressureReading reading2 = new BloodPressureReading(
                    devUser, 122, 81, 65, OffsetDateTime.now().minusDays(1)
            );
            reading2.setNotes(Map.of("feeling", "normal", "arm", "left"));

            BloodPressureReading reading3 = new BloodPressureReading(
                    devUser, 135, 85, 75, OffsetDateTime.now()
            );
            reading3.setNotes(Map.of("feeling", "stressed from work", "arm", "right"));

            // Save all readings to the database
            readingRepository.saveAll(List.of(reading1, reading2, reading3));

            log.info("Database has been initialized with development data.");
        };
    }
}