package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals, Long> {

    /**
     * Finds all vital readings for a specific user, ordered by the most recent.
     * The method signature is updated to use the new VitalReading entity.
     *
     * @param userId The ID of the user.
     * @return A list of vital readings.
     */
    List<Vitals> findByPatientIdOrderByReadingTimestampDesc(Long userId);
}