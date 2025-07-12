package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.ActivityLog;
import net.jasonchestnut.systolic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByPatient(Patient patient);
    List<ActivityLog> findByPatientAndTimestampBetween(Patient patient, LocalDateTime start, LocalDateTime end);
    List<ActivityLog> findByPatientOrderByTimestampDesc(Patient patient);
}