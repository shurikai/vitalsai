package net.jasonchestnut.systolic.repository;

import net.jasonchestnut.systolic.entity.Medication;
import net.jasonchestnut.systolic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPatient(Patient patient);
    List<Medication> findByPatientAndNameContainingIgnoreCase(Patient patient, String name);
}