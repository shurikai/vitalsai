package net.jasonchestnut.systolic.service;

import net.jasonchestnut.systolic.entity.ActivityLog;
import net.jasonchestnut.systolic.entity.Patient;
import net.jasonchestnut.systolic.exception.ResourceNotFoundException;
import net.jasonchestnut.systolic.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public ActivityLog save(ActivityLog activityLog) {
        return activityLogRepository.save(activityLog);
    }

    @Transactional(readOnly = true)
    public ActivityLog findById(Long id) {
        return activityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActivityLog not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findByPatient(Patient patient) {
        return activityLogRepository.findByPatient(patient);
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findByPatientAndDateRange(Patient patient, LocalDateTime start, LocalDateTime end) {
        return activityLogRepository.findByPatientAndTimestampBetween(patient, start, end);
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> findRecentActivities(Patient patient) {
        return activityLogRepository.findByPatientOrderByTimestampDesc(patient);
    }

    public void delete(Long id) {
        activityLogRepository.deleteById(id);
    }
}