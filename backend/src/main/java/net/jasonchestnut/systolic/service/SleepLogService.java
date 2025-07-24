package net.jasonchestnut.systolic.service;

import jakarta.validation.Valid;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.entity.SleepLog;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.repository.SleepLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SleepLogService {
    private final SleepLogRepository sleepLogRepository;

    public SleepLogService(SleepLogRepository sleepLogRepository) {
        this.sleepLogRepository = sleepLogRepository;
    }

    public SleepLog save(@Valid SleepLog sleepLog) {
        // TODO: Generate Kafka events as necessary.
        return sleepLogRepository.save(sleepLog);
    }

    @Transactional(readOnly = true)
    public List<SleepLog> findByPatient(Patient patient) {
        return sleepLogRepository.findByPatientOrderByStartTimeDesc(patient);
    }

    @Transactional(readOnly = true)
    public List<SleepLog> findByPatientAndDateRange(Patient patient, LocalDateTime start, LocalDateTime end) {
        return sleepLogRepository.findByPatientAndStartTimeBetweenOrderByStartTimeDesc(patient, start, end);
    }

    @Transactional(readOnly = true)
    public List<SleepLog> findByPatientAndQuality(Patient patient, Integer quality ) {
        return sleepLogRepository.findByPatientAndQualityOrderByStartTimeDesc(patient, quality);
    }

    @Transactional(readOnly = true)
    public SleepLog findFirstByPatient(Patient patient ) {
        return sleepLogRepository.findFirstByPatientOrderByEndTimeDesc(patient)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Sleep log not found for patient with id: " + patient.getId())
                );
    }
}
