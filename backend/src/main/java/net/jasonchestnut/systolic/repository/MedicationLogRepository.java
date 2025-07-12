package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.MedicationLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {
    List<MedicationLog> findByPatient(Patient patient);
    List<MedicationLog> findByPatientAndTakenAtBetween(Patient patient, LocalDateTime start, LocalDateTime end);
    List<MedicationLog> findByPatientAndMedication(Patient patient, Medication medication);
    List<MedicationLog> findByPatientOrderByTakenAtDesc(Patient patient);
}