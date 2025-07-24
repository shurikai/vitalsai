package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    // Find all sleep logs for a patient
    List<SleepLog> findByPatientOrderByStartTimeDesc(Patient patient);
    
    // Find sleep logs within a date range
    List<SleepLog> findByPatientAndStartTimeBetweenOrderByStartTimeDesc(
            Patient patient, LocalDateTime startTime, LocalDateTime endTime);
    
    // Find sleep logs with specific quality rating
    List<SleepLog> findByPatientAndQualityOrderByStartTimeDesc(Patient patient, Integer quality);
    
    // Find most recent sleep log
    Optional<SleepLog> findFirstByPatientOrderByEndTimeDesc(Patient patient);
    
    // Find overlapping sleep logs (for validation)
    @Query("SELECT s FROM SleepLog s WHERE s.patient = ?1 AND " +
           "((s.startTime BETWEEN ?2 AND ?3) OR (s.endTime BETWEEN ?2 AND ?3))")
    List<SleepLog> findOverlappingLogs(Patient patient, LocalDateTime start, LocalDateTime end);

    // Calculate average sleep duration for a time period
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (end_time - start_time))) " +
            "FROM sleep_logs WHERE patient_id = ?1 " +
            "AND start_time BETWEEN ?2 AND ?3",
            nativeQuery = true)
    Optional<Double> calculateAverageSleepDuration(Patient patient, LocalDateTime start, LocalDateTime end);

    // Calculate average sleep quality for a time period
    @Query("SELECT AVG(s.quality) FROM SleepLog s " +
           "WHERE s.patient = ?1 AND s.startTime BETWEEN ?2 AND ?3")
    Optional<Double> calculateAverageSleepQuality(Patient patient, LocalDateTime start, LocalDateTime end);
}