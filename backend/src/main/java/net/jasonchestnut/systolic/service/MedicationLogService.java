package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.entity.MedicationLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Medication;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.repository.MedicationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MedicationLogService {
    private final MedicationLogRepository medicationLogRepository;

    public MedicationLogService(MedicationLogRepository medicationLogRepository) {
        this.medicationLogRepository = medicationLogRepository;
    }

    public MedicationLog save(MedicationLog medicationLog) {
        return medicationLogRepository.save(medicationLog);
    }

    @Transactional(readOnly = true)
    public MedicationLog findById(Long id) {
        return medicationLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicationLog not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MedicationLog> findByPatient(Patient patient) {
        return medicationLogRepository.findByPatient(patient);
    }

    @Transactional(readOnly = true)
    public List<MedicationLog> findByPatientAndDateRange(Patient patient, LocalDateTime start, LocalDateTime end) {
        return medicationLogRepository.findByPatientAndTakenAtBetween(patient, start, end);
    }

    @Transactional(readOnly = true)
    public List<MedicationLog> findByPatientAndMedication(Patient patient, Medication medication) {
        return medicationLogRepository.findByPatientAndMedication(patient, medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationLog> findRecentLogs(Patient patient) {
        return medicationLogRepository.findByPatientOrderByTakenAtDesc(patient);
    }

    public void delete(Long id) {
        medicationLogRepository.deleteById(id);
    }
}