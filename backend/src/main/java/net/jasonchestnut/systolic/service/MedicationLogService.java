package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.entity.MedicationLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.Medication;
import net.jasonchestnut.systolic.events.EventProducerService;
import net.jasonchestnut.systolic.events.dto.MedicationLogEvent;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.exception.UnauthorizedException;
import net.jasonchestnut.systolic.repository.MedicationLogRepository;
import net.jasonchestnut.systolic.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MedicationLogService {
    private final MedicationLogRepository medicationLogRepository;
    private final MedicationRepository medicationRepository;
    private final EventProducerService eventProducerService;

    public MedicationLogService(MedicationLogRepository medicationLogRepository, MedicationRepository medicationRepository, EventProducerService eventProducerService) {
        this.medicationLogRepository = medicationLogRepository;
        this.medicationRepository = medicationRepository;
        this.eventProducerService = eventProducerService;
    }

    public MedicationLog save(MedicationLog medicationLog) {
        // Security Check: Ensure the medication being logged belongs to the patient.
        Medication medication = medicationRepository.findById(medicationLog.getMedication().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + medicationLog.getMedication().getId()));

        if (!medication.getPatient().getId().equals(medicationLog.getPatient().getId())) {
            throw new UnauthorizedException("Access Denied: Medication does not belong to the current patient.");
        }

        MedicationLog savedLog = medicationLogRepository.save(medicationLog);

        // Produce Kafka event
        MedicationLogEvent event = new MedicationLogEvent(
                savedLog.getPatient().getId(),
                savedLog.getId(),
                savedLog.getMedication().getId(),
                savedLog.getMedication().getName(),
                savedLog.getTakenAt());
        eventProducerService.sendMedicationLogEvent(event);

        return savedLog;
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